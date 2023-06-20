package com.exless.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BahanViewModel : ViewModel() {
    val bahanArrayListView: MutableLiveData<ArrayList<Datarv_seeexperired>> = MutableLiveData(ArrayList())
}
