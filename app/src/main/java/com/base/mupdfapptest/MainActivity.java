package com.base.mupdfapptest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.artifex.mupdf.viewer.DocumentActivity;

public class MainActivity extends AppCompatActivity {

    Button selectPdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        selectPdf = findViewById(R.id.selectPdf);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        selectPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check condition
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                    if (ActivityCompat.checkSelfPermission(
                            MainActivity.this,
                            android.Manifest.permission
                                    .READ_EXTERNAL_STORAGE)
                            != PackageManager
                            .PERMISSION_GRANTED) {
                        // When permission is not granted
                        // Result permission
                        ActivityCompat.requestPermissions(
                                MainActivity.this,
                                new String[]{
                                        android.Manifest.permission
                                                .READ_EXTERNAL_STORAGE},
                                1);
                    } else {
                        // When permission is granted
                        // Create method
                        selectPDF();
                    }
                }
                else {
                    // When permission is granted
                    // Create method
                    selectPDF();
                }
            }
        });

    }

    // Initialize result launcher
    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts
            .StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(
                ActivityResult result)
        {
            // Initialize result data
            Intent data = result.getData();
            // check condition
            if (data != null) {
                // When data is not equal to empty
                // Get PDf uri
                Uri sUri = data.getData();
                // set Uri on text view
//                tvUri.setText(Html.fromHtml(
//                        "<big><b>PDF Uri</b></big><br>"
//                                + sUri));
                Log.e("error in doc", sUri.toString());
                startMuPDFActivity(sUri);

                // Get PDF path
                String sPath = sUri.getPath();
                // Set path on text view
//                tvPath.setText(Html.fromHtml(
//                        "<big><b>PDF Path</b></big><br>"
//                                + sPath));
            }
        }
    });
    public void startMuPDFActivity(Uri documentUri) {
        Intent intent = new Intent(this, DocumentActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(documentUri);
        startActivity(intent);
    }

    private void selectPDF()
    {
        // Initialize intent
        Intent intent
                = new Intent(Intent.ACTION_GET_CONTENT);
        // set type
        intent.setType("application/pdf");
        // Launch intent
        resultLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);

        // check condition
        if (requestCode == 1 && grantResults.length > 0
                && grantResults[0]
                == PackageManager.PERMISSION_GRANTED) {
            // When permission is granted
            // Call method
            selectPDF();
        }
        else {
            // When permission is denied
            // Display toast
            Toast
                    .makeText(getApplicationContext(),
                            "Permission Denied",
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }
}