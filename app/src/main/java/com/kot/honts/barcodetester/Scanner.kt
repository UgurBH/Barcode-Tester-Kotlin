package com.kot.honts.barcodetester

import android.content.Context
import android.util.Log
import com.honeywell.aidc.*
import com.honeywell.aidc.BarcodeReader.BarcodeListener

class Scanner(context: Context) : BarcodeListener, BarcodeReader.TriggerListener  {

    private val TAG = "Tester-Scanner"

    private var barcodeReader: BarcodeReader? = null
    private var manager: AidcManager? = null
    private var appcontext: Context? = null
    private var scanData: ScanData? = null

    var scannerCallBackInstance:ScannerCallBack? = null


    interface ScannerCallBack {
        fun barcodeDecodeSuccess(barcodeData:String, aimID:String)

    }

    init {
        this.appcontext = context
    }

    fun initScannerCallBack(scannerCallBack: ScannerCallBack){
        this.scannerCallBackInstance = scannerCallBack

    }

    /**
    Releasing scanner
     */
    public fun releaseScanner(){
        if(barcodeReader!= null) {
            barcodeReader!!.release();
            Log.d(TAG, "onPause barcodereader is released");
        }
    }

    /**
     * closing scanner
     */
    public fun closeScanner(){
        Log.d(TAG, "closeScanner: closing scanner")
        if(barcodeReader != null){
        barcodeReader!!.close()
        }
        if(manager != null) {
            manager!!.close()
        }
        Log.d(TAG, "closeScanner: closing scanner exit")
    }

    /**
     * Creating scanner object, and claims scan engine, then applies given configuration in the properties.
     */
    public fun createScanner() {

        Log.d(TAG, "createScanner: called")

        AidcManager.create(appcontext!!.applicationContext) { aidcManager ->
            var manager = aidcManager
            try {
                barcodeReader = manager.createBarcodeReader()
                Log.d(TAG, "onCreate barcode reader created")
            } catch (e: InvalidScannerNameException) {
                //Toast.makeText(this@MainActivity, "Invalid Scanner Name Exception: " + e.message, Toast.LENGTH_SHORT)
                    //.show()
            } catch (e: Exception) {
                //Toast.makeText(this@MainActivity, "Exception: " + e.message, Toast.LENGTH_SHORT).show()
            }
            if (barcodeReader != null) {
                Log.d(TAG, "barcodeReader is not null")
                try {
                    barcodeReader!!.claim()
                    Log.d(TAG, "barcodeReader is claimed in onCreate")
                } catch (e: ScannerUnavailableException) {
                    e.printStackTrace()
                }
                barcodeReader!!.addBarcodeListener(this@Scanner)
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
                    //Toast.makeText(this@MainActivity, "Failed to create the properties", Toast.LENGTH_SHORT).show()
                }
                barcodeReader!!.addTriggerListener(this@Scanner)
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


                barcodeReader!!.setProperties(properties)
            }
        }
    }

    override fun onBarcodeEvent(p0: BarcodeReadEvent?) {
        scannerCallBackInstance!!.barcodeDecodeSuccess(p0!!.barcodeData, p0!!.aimId)


        //TODO("Not yet implemented")
    }

    override fun onFailureEvent(p0: BarcodeFailureEvent?) {
        //TODO("Not yet implemented")
    }

    override fun onTriggerEvent(p0: TriggerStateChangeEvent?) {
        //TODO("Not yet implemented")
    }
}