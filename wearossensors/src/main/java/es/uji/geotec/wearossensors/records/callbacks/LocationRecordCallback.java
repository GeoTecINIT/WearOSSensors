package es.uji.geotec.wearossensors.records.callbacks;

import android.content.Context;


import java.nio.ByteBuffer;
import java.util.List;

import es.uji.geotec.wearossensors.records.LocationRecord;

public class LocationRecordCallback extends AbstractRecordCallback<LocationRecord> {

    public LocationRecordCallback(Context context, String sourceNodeId, String path) {
        super(context, sourceNodeId, path);
    }

    @Override
    protected byte[] encodeRecords(List<LocationRecord> records) {
        int size = records.size();
        byte[] bytes = new byte[Integer.BYTES + (Double.BYTES * 3 + Float.BYTES * 4 + Long.BYTES) * size];

        ByteBuffer buff = ByteBuffer.wrap(bytes);
        buff.putInt(size);
        for (LocationRecord record : records) {
            buff.putDouble(record.getLatitude());
            buff.putDouble(record.getLongitude());
            buff.putDouble(record.getAltitude());
            buff.putFloat(record.getVerticalAccuracy());
            buff.putFloat(record.getHorizontalAccuracy());
            buff.putFloat(record.getSpeed());
            buff.putFloat(record.getDirection());
            buff.putLong(record.getTimestamp());
        }

        return bytes;
    }
}
