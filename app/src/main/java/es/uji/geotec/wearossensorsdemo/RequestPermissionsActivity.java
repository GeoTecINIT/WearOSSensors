package es.uji.geotec.wearossensorsdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import es.uji.geotec.wearossensors.permissions.handler.PermissionsRequestHandler;

public class RequestPermissionsActivity extends FragmentActivity {

    TextView descriptionText;
    ProgressBar progressBar;
    ImageView checkIcon, failIcon;

    private PermissionsRequestHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        descriptionText = findViewById(R.id.description);
        progressBar = findViewById(R.id.progressBar);
        checkIcon = findViewById(R.id.checkIcon);
        failIcon = findViewById(R.id.failIcon);

        handler = new PermissionsRequestHandler(this);
        handler.onPermissionsResult(this::updateUI);
        handler.handleRequest();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        handler.handleResult(requestCode, permissions, grantResults);
    }

    private void updateUI(boolean success) {
        progressBar.setVisibility(View.GONE);
        if (success) {
            descriptionText.setText("Thanks! :D");
            checkIcon.setVisibility(View.VISIBLE);
        } else {
            descriptionText.setText("Permissions denied :(");
            failIcon.setVisibility(View.VISIBLE);
        }
    }
}
