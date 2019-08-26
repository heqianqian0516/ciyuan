package com.ciyuanplus.mobile.utils.dialog;

import android.animation.TypeEvaluator;

class KickBackAnimator implements TypeEvaluator<Float> {
    private float mDuration = 0f;

    public void setDuration(float duration) {
        mDuration = duration;
    }

    public Float evaluate(float fraction, Float startValue, Float endValue) {
        float t = mDuration * fraction;
        float b = startValue;
        float c = endValue - startValue;
        float d = mDuration;
        float result = calculate(t, b, c, d);
        return result;
    }

    private Float calculate(float t, float b, float c, float d) {
        float s = 1.70158f;
        return c * ((t = t / d - 1) * t * ((s + 1) * t + s) + 1) + b;
    }
}
