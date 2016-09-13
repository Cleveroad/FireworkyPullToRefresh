package com.cleveroad.pulltorefresh.firework;

class BezierCurveHelper {
    /**
     * @param startPoint  = P0
     * @param endPoint    = P2
     * @param targetPoint = P1
     * @param t           = 0..1
     * @return Bézier curve point
     * @see "https://en.wikipedia.org/wiki/B%C3%A9zier_curve"
     */
    static float quadTo(float startPoint, float endPoint, float targetPoint, float t) {
        return (float) (Math.pow(1.f - t, 2.f) * startPoint +
                        2.f * t * (1.f - t) * targetPoint +
                        Math.pow(t, 2.f) * endPoint
        );
    }

    static float getQuadTargetPoint(float startPoint, float endPoint, float bezierPoint, float t) {
        return (float) (bezierPoint - (Math.pow(1.f - t, 2.f) * startPoint + Math.pow(t, 2.f) * endPoint)) / (2.f * t * (1.f - t));
    }
}
