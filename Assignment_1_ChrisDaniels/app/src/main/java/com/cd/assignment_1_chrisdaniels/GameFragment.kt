package com.cd.assignment_1_chrisdaniels

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.cd.assignment_1_chrisdaniels.databinding.FragmentGameBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameFragment : Fragment(), SensorEventListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    //binding to enable access to all the UI's views, nav controller enables navigation between fragments
    private lateinit var binding: FragmentGameBinding
    private lateinit var navController : NavController
    //variables for phone sensors
    lateinit var sensorManager: SensorManager
    lateinit var accelerometer: Sensor
    lateinit var gyro: Sensor
    lateinit var lightSense: Sensor
    //enables use of view model to share data between fragments
    lateinit var myViewModel: MyViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        myViewModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
        var myModel = myViewModel.myLiveModel.value

        val secondsText = {a:Long,b:Long ->(a/b).toString()} //Lambda expression to convert (long) time to seconds, then to a string to be displayed
        //Set timer for game length, timer logic contained in separate function
        val timer = createTimer(60000, 1000, myModel, secondsText)
        timer.start()

        //Set up sensor manager and individual sensors
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, accelerometer,SensorManager.SENSOR_STATUS_ACCURACY_LOW)
        lightSense = sensorManager.getDefaultSensor((Sensor.TYPE_LIGHT))
        sensorManager.registerListener(this, lightSense, SensorManager.SENSOR_DELAY_NORMAL)

        //TO REMOVE
//        binding.testButton.setOnClickListener{
//
//            if(myModel != null){
//                myModel.currentScore = binding.gameSurfaceView.score
//                binding.gameSurfaceView.stop()
//                timer.cancel()
//                navController.navigate(R.id.action_gameFragment_to_userScoreFragment)
//            }
//        }
        //overrides back button to ensure the thread has stopped to prevent crashing if the button is pressed mid way through the run function
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            binding.gameSurfaceView.stop() //stops game
            timer.cancel()
            navController.navigate(R.id.action_gameFragment_to_instructionFragment) //navigates back to instruction fragment

        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GameFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GameFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    /**
     * Function to create a timer, takes maximum time, time interval, and MyModel as parameters
     * Sets textview to represent a counter counting down, checks each second that the game is running, if it ends
     * the function navigates to the next screen. If the timer ends, navigates to next screen.
     * Returns the timer
     */
    fun createTimer(
        totalTime: Long,
        interval: Long,
        myModel: MyModel?,
        secondsText: (Long, Long) -> String
    ) : CountDownTimer {

        //Instantiate timer object
        val timer =  object : CountDownTimer(totalTime, interval) {
            override fun onTick(p0: Long) {

                binding.timerText.text = secondsText(p0,interval) //sets text view to number of seconds
                //checks the game if the game has ended
                if(!binding.gameSurfaceView.isRunning) {
                    if (myModel != null) {
                        myModel.currentScore = binding.gameSurfaceView.score  //sets the current score in MyModel
                    }
                    Thread.sleep(2000) //two second delay
                    navController.navigate(R.id.action_gameFragment_to_userScoreFragment)
                    this.cancel()
                }

            }

            /**
             * function to handle the end of the timer, ends game and navigates to the the next fragment
             */
            override fun onFinish() {
                if (myModel != null) {
                    myModel.currentScore = binding.gameSurfaceView.score //sets the current score in MyModel
                }
                binding.gameSurfaceView.isTimeUp = true
                Thread.sleep(2000)  //two second delay
                navController.navigate(R.id.action_gameFragment_to_userScoreFragment)
            }
        }
        return timer
    }

    /**
     * Function to obtain data from the sensors and pass it to the Surface View
     */
    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null)
            return

        //Accelerometer
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER ){
            var x=event.values[0]
            var y=event.values[1]

            binding.gameSurfaceView.px= x*-3f
            binding.gameSurfaceView.py =y*3f
        }

        if(event.sensor.type == Sensor.TYPE_LIGHT){
            var lux = event.values[0]
            if(lux < 10000f){
                binding.gameSurfaceView.lux = lux
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

}