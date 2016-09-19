package com.cleveroad.pulltorefresh.firework;

import android.graphics.Canvas;

interface FireworksDrawer {
    void draw(Canvas canvas, int width, int height);
    void reset();
}
