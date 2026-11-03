package com.android.cast.dlna.dmc.control;

import androidx.annotation.NonNull;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.support.lastchange.EventedValue;
import org.fourthline.cling.support.model.TransportState;

/**
 * Events of a device connected via {@link com.android.cast.dlna.dmc.DLNACastManager#connectDevice}.
 * All callbacks are delivered on the main thread.
 */
public interface OnDeviceControlListener {

    void onConnected(@NonNull Device<?, ?, ?> device);

    void onDisconnected(@NonNull Device<?, ?, ?> device);

    void onAvTransportStateChanged(@NonNull TransportState state);

    void onEventChanged(@NonNull EventedValue<?> event);

    void onRendererVolumeChanged(int volume);

    void onRendererVolumeMuteChanged(boolean mute);
}
