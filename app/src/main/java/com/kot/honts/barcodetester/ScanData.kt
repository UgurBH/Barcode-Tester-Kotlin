package com.kot.honts.barcodetester

import android.content.Context
import android.util.Log

data class ScanData(var barcodeData:String, var aimID:String) {

    private val TAG = "Tester-ScanData"


    init {

        Log.d(TAG, barcodeData)
        Log.d(TAG, aimID)
    }

    fun setbarcodeData(){

    }


}