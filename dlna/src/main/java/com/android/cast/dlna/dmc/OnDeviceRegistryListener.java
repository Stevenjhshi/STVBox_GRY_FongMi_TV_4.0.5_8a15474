package com.android.cast.dlna.dmc;

import org.fourthline.cling.model.meta.Device;

/**
 * this listener call in UI thread.
 */
public interface OnDeviceRegistryListener {
    void onDeviceAdded(Device<?, ?, ?> device);

    default void onDeviceUpdated(Device<?, ?, ?> device) {
    }

    void onDeviceRemoved(Device<?, ?, ?> device);
}
