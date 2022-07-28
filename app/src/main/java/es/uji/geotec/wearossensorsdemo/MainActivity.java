package es.uji.geotec.wearossensorsdemo;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import es.uji.geotec.backgroundsensors.sensor.Sensor;
import es.uji.geotec.backgroundsensors.sensor.SensorManager;
import es.uji.geotec.wearossensors.command.CommandClient;
import es.uji.geotec.wearossensors.freemessage.FreeMessage;
import es.uji.geotec.wearossensors.freemessage.FreeMessageClient;
import es.uji.geotec.wearossensors.permissions.PermissionsManager;
import es.uji.geotec.wearossensors.sensor.WearSensor;

public class MainActivity extends Activity {

    private LinearLayout linearLayout;
    private Button startAll, stopAll, startSingle, stopSingle;
    private Spinner sensorSpinner;

    private CommandClient commandClient;
    private FreeMessageClient messageClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupLayout();
        setupButtons();
        setupSpinner();

        commandClient = new CommandClient(this);
        messageClient = new FreeMessageClient(this);
        messageClient.registerListener(message -> {
            Log.d("MainActivity", "received " + message);

            if (message.responseRequired()){
                Log.d("MainActivity", "response required! sending response...");
                FreeMessage response = new FreeMessage("PONG!", message.getFreeMessage());
                messageClient.send(response);
            }
        });

        PermissionsManager.setPermissionsActivity(this, RequestPermissionsActivity.class);
    }

    public void onStartAllCommandTap(View view) {
        toggleVisibility(stopAll, startAll);
        commandClient.sendCommand("start-all");
    }

    public void onStopAllCommandTap(View view) {
        toggleVisibility(startAll, stopAll);
        commandClient.sendCommand("stop-all");
    }

    public void onStartSingleCommandTap(View view) {
        toggleVisibility(stopSingle, startSingle);
        String selectedSensor = (String) sensorSpinner.getSelectedItem();
        sensorSpinner.setEnabled(false);

        commandClient.sendCommand("start-" + selectedSensor.toLowerCase());
    }

    public void onStopSingleCommandTap(View view) {
        toggleVisibility(startSingle, stopSingle);
        String selectedSensor = (String) sensorSpinner.getSelectedItem();
        sensorSpinner.setEnabled(true);

        commandClient.sendCommand("stop-" + selectedSensor.toLowerCase());
    }

    public void onSendFreeMessageTap(View view) {
        FreeMessage message = new FreeMessage("Hi! This is a test message");
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
        startAll = findViewById(R.id.start_all_command);
        stopAll = findViewById(R.id.stop_all_command);
        startSingle = findViewById(R.id.start_single_command);
        stopSingle = findViewById(R.id.stop_single_command);
    }

    private void setupSpinner() {
        sensorSpinner = findViewById(R.id.sensor_spinner);

        SensorManager sensorManager = new SensorManager(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (Sensor sensor : sensorManager.availableSensors(WearSensor.values())) {
            adapter.add(sensor.toString());
        }

        sensorSpinner.setAdapter(adapter);
    }

    private void toggleVisibility(Button setVisible, Button setGone) {
        setVisible.setVisibility(View.VISIBLE);
        setGone.setVisibility(View.GONE);
    }
}