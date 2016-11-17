package me.darwinlouistoledo.rxupdatechecker;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import rx.Observable;
import rx.Subscriber;
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
    private Context context;

    private String appPackageName;
    private boolean enableLogging=false;
    private boolean isDebug = false;

    public RxUpdateChecker(Context context) {
        this.context = context;
    }

    /**
     * Use this if you want to see the log of the library if there's
     * an error and the result.
     *
     * @return
     */
    public RxUpdateChecker enableLog(){
        this.enableLogging = true;
        return this;
    }

    /**
     * You should use this if you're providing a package name to check.
     * If you didn't use this, the package name provided will not take
     * effect. The package name from Context will be use instead.
     *
     * See {@link #packageName(String)}
     *
     * @return
     */
    public RxUpdateChecker forDebugging(){
        this.isDebug = true;
        return this;
    }

    /**
     * The package name to test/debug. You should call {@link #forDebugging()}
     * method in order for the given package name to take effect.
     *
     * @param packageName
     * @return
     */
    public RxUpdateChecker packageName(String packageName){
        this.appPackageName = packageName;
        return this;
    }

    /**
     * The observable the will check for an update. The result of this method
     * will determine if there's an update available or none.
     *
     * If aBoolean = true, then there's an update, else none.
     *
     * @return
     */
    public Observable<Boolean> check(){
        return startChecking();
    }

    private Observable<Boolean> startChecking(){
        if ((appPackageName == null || appPackageName.isEmpty()) && !isDebug)
            appPackageName = context.getPackageName();

        return Observable.just(appPackageName)
                .compose(request())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable.Transformer<String, Boolean> request(){
        return appDetailsObservable -> appDetailsObservable
                .flatMap(new Func1<String, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(final String package_name) {
                        return appDetailsObservable(package_name)
                                .doOnNext(appDetails -> {
                                    Utils.saveAppDetails(context, appDetails);
                                    Logger.d(appDetails.toString(), enableLogging);
                                })
                                .doOnError(throwable -> Logger.e("Error Occured: "+throwable.getMessage(), enableLogging))
                                .map(appDetails -> checkIfHasNewUpdate(appDetails, package_name))
                                .doOnNext(aBoolean -> releaseObject());
                    }
                });
    }

    private Observable<AppDetails> appDetailsObservable(final String packageName){
        return Observable.create(new Observable.OnSubscribe<AppDetails>() {
            @Override
            public void call(Subscriber<? super AppDetails> subscriber) {
                try {
                    Document doc = Jsoup.connect(Constants.URL_PLAYSTORE.concat(packageName))
                            .header(Constants.HEADER_ACCEPT_LANGUAGE, Constants.VALUE_ACCEPT_LANGUAGE)
                            .referrer(Constants.REFERRER)
                            .get();

                    AppDetails appDetails = new AppDetails();

                    appDetails.setSoftwareVersion(doc.select(Constants.DIV_ITEMPROP_SOFTWAREVERSION).first().ownText());
                    appDetails.setOperatingSystems(doc.select(Constants.DIV_ITEMPROP_OPERATINGSYSTEM).first().ownText());
                    appDetails.setDatePublished(doc.select(Constants.DIV_ITEMPROP_DATEPUBLISHED).first().ownText());
                    appDetails.setNumDownloads(doc.select(Constants.DIV_ITEMPROP_NUMDOWNLOADS).first().ownText());

                    subscriber.onNext(appDetails);
                    subscriber.onCompleted();
                }catch (Exception e){
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }

    private Boolean checkIfHasNewUpdate(AppDetails appDetails, String package_name){
        if (Utils.containsNumber(appDetails.getSoftwareVersion())){
            try {
                PackageInfo info = context.getPackageManager().getPackageInfo(package_name,
                        PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);

                if (!info.versionName.equalsIgnoreCase(appDetails.getSoftwareVersion())
                        && Utils.versionCompareNumerically(info.versionName, appDetails.getSoftwareVersion())<0){
                    return true;
                }
            } catch (PackageManager.NameNotFoundException e){
                e.printStackTrace();
            }
        }

        return false;
    }

    private void releaseObject(){
        this.context=null;
        this.appPackageName=null;
    }

}
