package es.uji.geotec.wearossensorsdemo;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import es.uji.geotec.wearossensors.intent.IntentManager;
import es.uji.geotec.wearossensors.permissions.PermissionsManager;
import es.uji.geotec.wearossensors.permissions.PermissionsResultClient;

public class RequestPermissionsActivity extends FragmentActivity {

    TextView descriptionText;
    ProgressBar progressBar;
    ImageView checkIcon, failIcon;

    ArrayList<String> permissions;
    ArrayList<String> specialPermissions;
    boolean specialPermissionsRequested = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        descriptionText = findViewById(R.id.description);
        progressBar = findViewById(R.id.progressBar);
        checkIcon = findViewById(R.id.checkIcon);
        failIcon = findViewById(R.id.failIcon);

        Timer delayer = new Timer();
        delayer.schedule(new TimerTask() {
            @Override
            public void run() {
                permissions = PermissionsManager.permissionsToRequestFromIntent(getIntent());
                specialPermissions = PermissionsManager.specialPermissionsToRequestFromIntent(getIntent());

                if (permissions.size() != 0) {
                    requestPermissions(permissions);
                } else if (specialPermissions.size() != 0) {
                    specialPermissionsRequested = true;
                    requestPermissions(specialPermissions);
                } else {
                    finishDeferred();
                }
            }
        }, 500);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != PermissionsManager.PERMISSIONS_RC) {
            return;
        }

        ArrayList<String> permissionsRejected = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                permissionsRejected.add(permissions[i]);
            }
        }

        if (permissionsRejected.size() != 0) {
            requestFailed(permissionsRejected);
            return;
        }

        if (!specialPermissionsRequested && specialPermissions.size() != 0) {
            Timer delayer = new Timer();
            delayer.schedule(new TimerTask() {
                @Override
                public void run() {
                    requestPermissions(specialPermissions);
                    specialPermissionsRequested = true;
                }
            }, 1000);
            return;
        }

        requestSucceeded();
    }

    private void requestPermissions(ArrayList<String> permissions) {
        PermissionsManager.requestPermissions(this, permissions);
    }

    private void requestSucceeded() {
        PermissionsResultClient permissionsResultClient = new PermissionsResultClient(this);
        boolean remoteRequest = IntentManager.isRemoteRequest(getIntent());
        if (remoteRequest) {
            permissionsResultClient.sendPermissionsSuccessfulResponse(getIntent());
        }

        updateUI(true);
    }

    private void requestFailed(ArrayList<String> permissionsRejected) {
        PermissionsResultClient permissionsResultClient = new PermissionsResultClient(this);
        boolean remoteRequest = IntentManager.isRemoteRequest(getIntent());
        if (remoteRequest) {
            String failureMessage = buildFailureMessage(permissionsRejected);
            permissionsResultClient.sendPermissionsFailureResponse(getIntent(), failureMessage);
        }

        updateUI(false);
    }

    private String buildFailureMessage(ArrayList<String> failures) {
        StringBuilder sb = new StringBuilder();
        sb.append("Permissions rejected: ");
        String separator = " ";
        for (String failure : failures) {
            sb.append(separator);
            separator = ", ";
            sb.append(failure);
        }

        return sb.toString();
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
        finishDeferred();
    }

    private void finishDeferred() {
        Timer delayer = new Timer();
        delayer.schedule(new TimerTask() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);
    }
}
