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
import android.widget.Spinner;

import es.uji.geotec.backgroundsensors.collection.CollectionConfiguration;
import es.uji.geotec.backgroundsensors.sensor.Sensor;
import es.uji.geotec.backgroundsensors.sensor.SensorManager;
import es.uji.geotec.wearossensors.command.CommandClient;
import es.uji.geotec.wearossensors.plainmessage.PlainMessage;
import es.uji.geotec.wearossensors.plainmessage.PlainMessageClient;
import es.uji.geotec.wearossensors.permissions.PermissionsManager;
import es.uji.geotec.wearossensors.sensor.WearSensor;

public class MainActivity extends Activity {

    private LinearLayout linearLayout;
    private Button  startSingle, stopSingle;
    private Spinner sensorSpinner;

    private CommandClient commandClient;
    private PlainMessageClient messageClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupLayout();
        setupButtons();
        setupSpinner();

        commandClient = new CommandClient(this);
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

    public void onStartSingleCommandTap(View view) {
        WearSensor selectedSensor = (WearSensor) sensorSpinner.getSelectedItem();
        boolean requested = PermissionsManager.launchPermissionsRequestIfNeeded(this, selectedSensor.getRequiredPermissions());
        if (requested) return;

        toggleVisibility(stopSingle, startSingle);
        sensorSpinner.setEnabled(false);

        CollectionConfiguration config = new CollectionConfiguration(
                selectedSensor,
                android.hardware.SensorManager.SENSOR_DELAY_GAME,
                selectedSensor == WearSensor.HEART_RATE || selectedSensor == WearSensor.LOCATION ? 1 : 50
        );
        commandClient.sendStartCommand(config);
    }

    public void onStopSingleCommandTap(View view) {
        toggleVisibility(startSingle, stopSingle);
        Sensor selectedSensor = (Sensor) sensorSpinner.getSelectedItem();
        sensorSpinner.setEnabled(true);

        commandClient.sendStopCommand(selectedSensor);
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