package me.dlet.rxupdatecheckerlib;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by darwinlouistoledo on 11/9/16.
 */
class Utils {
    /**
     * Since the library check from the Desktop Web Page of the app the "Current Version" field, if there are different apks for the app,
     * the Play Store will shown "Varies depending on the device", so the library can't compare it to versionName installed.
     */
    public static final boolean containsNumber(String string) {
        return string.matches(".*[0-9].*");
    }

    public static void saveAppDetails(Context context, AppDetails details){
        SharedPreferences preferences = context.getSharedPreferences(Constants.PREFS_RX_UPDATE_CHECKER+context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.PREFS_KEY_SOFTWARE_VERSION, details.getSoftwareVersion());
        editor.putString(Constants.PREFS_KEY_OPERATING_SYSTEM, details.getOperatingSystems());
        editor.putString(Constants.PREFS_KEY_DATE_PUBLISHED, details.getDatePublished());
        editor.putString(Constants.PREFS_KEY_NUM_DOWNLOADS, details.getNumDownloads());
        editor.commit();
    }

    public static AppDetails getSavedAppDetails(Context context){
        SharedPreferences preferences = context.getSharedPreferences(Constants.PREFS_RX_UPDATE_CHECKER+context.getPackageName(), Context.MODE_PRIVATE);
        AppDetails appDetails = new AppDetails();
        appDetails.setSoftwareVersion(preferences.getString(Constants.PREFS_KEY_SOFTWARE_VERSION, ""));
        appDetails.setOperatingSystems(preferences.getString(Constants.PREFS_KEY_OPERATING_SYSTEM, ""));
        appDetails.setDatePublished(preferences.getString(Constants.PREFS_KEY_DATE_PUBLISHED, ""));
        appDetails.setNumDownloads(preferences.getString(Constants.PREFS_KEY_NUM_DOWNLOADS, ""));

        return appDetails;
    }

}
