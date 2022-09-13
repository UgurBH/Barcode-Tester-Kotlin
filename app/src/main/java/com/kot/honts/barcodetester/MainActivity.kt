package com.kot.honts.barcodetester

/**
 * onCreate method claims the scanner and apply the decode profiles.
 * onPause and onRelease methods releases the scanner, so during your tests make sure to destroy the app and launch again
 */

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(), Scanner.ScannerCallBack {


    private val TAG = "Tester-Kot"
    private var clearButton: Button? = null
    private var scanner: Scanner? = Scanner(this)
    private var textView: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scanner!!.initScannerCallBack(this)

        clearButton = findViewById(R.id.clearButton)
        clearButton!!.setOnClickListener {
            if (textView != null) {
                textView!!.setText("")
            }
        }

    }


    override fun onStart() {
        super.onStart()
        scanner!!.createScanner()

    }

    override fun onPause() {
        super.onPause()
        scanner!!.releaseScanner()


    }

    override fun onDestroy() {
        super.onDestroy()
        scanner!!.closeScanner()


    }

    override fun barcodeDecodeSuccess(barcodeData: String, aimID: String) {
        textView = findViewById(R.id.scanOutput)
        textView!!.setText("Decoded barcode is $barcodeData \n aimID is $aimID ")

    }


}






