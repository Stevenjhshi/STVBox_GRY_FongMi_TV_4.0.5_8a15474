package com.android.cast.dlna.dmc.control;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.cast.dlna.core.ICast;
import com.android.cast.dlna.core.Utils;
import com.android.cast.dlna.dmc.DLNACastManager;

import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.lastchange.EventedValue;
import org.fourthline.cling.support.model.MediaInfo;
import org.fourthline.cling.support.model.PositionInfo;
import org.fourthline.cling.support.model.TransportInfo;
import org.fourthline.cling.support.model.TransportState;

/**
 * Controls a connected remote renderer: issues AVTransport / RenderingControl actions
 * and forwards GENA subscription events to {@link OnDeviceControlListener}.
 * Action callbacks are delivered on the main thread.
 */
public class DeviceControl implements CastSubscriptionCallback.OnSubscriptionEventListener {

    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final ControlPoint mControlPoint;
    private final Device<?, ?, ?> mDevice;
    private final OnDeviceControlListener mListener;
    private final IServiceFactory mServiceFactory;
    private String mUri;

    public DeviceControl(@NonNull ControlPoint controlPoint, @NonNull Device<?, ?, ?> device, @Nullable OnDeviceControlListener listener) {
        mControlPoint = controlPoint;
        mDevice = device;
        mListener = listener;
        mServiceFactory = new IServiceFactory.ServiceFactoryImpl(controlPoint, device);
        subscribe(device.findService(DLNACastManager.SERVICE_AV_TRANSPORT));
        subscribe(device.findService(DLNACastManager.SERVICE_RENDERING_CONTROL));
    }

    private void subscribe(@Nullable Service<?, ?> service) {
        if (service != null) mControlPoint.execute(new CastSubscriptionCallback(service, 600, this));
    }

    @NonNull
    public Device<?, ?, ?> getDevice() {
        return mDevice;
    }

    @Nullable
    public OnDeviceControlListener getListener() {
        return mListener;
    }

    public boolean isCasting(@Nullable Device<?, ?, ?> device) {
        return device != null && mDevice.equals(device);
    }

    public boolean isCasting(@Nullable Device<?, ?, ?> device, @Nullable String uri) {
        return isCasting(device) && (uri == null || uri.equals(mUri));
    }

    // -----------------------------------------------------------------------------------------
    // ---- AVTransport actions
    // -----------------------------------------------------------------------------------------
    public <T> void setAVTransportURI(@NonNull String uri, @Nullable String title, @Nullable ServiceActionCallback<T> callback) {
        mUri = uri;
        ICast.ICastVideo castVideo = new ICast.ICastVideo() {
            @NonNull
            @Override
            public String getId() {
                return uri;
            }

            @NonNull
            @Override
            public String getUri() {
                return uri;
            }

            @Override
            public String getName() {
                return title == null ? uri : title;
            }

            @Override
            public long getDurationMillSeconds() {
                return 0;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public long getBitrate() {
                return 0;
            }
        };
        mServiceFactory.getAvService().cast(new ICastInterface.CastEventListener() {
            @Override
            public void onSuccess(String result) {
                notifySuccess(callback);
            }

            @Override
            public void onFailed(String errMsg) {
                notifyFailure(callback, errMsg);
            }
        }, uri, Utils.getMetadata(castVideo));
    }

    /**
     * @param speed play speed, only "1" is supported by most renderers, forwarded value is ignored here.
     */
    public <T> void play(@Nullable String speed, @Nullable ServiceActionCallback<T> callback) {
        mServiceFactory.getAvService().play(adapt(callback));
    }

    public <T> void pause(@Nullable ServiceActionCallback<T> callback) {
        mServiceFactory.getAvService().pause(adapt(callback));
    }

    public <T> void stop(@Nullable ServiceActionCallback<T> callback) {
        mServiceFactory.getAvService().stop(adapt(callback));
    }

    /**
     * @param position current watch time, ms
     */
    public <T> void seek(long position, @Nullable ServiceActionCallback<T> callback) {
        mServiceFactory.getAvService().seek(adapt(callback), position);
    }

    public void getPositionInfo(@Nullable ServiceActionCallback<PositionInfo> callback) {
        mServiceFactory.getAvService().getPositionInfo(adaptValue(callback));
    }

    public void getMediaInfo(@Nullable ServiceActionCallback<MediaInfo> callback) {
        mServiceFactory.getAvService().getMediaInfo(adaptValue(callback));
    }

    public void getTransportInfo(@Nullable ServiceActionCallback<TransportInfo> callback) {
        mServiceFactory.getAvService().getTransportInfo(adaptValue(callback));
    }

    // -----------------------------------------------------------------------------------------
    // ---- RenderingControl actions
    // -----------------------------------------------------------------------------------------
    public <T> void setVolume(int percent, @Nullable ServiceActionCallback<T> callback) {
        mServiceFactory.getRenderService().setVolume(adapt(callback), percent);
    }

    public <T> void setMute(boolean mute, @Nullable ServiceActionCallback<T> callback) {
        mServiceFactory.getRenderService().setMute(adapt(callback), mute);
    }

    // -----------------------------------------------------------------------------------------
    // ---- callback adapters
    // -----------------------------------------------------------------------------------------
    @Nullable
    private <T, R> IServiceAction.IServiceActionCallback<R> adapt(@Nullable ServiceActionCallback<T> callback) {
        if (callback == null) return null;
        return new IServiceAction.IServiceActionCallback<R>() {
            @Override
            public void onSuccess(R result) {
                callback.onSuccess(null);
            }

            @Override
            public void onFailed(String errMsg) {
                callback.onFailure(errMsg);
            }
        };
    }

    @Nullable
    private <R> IServiceAction.IServiceActionCallback<R> adaptValue(@Nullable ServiceActionCallback<R> callback) {
        if (callback == null) return null;
        return new IServiceAction.IServiceActionCallback<R>() {
            @Override
            public void onSuccess(R result) {
                callback.onSuccess(result);
            }

            @Override
            public void onFailed(String errMsg) {
                callback.onFailure(errMsg);
            }
        };
    }

    private <T> void notifySuccess(@Nullable ServiceActionCallback<T> callback) {
        if (callback != null) callback.onSuccess(null);
    }

    private <T> void notifyFailure(@Nullable ServiceActionCallback<T> callback, @Nullable String errMsg) {
        if (callback != null) callback.onFailure(errMsg == null ? "error" : errMsg);
    }

    // -----------------------------------------------------------------------------------------
    // ---- subscription events
    // -----------------------------------------------------------------------------------------
    @Override
    public void onAvTransportStateChanged(@NonNull TransportState state) {
        if (mListener != null) mHandler.post(() -> mListener.onAvTransportStateChanged(state));
    }

    @Override
    public void onEventChanged(@NonNull EventedValue<?> event) {
        if (mListener != null) mHandler.post(() -> mListener.onEventChanged(event));
    }

    @Override
    public void onRendererVolumeChanged(int volume) {
        if (mListener != null) mHandler.post(() -> mListener.onRendererVolumeChanged(volume));
    }

    @Override
    public void onRendererVolumeMuteChanged(boolean mute) {
        if (mListener != null) mHandler.post(() -> mListener.onRendererVolumeMuteChanged(mute));
    }
}
