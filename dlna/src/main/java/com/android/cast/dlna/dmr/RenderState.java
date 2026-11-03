package com.android.cast.dlna.dmr;

/**
 * Renderer playback state reported by the real player (CastActivity) back to the DMR service.
 */
public enum RenderState {
    IDLE,
    PREPARING,
    PLAYING,
    PAUSED,
    STOPPED
}
