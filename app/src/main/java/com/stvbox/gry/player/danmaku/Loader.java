package com.stvbox.gry.player.danmaku;

import com.stvbox.gry.Constant;
import com.stvbox.gry.bean.Danmaku;
import com.stvbox.gry.utils.UrlUtil;
import com.github.catvod.net.OkHttp;

import java.io.IOException;
import java.io.InputStream;

import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.parser.android.AndroidFileSource;

public class Loader implements ILoader {

    private AndroidFileSource dataSource;

    public Loader(Danmaku item) {
        try {
            load(item.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load(String url) throws IllegalDataException {
        try {
            OkHttp.cancel("danmaku");
            if (url.startsWith("/")) url = "file:/" + url;
            load(OkHttp.newCall(OkHttp.client(Constant.TIMEOUT_DANMAKU), UrlUtil.convert(url), "danmaku").execute().body().byteStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load(InputStream stream) throws IllegalDataException {
        dataSource = new AndroidFileSource(stream);
    }

    @Override
    public AndroidFileSource getDataSource() {
        return dataSource;
    }
}