package org.etaskerapp.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.github.gcacace.signaturepad.views.SignaturePad;

import org.etaskerapp.R;
import org.etaskerapp.constant.Constant;
import org.etaskerapp.model.Task;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignatureActivity extends AppCompatActivity {

    private Task task;
    private File photoFile;
    private Bitmap b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signature_activity);

        Intent intent = getIntent();
        task = (Task)intent.getSerializableExtra(TaskListActivity.TASK);

        final SignaturePad mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
            }

            @Override
            public void onSigned() {
                b = mSignaturePad.getSignatureBitmap();
            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
            }
        });

        findViewById(R.id.buttonSaveSignature).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        String mCurrentPhotoPath = image.getAbsolutePath();
        return image;

    }

    private void makeToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    private void upload() {
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            makeToast(ex.getMessage());
            return;
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {

            OutputStream os;
            try {
                os = new FileOutputStream(photoFile);
                b.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.flush();
                os.close();
            } catch (Exception e) {
                makeToast(e.getMessage());
            }
            AndroidNetworking.upload(Constant.URL_SIGNATURES + "/"+ task.getId())
                    .setOkHttpClient(LoginActivity.OK_HTTP_CLIENT)
                    .addMultipartFile("file", photoFile)
                    .setPriority(Priority.HIGH)
                    .build()
                    .setUploadProgressListener(new UploadProgressListener() {
                        @Override
                        public void onProgress(long bytesUploaded, long totalBytes) {
                            // do anything with progress
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            photoFile.delete();
                        }
                        @Override
                        public void onError(ANError error) {
                            makeToast(error.getCause().toString());
                        }
                    });
                finish();
        }
    }
}
