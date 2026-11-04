package com.android.cast.dlna.dmr.service;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.android.cast.dlna.dmr.CastAction;
import com.android.cast.dlna.dmr.DLNARendererService;

/**
 * Bridge between the DMR service layer and the real renderer player activity.
 * Named RendererInterfaceKt to stay binary compatible with the original Kotlin top level declarations.
 */
public final class RendererInterfaceKt {

    public static final String keyExtraCastAction = "com.android.cast.dlna.dmr.service.CastAction";

    private static final String CAST_ACTIVITY = "com.stvbox.gry.ui.activity.CastActivity";
    private static final long REDELIVER_DELAY_MS = 1500;

    private RendererInterfaceKt() {
    }

    /**
     * Deliver a new cast action to CastActivity (referenced by name, the library cannot depend on the app module).
     * CastActivity (singleTop) only consumes the action in onNewIntent, so when it is cold-started
     * by this intent the extra would be dropped: re-deliver the intent once the activity is up,
     * unless a real player got bound in the meantime (warm start path already consumed it).
     */
    public static void startCastActivity(Context context, CastAction action) {
        try {
            Intent intent = new Intent();
            intent.setClassName(context, CAST_ACTIVITY);
            intent.putExtra(keyExtraCastAction, action);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                DLNARendererService service = DLNARendererService.getInstance();
                if (service == null || !service.hasRenderControl()) {
                    try {
                        context.startActivity(intent);
                    } catch (Exception ignored) {
                    }
                }
            }, REDELIVER_DELAY_MS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
