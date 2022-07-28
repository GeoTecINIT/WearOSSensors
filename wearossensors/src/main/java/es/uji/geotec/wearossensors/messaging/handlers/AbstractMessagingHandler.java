package es.uji.geotec.wearossensors.messaging.handlers;

import android.content.Context;

import com.google.android.gms.wearable.MessageEvent;

import java.util.ArrayList;

import es.uji.geotec.backgroundsensors.collection.CollectionConfiguration;
import es.uji.geotec.backgroundsensors.sensor.SensorManager;
import es.uji.geotec.backgroundsensors.service.manager.ServiceManager;
import es.uji.geotec.wearossensors.messaging.MessagingClient;
import es.uji.geotec.wearossensors.messaging.MessagingProtocol;
import es.uji.geotec.wearossensors.messaging.ResultMessagingProtocol;
import es.uji.geotec.wearossensors.notifications.NotificationProvider;
import es.uji.geotec.wearossensors.permissions.PermissionsManager;
import es.uji.geotec.wearossensors.records.callbacks.RecordCallbackProvider;
import es.uji.geotec.wearossensors.sensor.WearSensor;
import es.uji.geotec.wearossensors.services.WearSensorRecordingService;

public abstract class AbstractMessagingHandler {

    private Context context;
    private ServiceManager serviceManager;

    public AbstractMessagingHandler(Context context) {
        this.context = context;
        this.serviceManager = new ServiceManager(context, WearSensorRecordingService.class);
    }

    public void handleMessage(MessageEvent event) {
        String path = event.getPath();
        String sourceNodeId = event.getSourceNodeId();
        MessagingProtocol protocol = getProtocol();

        if (path.equals(protocol.getReadyProtocol().getMessagePath())) {
            handleIsReadyRequest(sourceNodeId);
        } else if (path.equals(protocol.getPrepareProtocol().getMessagePath())) {
            handlePrepareRequest(sourceNodeId);
        } else if (path.equals(protocol.getStartMessagePath())) {
            handleStartRequest(sourceNodeId, event.getData());
        } else if (path.equals(protocol.getStopMessagePath())) {
            handleStopRequest();
        }
    }

    private void handleIsReadyRequest(String sourceNodeId) {
        MessagingClient messageClient = new MessagingClient(context);
        ResultMessagingProtocol readyProtocol = getProtocol().getReadyProtocol();
        ArrayList<String> permissionsToBeRequested =
                PermissionsManager.permissionsToBeRequested(context, getRequiredPermissions());

        if (!isSensorSupported() || permissionsToBeRequested.size() != 0) {
            messageClient.sendFailureResponse(sourceNodeId, readyProtocol);
            return;
        }

        messageClient.sendSuccessfulResponse(sourceNodeId, readyProtocol);
    }

    private void handlePrepareRequest(String sourceNodeId) {
        MessagingClient messageClient = new MessagingClient(context);
        ResultMessagingProtocol prepareProtocol = getProtocol().getPrepareProtocol();

        if (!isSensorSupported()) {
            messageClient.sendFailureResponseWithReason(
                    sourceNodeId,
                    prepareProtocol,
                    "Sensor of type " + getWearSensorType() + " not supported");
            return;
        }

        ArrayList<String> permissionsToBeRequested =
                PermissionsManager.permissionsToBeRequested(context, getRequiredPermissions());

        if (permissionsToBeRequested.size() != 0) {
            NotificationProvider notificationProvider = new NotificationProvider(context);
            notificationProvider.createNotificationForPermissions(
                    permissionsToBeRequested,
                    PermissionsManager.getPermissionsActivity(context),
                    sourceNodeId,
                    prepareProtocol
            );
            return;
        }

        messageClient.sendSuccessfulResponse(sourceNodeId, prepareProtocol);
    }

    private void handleStartRequest(String sourceNodeId, byte[] configuration) {
        String[] configParams = new String(configuration).split("#");
        CollectionConfiguration wearConfig = new CollectionConfiguration(
                getWearSensorType(),
                Integer.parseInt(configParams[0]),
                Integer.parseInt(configParams[1])
        );

        serviceManager.startCollection(
                wearConfig,
                RecordCallbackProvider.getRecordCallbackFor(
                        getWearSensorType(),
                        context,
                        sourceNodeId,
                        getProtocol().getNewRecordMessagePath()
                )
        );
    }

    private void handleStopRequest() {
        serviceManager.stopCollection(getWearSensorType());
    }

    private boolean isSensorSupported() {
        SensorManager sensorManager = new SensorManager(context);
        WearSensor wearSensor = getWearSensorType();
        return sensorManager.isSensorAvailable(wearSensor);
    }

    protected abstract ArrayList<String> getRequiredPermissions();

    protected abstract MessagingProtocol getProtocol();

    protected abstract WearSensor getWearSensorType();
}
