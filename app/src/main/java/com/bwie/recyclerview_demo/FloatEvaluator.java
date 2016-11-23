package com.bwie.recyclerview_demo;

import android.animation.TypeEvaluator;

/**
 * Created by w9072 on 2016/11/11.
 */

public class FloatEvaluator implements TypeEvaluator {
    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        float startFloat = ((Number) startValue).floatValue();
        return startFloat + fraction * (((Number) endValue).floatValue() - startFloat);
    }
}
