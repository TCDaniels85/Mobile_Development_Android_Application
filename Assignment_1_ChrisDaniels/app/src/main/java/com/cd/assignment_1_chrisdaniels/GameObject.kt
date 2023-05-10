package com.cd.assignment_1_chrisdaniels

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable

/**
 * Game object superclass for all objects on the canvas within the GameSurfaceView
 * Requires x and y co-ordinates, a drawable image, and dx and dy variables to apply  movement
 */
open class GameObject (var x:Int, var y: Int, var dx: Int, var dy: Int, var image: Drawable){
    //Height and width of GameObject
    var width: Int = 100
    var height: Int = 100
    var hitBox = Rect(x,y, x+width, y+height) //hitbox of type Rect to enable collision detection
    lateinit var name: String


    /**
     * Move class to provide movement to gameobjects, sets image bounds, draws image on canvas and
     * updates the hitbox co-ordinates to align with the image
     */
    open fun move (canvas: Canvas){
        updateHitBox()
        image.setBounds(x,y, x+width, y+height)
        image.draw(canvas)
    }

    /**
     * Updates co-ordinates of the hitBox
     */
    open fun updateHitBox(){
        hitBox.left = x
        hitBox.top = y
        hitBox.right = x+width
        hitBox.bottom = y+height
    }
}