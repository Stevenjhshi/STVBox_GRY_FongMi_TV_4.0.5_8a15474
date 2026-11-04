package com.stvbox.gry.ui.base;

import android.view.View;

import androidx.leanback.widget.Presenter;

import com.stvbox.gry.bean.Vod;

public abstract class BaseVodHolder extends Presenter.ViewHolder {

    public BaseVodHolder(View view) {
        super(view);
    }

    public abstract void initView(Vod item);
}
