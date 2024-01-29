package es.uji.geotec.wearossensorsdemo;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import es.uji.geotec.backgroundsensors.sensor.Sensor;
import es.uji.geotec.backgroundsensors.sensor.SensorManager;
import es.uji.geotec.wearossensors.plainmessage.PlainMessage;
import es.uji.geotec.wearossensors.plainmessage.PlainMessageClient;
import es.uji.geotec.wearossensors.permissions.PermissionsManager;
import es.uji.geotec.wearossensors.sensor.WearSensor;
import es.uji.geotec.wearossensorsdemo.command.CollectionCommand;
import es.uji.geotec.wearossensorsdemo.command.LocalCollectionCommand;
import es.uji.geotec.wearossensorsdemo.command.RemoteCollectionCommand;

public class MainActivity extends Activity {

    private LinearLayout linearLayout;
    private RadioGroup destinationRadio;
    private Button  startSingle, stopSingle;
    private Spinner sensorSpinner;
    private CollectionCommand command;
    private PlainMessageClient messageClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupLayout();
        setupButtons();
        setupSpinner();

        command = new LocalCollectionCommand(this);
        messageClient = new PlainMessageClient(this);
        messageClient.registerListener(message -> {
            Log.d("MainActivity", "received " + message);

            if (message.responseRequired()){
                Log.d("MainActivity", "response required! sending response...");
                PlainMessage response = new PlainMessage("PONG!", message.getPlainMessage());
                messageClient.send(response);
            }
        });

        PermissionsManager.setPermissionsActivity(this, RequestPermissionsActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionsManager.launchRequiredPermissionsRequest(this);
        }
    }

    public void onDestinationButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        if (!checked) return;

        switch(view.getId()) {
            case R.id.local_collection:
                this.command = new LocalCollectionCommand(this);
                break;
            case R.id.remote_collection:
                this.command = new RemoteCollectionCommand(this);
                break;
        }
    }

    public void onStartSingleCommandTap(View view) {
        WearSensor selectedSensor = (WearSensor) sensorSpinner.getSelectedItem();
        boolean requested = PermissionsManager.launchPermissionsRequestIfNeeded(this, selectedSensor.getRequiredPermissions());
        if (requested) return;

        toggleVisibility(stopSingle, startSingle);
        sensorSpinner.setEnabled(false);
        destinationRadio.setEnabled(false);

        command.executeStart(selectedSensor);
    }

    public void onStopSingleCommandTap(View view) {
        toggleVisibility(startSingle, stopSingle);
        sensorSpinner.setEnabled(true);
        destinationRadio.setEnabled(true);

        WearSensor selectedSensor = (WearSensor) sensorSpinner.getSelectedItem();
        command.executeStop(selectedSensor);
    }

    public void onSendFreeMessageTap(View view) {
        PlainMessage message = new PlainMessage("Hi! This is a test message");
        messageClient.send(message);
    }

    private void setupLayout() {
        linearLayout = findViewById(R.id.linear_layout);
        if (this.getResources().getConfiguration().isScreenRound()) {
            int padding = (int) (Resources.getSystem().getDisplayMetrics().widthPixels * 0.146467f);
            linearLayout.setPadding(padding, padding, padding, padding);
        }
    }

    private void setupButtons() {
        startSingle = findViewById(R.id.start_single_command);
        stopSingle = findViewById(R.id.stop_single_command);
        destinationRadio = findViewById(R.id.destination_collection);
    }

    private void setupSpinner() {
        sensorSpinner = findViewById(R.id.sensor_spinner);

        SensorManager sensorManager = new SensorManager(this);

        ArrayAdapter<Sensor> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (Sensor sensor : sensorManager.availableSensors(WearSensor.values())) {
            adapter.add(sensor);
        }

        sensorSpinner.setAdapter(adapter);
    }

    private void toggleVisibility(Button setVisible, Button setGone) {
        setVisible.setVisibility(View.VISIBLE);
        setGone.setVisibility(View.GONE);
    }
}