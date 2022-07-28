package es.uji.geotec.wearossensors.records.callbacks;

import android.content.Context;

import java.nio.ByteBuffer;
import java.util.List;

import es.uji.geotec.backgroundsensors.record.TriAxialRecord;


public class TriAxialRecordCallback extends AbstractRecordCallback<TriAxialRecord> {

    public TriAxialRecordCallback(Context context, String sourceNodeId, String path) {
        super(context, sourceNodeId, path);
    }

    @Override
    protected byte[] encodeRecords(List<TriAxialRecord> records) {
        int size = records.size();
        byte[] bytes = new byte[Integer.BYTES + (Float.BYTES * 3 + Long.BYTES) * size];

        ByteBuffer buff = ByteBuffer.wrap(bytes);
        buff.putInt(size);
        for (TriAxialRecord record : records) {
            buff.putFloat(record.getX());
            buff.putFloat(record.getY());
            buff.putFloat(record.getZ());
            buff.putLong(record.getTimestamp());
        }

        return bytes;
    }
}
