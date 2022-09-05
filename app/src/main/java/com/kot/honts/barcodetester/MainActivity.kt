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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.honeywell.aidc.*
import com.honeywell.aidc.BarcodeReader.BarcodeListener


class MainActivity : AppCompatActivity(), BarcodeListener {

    private val TAG = "BarcodeTester-Kot"
    private var barcodeReader: BarcodeReader? = null
    private var manager: AidcManager? = null
    private var clearButton: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





        AidcManager.create(this) { aidcManager ->
            var manager = aidcManager
            try {
                barcodeReader = manager.createBarcodeReader()
                Log.d(TAG, "onCreate barcode reader created")
            } catch (e: InvalidScannerNameException) {
                Toast.makeText(this@MainActivity, "Invalid Scanner Name Exception: " + e.message, Toast.LENGTH_SHORT)
                    .show()
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Exception: " + e.message, Toast.LENGTH_SHORT).show()
            }
            if (barcodeReader != null) {
                Log.d(TAG, "barcodeReader is not null")
                try {
                    barcodeReader!!.claim()
                    Log.d(TAG, "barcodeReader is claimed in onCreate")
                } catch (e: ScannerUnavailableException) {
                    e.printStackTrace()
                }
                barcodeReader!!.addBarcodeListener(this@MainActivity)
                try {
                    //barcodeReader.setProperty(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,BarcodeReader.TRIGGER_CONTROL_MODE_AUTO_CONTROL);
                    //added continuous mode below
                    //barcodeReader.setProperty(BarcodeReader.PROPERTY_TRIGGER_SCAN_MODE, BarcodeReader.TRIGGER_SCAN_MODE_ONESHOT);
                    barcodeReader!!.setProperty(
                        BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
                        BarcodeReader.TRIGGER_CONTROL_MODE_AUTO_CONTROL
                    )
                    //barcodeReader.setProperty(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,BarcodeReader.TRIGGER_CONTROL_MODE_CLIENT_CONTROL);
                } catch (e: UnsupportedPropertyException) {
                    Log.e(TAG, "onCreated: exception occured cannot apply setProperty")
                    Toast.makeText(this@MainActivity, "Failed to create the properties", Toast.LENGTH_SHORT).show()
                }
                barcodeReader!!.addTriggerListener(this@MainActivity)
                val properties: MutableMap<String, Any> = HashMap()
                properties[BarcodeReader.PROPERTY_TRIGGER_AUTO_MODE_TIMEOUT] = 100
                properties[BarcodeReader.PROPERTY_CODE_128_ENABLED] = true
                properties[BarcodeReader.PROPERTY_GS1_128_ENABLED] = true
                properties[BarcodeReader.PROPERTY_QR_CODE_ENABLED] = true
                properties[BarcodeReader.PROPERTY_CODE_39_ENABLED] = true
                properties[BarcodeReader.PROPERTY_DATAMATRIX_ENABLED] = true
                properties[BarcodeReader.PROPERTY_UPC_A_ENABLE] = true
                properties[BarcodeReader.PROPERTY_EAN_13_ENABLED] = true
                properties[BarcodeReader.SHORT_MARGIN_ENABLED] = true
                //properties.put(BarcodeReader.SHORT_MARGIN_DISABLED, true);
                //properties.put(BarcodeReader.SHORT_MARGIN_ENABLE_BOTH_ENDS, true);
                properties[BarcodeReader.PROPERTY_AZTEC_ENABLED] = false
                properties[BarcodeReader.PROPERTY_CODABAR_ENABLED] = false
                properties[BarcodeReader.PROPERTY_INTERLEAVED_25_ENABLED] = true
                properties[BarcodeReader.PROPERTY_PDF_417_ENABLED] = true
                properties[BarcodeReader.PROPERTY_TRIGGER_SCAN_SAME_SYMBOL_TIMEOUT_ENABLED] = true

                // Set Max Code 39 barcode length
                properties[BarcodeReader.PROPERTY_CODE_39_MAXIMUM_LENGTH] = 10
                // Turn on center decoding
                properties[BarcodeReader.PROPERTY_CENTER_DECODE] = true

                // Enable bad read response
                //properties.put(BarcodeReader.PROPERTY_NOTIFICATION_BAD_READ_ENABLED, false);
                // Sets time period for decoder timeout in any mode
                properties[BarcodeReader.PROPERTY_TRIGGER_SCAN_SAME_SYMBOL_TIMEOUT] = 50
                // Apply the settings
                properties[BarcodeReader.PROPERTY_NOTIFICATION_GOOD_READ_ENABLED] = false

                //testing Wedge with Amar
                //                    try {
                //                        barcodeReader.setProperty("DPR_WEDGE", true);
                //                    } catch (UnsupportedPropertyException e) {
                //                        e.printStackTrace();
                //                    }
                barcodeReader!!.setProperties(properties)
            }
        }


    }

    override fun onStart() {
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
        if (barcodeReader != null) {
            Log.d(TAG, "onPause: releasing scanner")
            barcodeReader!!.release()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (barcodeReader != null) {
            barcodeReader!!.close()
            barcodeReader = null
        }

        if (manager != null) {
            manager!!.close()
        }

    }


    private fun BarcodeReader.addBarcodeListener(mainActivity: MainActivity) {


    }

    private fun BarcodeReader.addTriggerListener(mainActivity: MainActivity) {

    }

    override fun onBarcodeEvent(p0: BarcodeReadEvent?) {
        Log.d(TAG, "onBarcodeEvent: good barcode decode")
        barcodeReader!!.notify(BarcodeReader.GOOD_READ_NOTIFICATION)

        val scanTextView: TextView = findViewById(R.id.scanText)
        clearButton = findViewById(R.id.clearButton)
        clearButton!!.setOnClickListener(View.OnClickListener {
                scanTextView.setText("")
        })

        scanTextView.setText("Barcode data is ${p0?.barcodeData} \n aim id is ${p0?.aimId}")

    }

    override fun onFailureEvent(p0: BarcodeFailureEvent?) {
        Log.d(TAG, "onFailureEvent: bad barcode decode")
        barcodeReader!!.notify(BarcodeReader.BAD_READ_NOTIFICATION)

    }


}






