package com.exless.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exless.`object`.Datarv_seeexperired

class BahanViewModel : ViewModel() {
    val bahanArrayListView: MutableLiveData<ArrayList<Datarv_seeexperired>> = MutableLiveData(ArrayList())
}
