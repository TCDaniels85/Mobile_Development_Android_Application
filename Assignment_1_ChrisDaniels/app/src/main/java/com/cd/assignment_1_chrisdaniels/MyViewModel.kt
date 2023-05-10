package com.cd.assignment_1_chrisdaniels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel that can be subclassed to to manage interactions between the Model and fragments.
 * MutableLiveData class ensures that the model is observable and allows fragments to be notified of changes in data
 */
class MyViewModel: ViewModel() {
    val myLiveModel = MutableLiveData<MyModel>()

    init{
        myLiveModel.value = MyModel()
    }


}