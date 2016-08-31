package com.cleveroad.ptr;

import android.content.Context;
import android.graphics.Color;

class Utils {

    static int convertDpToPixel(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public static int generateRandomColor() {
        int R = (int) (Math.random() * 256);
        int G = (int) (Math.random() * 256);
        int B = (int) (Math.random() * 256);
        return Color.rgb(R, G, B);
    }
}
