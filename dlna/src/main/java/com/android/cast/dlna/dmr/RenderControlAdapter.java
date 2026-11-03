package com.android.cast.dlna.dmr;

import androidx.annotation.NonNull;

/**
 * Adapts the app side {@link RenderControl} to the library internal {@link IDLNARenderControl}.
 */
public class RenderControlAdapter implements IDLNARenderControl {

    private final RenderControl mControl;

    public RenderControlAdapter(@NonNull RenderControl control) {
        mControl = control;
    }

    @NonNull
    public RenderControl getRenderControl() {
        return mControl;
    }

    @Override
    public void play() {
        mControl.play(null);
    }

    @Override
    public void pause() {
        mControl.pause();
    }

    @Override
    public void seek(long position) {
        mControl.seek(position);
    }

    @Override
    public void stop() {
        mControl.stop();
    }

    @Override
    public long getPosition() {
        return mControl.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return mControl.getDuration();
    }
}
