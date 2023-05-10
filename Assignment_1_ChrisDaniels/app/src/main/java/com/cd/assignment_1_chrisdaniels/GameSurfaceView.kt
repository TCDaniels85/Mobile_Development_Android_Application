package com.cd.assignment_1_chrisdaniels

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.AudioAttributes
import android.media.SoundPool
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.random.Random
import kotlin.random.nextInt

/**
 * Surface view class which can be drawn on a separate thread from other UI controls to enable the main UI to run smoothly.
 * This class handles the game logic
 */
class GameSurfaceView(context: Context?, attrs: AttributeSet?) : SurfaceView(context, attrs), Runnable {
    var paint = Paint()

    //boolean values to control ending the game
    var isRunning = true
    var isGameOver = false
    var isTimeUp = false
    private lateinit var myThread: Thread
    private lateinit var myHolder: SurfaceHolder
    private var gameObjects = ArrayList<GameObject>()
    //Variables to be passed from sensors in game fragment
    var px: Float = 0f
    var py: Float = 0f
    var lux = 0f
    //records user score and life
    var score: Int = 0
    private var life: Int = 200

    //Set resources for player character and other game objects
    private val playerCharacter = context!!.resources.getDrawable(R.drawable.player,null)
    private var player = Player(450,700,0,0,playerCharacter)
    private val amoeba = context!!.resources.getDrawable(R.drawable.amoeba,null)
    private val nematode = context!!.resources.getDrawable(R.drawable.nematode,null)
    private val bacteria = context!!.resources.getDrawable(R.drawable.bacteria,null)
    private val algae = context!!.resources.getDrawable(R.drawable.algae,null)
    private val plantCell = context!!.resources.getDrawable(R.drawable.plantcell,null)

    private val audio: Audio

    //constructor
    init {
        paint.color = Color.WHITE

        gameObjects.add(player) //adds player to gameobjects list
        paint.textSize=50f
        start()     //starts game (in another thread from ui)
        myHolder = holder
        audio = Audio(context!!)  //passes the context of the fragment to the Audio class

    }

    /**
     * Function to add game objects to game object arrayList. Uses a random number generator to provide a statistical chance
     * each frame that an gameobject will be generated, allowing for control over numbers of each type of object to be generated.
     * When the lux level goes below 10, the enemy and collectable type generated change
     */
    fun addGameObjects(canvasWidth: Int, lux: Float){

        if(Random.nextInt(100) == 50 && lux >= 10){
            gameObjects.add(PlantCell(Random.nextInt(50..canvasWidth-100), -100, Random.nextInt(-3..3), Random.nextInt(2..5), plantCell))
        }
        if(Random.nextInt(125) == 50 && lux >= 10){
            gameObjects.add(Bacteria(Random.nextInt(50..canvasWidth-100), -100, Random.nextInt(-3..3), Random.nextInt(2..5), bacteria))
        }
        if(Random.nextInt(75) == 50  && lux < 10){ //only generated when it is dark
            gameObjects.add(Algae(Random.nextInt(50..canvasWidth-100), -100, Random.nextInt(-3..3), Random.nextInt(2..5), algae))
        }
        if(Random.nextInt(150) == 50 && lux >= 10){
            gameObjects.add(Amoeba(Random.nextInt(50..canvasWidth-100),-100,Random.nextInt(-4..4),Random.nextInt(2..5),amoeba))
        }
        if(Random.nextInt(90) == 50 && lux < 10){ //only generated when it is dark
            gameObjects.add(Nematode(Random.nextInt(50..canvasWidth-100), -100, Random.nextInt(3..3), Random.nextInt(2..5), nematode))
        }

    }


    /**
     * Function to move the player game object, takes parameters from the sensor class(accelerometer)
     * and updates the player objects movement variables
     */
    fun playerMove(sensorX: Float, sensorY: Float){
        player.dx = sensorX.toInt()
        player.dy = sensorY.toInt()

    }

    /**
     * Function to draw on the foreground of the surfaceView
     * Used to update the player score and remaining life on the canvas
     */
    override fun onDrawForeground(canvas: Canvas?) {

        //paint colour for text
        paint.color = Color.BLACK

        //lambda to create a percentage
        var percentage = {a:Double, b:Double -> (a/b*100.0).toInt()}

        super.onDrawForeground(canvas)
        canvas!!.drawText("Score:" ,50f, 40f, paint)
        canvas!!.drawText(score.toString(),50f, 90f, paint)
        canvas!!.drawText("Life:", 800f, 40f, paint)
        canvas!!.drawText(percentage(life.toDouble(), 200.0).toString() +" %",800f, 90f, paint)

        paint.color = Color.RED //Colour for end game text
        //Sets Game over text
        if(isGameOver) {
            paint.textSize = 100f
            canvas!!.drawText("Game Over", (canvas.width/2).toFloat() - 200f,
                (canvas.height/2).toFloat(), paint)
        }
        //Sets time up text
        if(isTimeUp){
            paint.textSize = 100f
            canvas!!.drawText("Time Up", (canvas.width/2).toFloat() - 150f, (canvas.height/2).toFloat(), paint)
        }
        //sets paint for canvas
        paint.color = Color.WHITE

    }

    /**
     * Runs in a separate thread of execution
     */
    override fun run() {
        val toRemove = ArrayList<GameObject>() //Variable to hold list of objects to remove each frame
        while(isRunning){
            if(!myHolder.surface.isValid)
                continue
            val canvas: Canvas = myHolder.lockCanvas()
            canvas.drawRect (0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), paint)
            changeCanvasColour(lux, canvas)
            addGameObjects(canvas.width, lux)  //adds game objects
            //Loops through game object arraylist
            for(gameObject in gameObjects) {
                playerMove(px,py)
                gameObject.move(canvas)
                //If game object has collided with player, or is out of the screen boundary they are added to list to be removed
                gameObjects.filter{collisionCheck(it)}.map{toRemove.add(it)}
                gameObjects.filter { isOutOfBounds(it, canvas.height) }.map{toRemove.add(it)}
            }
            gameObjects.removeAll(toRemove.toSet()) //removes all game objects in the toRemove list
            onDrawForeground(canvas)
            myHolder.unlockCanvasAndPost(canvas)
            checkGameOver()
        }
    }

    /**
     * Starts Game, sets isRunning boolean to true and starts thread
     */
    fun start(){
        isRunning = true
        myThread = Thread(this)
        myThread.start()
    }

    /**
     * Stops game and blocks actions until thread dies to prevent crashes
     */
    fun stop() {
        isRunning = false
        while (true){
            try {
                myThread.join()
                break
            } catch (e: InterruptedException){ //Blocks until the thread dies
                e.printStackTrace()
            }
            break
        }
    }

    /**
     * Function to test for player collisions with other gameobjects, adds points or damage to
     * player score appropriately
     * Returns a boolean which is used in the run method to remove gamebjects from the canvas
     */
    private fun collisionCheck (gameObject: GameObject): Boolean{

        if(gameObject != player) {
            if (player.hitBox.intersect(gameObject.hitBox)) {
                if(gameObject is Collectable){
                    score += gameObject.pointsValue

                    audio.playCrunchEffect()
                    return true
                }
                if(gameObject is Enemy){
                    life-= gameObject.damage

                    audio.playDamageEffect()
                    if(life <= 0) {
                        isGameOver = true   //sets game over boolean to tru to end the game
                    }
                    return true
                }
            }
        }
        return false
    }

    /**
     * Check if an object is out of the screen boundary then returns a boolean to indicate object to be removed
     */
    private fun isOutOfBounds(gameObject: GameObject, canvasHeight: Int):Boolean{
        if(gameObject.y > canvasHeight -90){
            return true
        }
        return false
    }

    /**
     * Checks if the game is over, then calls the stop function
     */
    private fun checkGameOver() {
        if(isGameOver || isTimeUp)
            stop()
    }

    /**
     * Changes the canvas colour according to the light level, to indicate to the player that
     * the light level is sufficiently low enough to enable the other set of collectable/enemies
     */
    private fun changeCanvasColour(lux: Float, canvas: Canvas){
        if(lux>=10){
            canvas.drawColor(Color.WHITE)
        } else{
            canvas.drawColor(Color.DKGRAY)
        }
    }

}