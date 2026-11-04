package com.stvbox.gry.impl;

import com.stvbox.gry.bean.Site;

public interface SiteCallback {

    void setSite(Site item);

    void onChanged();
}
