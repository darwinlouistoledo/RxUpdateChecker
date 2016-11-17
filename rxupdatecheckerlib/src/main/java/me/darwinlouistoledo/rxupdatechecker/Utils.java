package me.darwinlouistoledo.rxupdatechecker;

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

    public static Integer versionCompareNumerically(String str1, String str2) {
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");
        int i = 0;
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }
        try {
            // compare first non-equal ordinal number
            if (i < vals1.length && i < vals2.length) {
                int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
                return Integer.signum(diff);
            }
            // the strings are equal or one string is a substring of the other
            // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
            else {
                return Integer.signum(vals1.length - vals2.length);
            }
        } catch (NumberFormatException e) {
            // Possibly there are different versions of the app in the store, so we can't check.
            return 0;
        }
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
