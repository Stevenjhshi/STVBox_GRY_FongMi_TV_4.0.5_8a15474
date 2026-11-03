package com.android.cast.dlna.dmr;

import android.os.Binder;

public class RendererServiceBinder extends Binder {

    private final DLNARendererService mService;

    public RendererServiceBinder(DLNARendererService service) {
        mService = service;
    }

    public DLNARendererService getService() {
        return mService;
    }
}
