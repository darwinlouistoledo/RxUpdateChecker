package me.darwinlouistoledo.rxupdatechecker;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * This is a class that will check for an update of an app
 * from Google PlayStore.
 *
 * @author Darwin Louis Toledo on 6/27/16.
 */
public class RxUpdateChecker {
  private static RxUpdateChecker sSingleton;

  private Context mContext;
  private String mAppPackageName;
  private boolean isLogEnabled = false;
  private boolean isDebug = false;

  public static RxUpdateChecker getInstance(Context context) {
    if (sSingleton == null) {
      sSingleton = new RxUpdateChecker(context.getApplicationContext());
    }

    return sSingleton;
  }

  private RxUpdateChecker(Context context) {
    this.mContext = context;
  }

  /**
   * Use this if you want to see the log of the library if there's
   * an error and the result.
   *
   * @return RxUpdateChecker
   */
  public RxUpdateChecker enableLog() {
    this.isLogEnabled = true;
    return this;
  }

  /**
   * You should use this if you're providing a package name to check.
   * If you didn't use this, the package name provided will not take
   * effect. The package name from Context will be use instead.
   *
   * See {@link #packageName(String)}
   *
   * @return RxUpdateChecker
   */
  public RxUpdateChecker forDebugging() {
    this.isDebug = true;
    return this;
  }

  /**
   * The package name to test/debug. You should call {@link #forDebugging()}
   * method in order for the given package name to take effect.
   *
   * @param packageName The package name of the app to check
   * @return RxUpdateChecker
   */
  public RxUpdateChecker packageName(String packageName) {
    this.mAppPackageName = packageName;
    return this;
  }

  /**
   * The Single that will emit a boolean result. If new version
   * is available, it will return {@code true} else {@code false}.
   */
  public Single<Boolean> check() {
    return startChecking();
  }

  private Single<Boolean> startChecking() {
    if ((mAppPackageName == null || mAppPackageName.isEmpty()) && !isDebug) {
      mAppPackageName = mContext.getPackageName();
    }

    return Single.just(mAppPackageName)
        .compose(request())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

  private Single.Transformer<String, Boolean> request() {
    return new Single.Transformer<String, Boolean>() {
      @Override public Single<Boolean> call(Single<String> stringSingle) {
        return stringSingle.map(new Func1<String, Boolean>() {
          @Override public Boolean call(String packageName) {
            String version = Utils.getAppVersion(mContext, packageName);
            return version.isEmpty() ? false
                : RxUpdateChecker.this.checkIfHasNewUpdate(version, packageName);
          }
        }).flatMap(new Func1<Boolean, Single<Boolean>>() {
          @Override public Single<Boolean> call(Boolean already_found) {
            if (already_found) {
              Logger.d("Already found new version before.", isLogEnabled);
              return Single.just(true);
            } else {
              Logger.d("Checking new version from Google Play Store.",
                  RxUpdateChecker.this.isLogEnabled);

              return RxUpdateChecker.this.httpCallObservable(mAppPackageName)
                  .map(new Func1<String, Boolean>() {
                    @Override public Boolean call(String found_version) {
                      if (found_version != null) {
                        Logger.d("Version from Google Play:" + found_version,
                            RxUpdateChecker.this.isLogEnabled);
                      }

                      Utils.saveAppVersion(mContext, RxUpdateChecker.this.mAppPackageName,
                          found_version);

                      return RxUpdateChecker.this.checkIfHasNewUpdate(found_version,
                          RxUpdateChecker.this.mAppPackageName);
                    }
                  });
            }
          }
        });
      }
    };
  }

  /**
   * Will call an HTTP request to get the HTML data of the app page from Google Play Store.
   */
  private Single<String> httpCallObservable(final String packageName) {
    return Single.create(new Single.OnSubscribe<String>() {
      @Override public void call(SingleSubscriber<? super String> subscriber) {
        BufferedReader bufferedReader = null;
        try {
          HttpClient httpClient = new HttpClient();
          InputStream is = httpClient.makeHttpCall(Constants.URL_PLAYSTORE.concat(packageName));
          bufferedReader = new BufferedReader(new InputStreamReader(is));
          String foundAppVersion = RxUpdateChecker.this.findAppVersion(bufferedReader);
          subscriber.onSuccess(foundAppVersion);
        } catch (IOException iox) {
          iox.printStackTrace();
          subscriber.onError(iox);
        } finally {
          if (bufferedReader != null) {
            try {
              bufferedReader.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
      }
    });
  }

  /**
   * This method will check if the version found is a valid/numeric version.
   * And if so, it will compare to current version if the version found is
   * higher.
   */
  private Boolean checkIfHasNewUpdate(String foundVersion, String package_name) {
    if (foundVersion != null && Utils.containsNumber(foundVersion)) {
      try {
        PackageInfo info = mContext.getPackageManager()
            .getPackageInfo(package_name, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);

        if (!info.versionName.equalsIgnoreCase(foundVersion)
            && Utils.versionCompareNumerically(info.versionName, foundVersion) < 0) {
          return true;
        }
      } catch (PackageManager.NameNotFoundException e) {
        e.printStackTrace();
      }
    } else {
      throw new RuntimeException("The version of the app varies with device.");
    }

    return false;
  }

  /**
   * This method will look for the app version number from the HTML data
   *
   * @throws IOException
   */
  private String findAppVersion(BufferedReader br) throws IOException {
    String readLine;
    while ((readLine = br.readLine()) != null) {
      if (readLine.contains(
          Constants.PS_HTML_DIV_ITEMPROP_SOFTWAREVERSION)) { // Obtain HTML readLine contaning version available in Play Store
        String containingVersion = readLine.substring(
            readLine.lastIndexOf(Constants.PS_HTML_DIV_ITEMPROP_SOFTWAREVERSION)
                + 28);  // Get the String starting with version available + Other HTML tags
        String[] removingUnusefulTags = containingVersion.split(
            Constants.PS_HTML_TO_REMOVE_USELESS_DIV_CONTENT); // Remove useless HTML tags
        return removingUnusefulTags[0];
      }
    }

    throw new RuntimeException(
        "No version found or the package has not been found in Google Play Store.");
  }
}
