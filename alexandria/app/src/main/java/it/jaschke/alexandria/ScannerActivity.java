package it.jaschke.alexandria;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

//Activity from https://github.com/dm77/barcodescanner
public class ScannerActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }
    
    @Override
    public void handleResult(Result rawResult) {

        //if (rawResult.getBarcodeFormat()== BarcodeFormat.EAN_13 || rawResult.getBarcodeFormat()== BarcodeFormat.EAN_8){
            //Log.v(TAG, rawResult.getText()); // Prints scan results
            //Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
            String ean =rawResult.getText();
            //if(ean.length()==10 && !ean.startsWith("978")){
            //    ean="978"+ean;
            //}
            SharedPreferences.Editor editor=getSharedPreferences("it.jaschke.alexandria",MODE_PRIVATE).edit();
            editor.putString("SCANNER_EAN", ean);
            editor.commit();

            finish();


        //}
    }
}