package es.uji.geotec.wearossensors.records.callbacks;

import android.content.Context;

import java.util.List;

import es.uji.geotec.backgroundsensors.record.Record;
import es.uji.geotec.backgroundsensors.record.callback.RecordCallback;
import es.uji.geotec.wearossensors.messaging.InternalMessagingClient;

public abstract class AbstractRecordCallback<T extends Record> implements RecordCallback<T> {

    private InternalMessagingClient internalMessagingClient;
    private String requesterId;
    private String sendingPath;

    public AbstractRecordCallback(Context context, String requesterId, String sendingPath) {
        this.internalMessagingClient = new InternalMessagingClient(context);
        this.requesterId = requesterId;
        this.sendingPath = sendingPath;
    }

    @Override
    public void onRecordsCollected(List<T> records) {
        byte[] recordsEncoded = encodeRecords(records);
        this.internalMessagingClient.sendNewRecord(requesterId, sendingPath, recordsEncoded);
    }

    protected abstract byte[] encodeRecords(List<T> records);
}
