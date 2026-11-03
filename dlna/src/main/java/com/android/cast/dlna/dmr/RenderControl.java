package com.android.cast.dlna.dmr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Callbacks the DMR (renderer) side issues to the real player.
 * Implemented by CastActivity.
 */
public interface RenderControl {

    @NonNull
    RenderState getState();

    long getCurrentPosition();

    long getDuration();

    void seek(long time);

    void pause();

    void play(@Nullable Double speed);

    void stop();
}
