package com.cd.assignment_1_chrisdaniels

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cd.assignment_1_chrisdaniels.databinding.FragmentScoresBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ScoresFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScoresFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    //Declare binding to this fragments user interface and declare references to MyViewModel and ScoresAdapter
    lateinit var binding: FragmentScoresBinding
    lateinit var myViewModel: MyViewModel
    lateinit var scoresAdapter: ScoresAdapter
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
        (activity as AppCompatActivity).supportActionBar?.title = "High Scores"
        binding = FragmentScoresBinding.inflate(inflater, container, false)  //inflate view to give access to recycler view through the layout manager
        return binding.root
    }

    /**
     * Provides access to the view model and set variable to represent the model
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        myViewModel =  ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
        var myModel = myViewModel.myLiveModel.value

        binding.scoresRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL,false)
        //Connect scores list to the adapter and the adapter to recycler view after check that myModel is not null
        if(myModel!=null){
            val score = myModel.scoreList
            scoresAdapter = ScoresAdapter(score)
            binding.scoresRecyclerView.adapter=scoresAdapter
            //scoresAdapter loops through items in scorelist and adds each one to the recyclerView
        }
        //onclicklistener for button to return to instruction page
        binding.returnToStartBtn.setOnClickListener{
            navController.navigate(R.id.action_scoresFragment_to_instructionFragment)

        }

        val previousFrag = navController.previousBackStackEntry?.destination?.id
        if (previousFrag != null) {
            if (previousFrag.equals(R.id.instructionFragment))
                binding.returnToStartBtn.visibility = View.INVISIBLE //sets button invisible from this fragment

        }
        //Customise back button
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            //Disables back button if fragment accessed from userscore fragment (to prevent multiple scores being added
            if (previousFrag != null) {
                if(previousFrag.equals(R.id.userScoreFragment) || previousFrag.equals(R.id.instructionFragment)){
                    navController.navigate(R.id.action_scoresFragment_to_instructionFragment)
                }
            }
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ScoresFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScoresFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}