package com.cd.assignment_1_chrisdaniels

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.cd.assignment_1_chrisdaniels.databinding.FragmentSplashBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SplashFragment.newInstance] factory method to
 * create an instance of this fragment.
 * Splash screen for the game
 */
class SplashFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    //binding to enable access to all the UI's views, nav controller enables navigation between fragments
    private lateinit var binding :FragmentSplashBinding
    private lateinit var navController : NavController

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
        //Reference to access all the views relating to fragment
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Initializes when the view is created, contains listeners for buttons and a creates a timer for the splash screen
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        val timer = createTimer(3000, 1000)
        timer.start()
    }

    /**
     * Create a timer, this is used in the for the splash screen to automatically start the application after
     * a designated time, in this case 3 seconds (set in the onViewCreated function)
     */
    fun createTimer(totalTime : Long, interval: Long ) : CountDownTimer{

        val timer =  object : CountDownTimer(totalTime, interval) {
            override fun onTick(p0: Long) { //do nothing
            }
            override fun onFinish() {
                this.cancel()  //cancel timer
                navController.navigate(R.id.action_splashFragment_to_instructionFragment2)
            }
        }
        return timer
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SplashFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SplashFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}