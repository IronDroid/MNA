package com.gomedia.mna;


import com.gomedia.mna.tools.FileIO;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Size;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import net.sourceforge.zbar.Config;

public class TaskQRCode_Activity extends Activity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;
    ImageScanner scanner;
    private boolean barcodeScanned = false;
    private boolean previewing = true;

    static {
        System.loadLibrary("iconv");
    }
    GlobalDataApplication global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_qrcode);

        global = (GlobalDataApplication) getApplication();

        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        /* Instance barcode scanner */
        scanner = new ImageScanner();

        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB, this);
        FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
        preview.addView(mPreview);

    }

    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
        }
        return camera;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }
    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing) {
                mCamera.autoFocus(autoFocusCB);
            }
        }
    };
    PreviewCallback previewCb = new PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {

            Camera.Parameters parameters = camera.getParameters();
            Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {
                previewing = false;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();

                SymbolSet syms = scanner.getResults();
                for (Symbol sym : syms) {
                    // scanText.setText("barcode result " + sym.getData());
                    // Log.e("fer", sym.getData());
                    // i.putExtra("SCAN_RESULT", sym.getData());
                    // setResult(RESULT_OK, i);
                    global.codeObra = sym.getData();
                    // Log.e("fer", global.codeObra);

                    boolean bool = FileIO.validateConnetDirfromSD(
                            "/.mna/",
                            new String[]{".jpg", "_" + global.idLang + ".txt"},
                            global.codeObra);
                    if (bool) {
                        startActivity(new Intent(TaskQRCode_Activity.this, PropertiesMNA_Activity.class).putExtra("tipo", 2));
                        finish();
                    } else {
                        // Toast.makeText(TaskQRCode_Activity.this,
                        // getResources().getString(R.string.txt_erro_code),
                        // Toast.LENGTH_LONG).show();
                        onCreateDialog(1).show();
                    }
                    barcodeScanned = true;
                }
            }
        }
    };

    protected android.app.Dialog onCreateDialog(int id) {
        LayoutInflater inflater = this.getLayoutInflater();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // alertDialogBuilder.setTitle("This is title");
        // alertDialogBuilder.setIcon(R.drawable.ic_delete);
        // set dialog message

        alertDialogBuilder
                // .setMessage(getResources().getString(R.string.txt_erro_code))
                .setCancelable(false)
                .setView(inflater.inflate(R.layout.layout_dialog_qr, null))
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(new Intent(TaskQRCode_Activity.this, TaskQRCode_Activity.class));
                finish();
            }
        })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // if this button is clicked, just close
                // the dialog box and do nothing
                dialog.cancel();
                finish();
            }
        });
        return alertDialogBuilder.create();
    }
    ;

	// Mimic continuous auto-focusing
	AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };
}
