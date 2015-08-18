package it.jaschke.alexandria;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

//Activity from https://github.com/dm77/barcodescanner
public class ScannerActivity extends Activity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZBarScannerView(this);    // Programmatically initialize the scanner view
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

        if (rawResult.getBarcodeFormat()== BarcodeFormat.ISBN10 || rawResult.getBarcodeFormat()== BarcodeFormat.ISBN10){
            Log.e("TAG", rawResult.getContents()); // Prints scan results
            Log.e("TAG", rawResult.getBarcodeFormat().getName()); // Prints the scan format (qrcode, pdf417 etc.)
            String ean =rawResult.getContents();
            if(ean.length()==10 && !ean.startsWith("978")){
                ean="978"+ean;
            }
            SharedPreferences.Editor editor=getSharedPreferences("it.jaschke.alexandria",MODE_PRIVATE).edit();
            editor.putString("SCANNER_EAN", ean);
            editor.commit();

            finish();
            //Intent bookIntent = new Intent(getActivity(), BookService.class);
            //bookIntent.putExtra(BookService.EAN, ean);
            //bookIntent.setAction(BookService.FETCH_BOOK);
            //getActivity().startService(bookIntent);

        }
    }
}