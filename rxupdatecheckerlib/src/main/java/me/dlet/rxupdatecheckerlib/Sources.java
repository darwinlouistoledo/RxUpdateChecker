package me.dlet.rxupdatecheckerlib;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by darwinlouistoledo on 6/27/16.
 */
class Sources {
    public static final int PLAYSTORE = 0;
    public static final int AMAZON = 1;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({PLAYSTORE, AMAZON})
    public @interface From {}
}
