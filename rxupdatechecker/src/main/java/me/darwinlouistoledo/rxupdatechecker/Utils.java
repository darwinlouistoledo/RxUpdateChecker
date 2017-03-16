package me.darwinlouistoledo.rxupdatechecker;

import android.content.Context;
import android.content.SharedPreferences;
import java.lang.ref.WeakReference;

/**
 * Created by darwinlouistoledo on 11/9/16.
 */
class Utils {

  private static WeakReference<SharedPreferences> weakReference;

  /**
   * Since the library check from the Desktop Web Page of the app the "Current Version" field, if
   * there are different apks for the app,
   * the Play Store will shown "Varies depending on the device", so the library can't compare it to
   * versionName installed.
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

  public static void saveAppVersion(Context context, String package_name, String found_version){
    if (weakReference == null || weakReference.get()==null){
      SharedPreferences sharedPreferences = context.getSharedPreferences(
          package_name+Constants.PREFS_RX_UPDATE_CHECKER, Context.MODE_PRIVATE);
      weakReference = new WeakReference<>(sharedPreferences);

    }

    weakReference.get().edit().putString(package_name+Constants.KEY_RUC_VERSION, found_version).commit();
  }

  public static String getAppVersion(Context context, String package_name){
    if (weakReference == null || weakReference.get()==null){
      SharedPreferences sharedPreferences = context.getSharedPreferences(
          package_name+Constants.PREFS_RX_UPDATE_CHECKER, Context.MODE_PRIVATE);
      weakReference = new WeakReference<>(sharedPreferences);
    }

    return weakReference.get().getString(package_name+Constants.KEY_RUC_VERSION, "");
  }

  public static void clear(){
    weakReference=null;
  }

}
