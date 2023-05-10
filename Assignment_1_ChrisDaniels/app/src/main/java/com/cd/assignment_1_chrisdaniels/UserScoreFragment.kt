package com.cd.assignment_1_chrisdaniels

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.MediaStore
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.cd.assignment_1_chrisdaniels.databinding.FragmentInstructionBinding
import com.cd.assignment_1_chrisdaniels.databinding.FragmentUserScoreBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val REQUEST_CODE = 42


/**
 * A simple [Fragment] subclass.
 * Use the [UserScoreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserScoreFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    //binding to enable access to all the UI's views, nav controller enables navigation between fragments
    private lateinit var binding : FragmentUserScoreBinding
    private lateinit var navController : NavController
    private lateinit var picFile: File

    private lateinit var myViewModel: MyViewModel



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
        (activity as AppCompatActivity).supportActionBar?.title = "Your Details"
        binding = FragmentUserScoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myViewModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
        navController = findNavController()
        var myModel = myViewModel.myLiveModel.value
        //Sets default image resource to be displayed
        binding.userImage.setImageResource(R.drawable.player)

        if(myModel != null)
            binding.latestScore.text = myModel.currentScore.toString() //Display users score obtained

        //Navigate to high score fragment and adds fragment data to model
        binding.viewHighScoresBtn.setOnClickListener{
            if(binding.editTextPersonName.text.trim().isEmpty()){ //if name field is empty, create toast message
                customToast("Please enter a name!")
            } else {
                navController.navigate(R.id.action_userScoreFragment_to_scoresFragment)
                //sets the model with information from fragment
                if(myModel != null){
                    setModel(myModel)
                }
            }
        }

        //Listener to take photo
        binding.takePhotoBtn.setOnClickListener {
            val takePhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            picFile = createPicFile()//
            val fileProvider = FileProvider.getUriForFile(requireActivity(), "com.cd.assignment_1_chrisdaniels.fileprovider", picFile)
            takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            try {               //checks user has a camera application
                startActivityForResult(takePhoto, REQUEST_CODE)
            } catch (e: ActivityNotFoundException){
                Toast.makeText(this.context, "Camera Application Not Found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Creates a file directory for the image that is taken
     */
    private fun createPicFile(): File {
        val storageDirectory = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())  //creates unique filename based on time/date

        return File.createTempFile(timeStamp, ".jpg", storageDirectory)

    }

    /**
     * Function to set the model add the user details to the high score array, this function is wrapped in a != null check
     * in onViewCreated() method
     */
    private fun setModel(myModel: MyModel){
        var name = binding.editTextPersonName.text.toString().trim()
        var image: Drawable
        var score = myModel.currentScore

        //Checks if the filepath has been initialized, adds default image if not
        if(this::picFile.isInitialized){
            image = BitmapDrawable(resources, BitmapFactory.decodeFile(picFile.absolutePath))
        } else{
            image = BitmapDrawable(resources, BitmapFactory.decodeResource(resources, R.drawable.john_test))

        }

        //Adds and initialises a scoreitem and add it to the high score array
        var myScoreItem = ScoresItem(name, score, image)
        myModel.scoreList.add(myScoreItem)
    }


    /**
     * Receives the captured photo from the camera application
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val userPhoto = BitmapFactory.decodeFile(picFile.absolutePath)
            binding.userImage.setImageBitmap(userPhoto)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserScoreFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserScoreFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    /**
     * Function to construct a custom toast, customises text colour, background colour, and position
     */
    private fun customToast(msg:String){
        val myToast = Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT)
        myToast.setGravity(Gravity.CENTER,0,0)
        val view = myToast.view
        view!!.background.setTint(Color.DKGRAY)
        val toastText = view!!.findViewById(android.R.id.message) as TextView

        toastText.setTextColor(Color.RED)
        myToast.show()
    }
}