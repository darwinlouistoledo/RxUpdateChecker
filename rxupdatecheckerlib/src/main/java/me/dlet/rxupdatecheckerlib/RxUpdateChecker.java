package me.dlet.rxupdatecheckerlib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by darwinlouistoledo on 6/27/16.
 */
public class RxUpdateChecker {
    private static RxUpdateChecker instance;
    private Context context;

    private Builder builder;

    public static RxUpdateChecker with(Context context){
        if (instance==null)
            instance = new RxUpdateChecker(context.getApplicationContext());

        return instance;
    }

    private RxUpdateChecker(Context context) {
        this.context = context;
        initiateBuilder();
    }

    public RxUpdateChecker enableLog(boolean enable){
        this.builder.enableLogging = enable;
        return this;
    }

    public RxUpdateChecker isDebug(boolean isDebug){
        this.builder.isDebug = isDebug;
        return this;
    }

    public RxUpdateChecker packageName(String packageName){
        this.builder.appPackageName = packageName;
        return this;
    }

    public Observable<Boolean> check(){
        return this.builder.check();
    }

    private void initiateBuilder(){
        this.builder = new Builder();
    }

    private class Builder{
        int sourceFrom = Sources.PLAYSTORE;
        boolean enableLogging=false;
        boolean isDebug = false;
        String appPackageName;

        private Observable<Boolean> check(){
            if ((appPackageName == null || appPackageName.isEmpty()) && !isDebug)
                appPackageName = context.getPackageName();

            return startChecking();
        }

        private Observable<Boolean> startChecking(){
            return Observable.just(appPackageName)
                    .compose(request())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }

        private Observable.Transformer<String, Boolean> request(){
            return new Observable.Transformer<String, Boolean>() {
                @Override
                public Observable<Boolean> call(final Observable<String> appDetailsObservable) {
                    return appDetailsObservable
                            .flatMap(new Func1<String, Observable<Boolean>>() {
                                @Override
                                public Observable<Boolean> call(final String package_name) {
                                    return appDetailsObservable(package_name)
                                            .doOnNext(new Action1<AppDetails>() {
                                                @Override
                                                public void call(AppDetails appDetails) {
                                                    Utils.saveAppDetails(context, appDetails);
                                                    Logger.d(appDetails.toString(), builder.enableLogging);
                                                }
                                            })
                                            .map(new Func1<AppDetails, Boolean>() {
                                                @Override
                                                public Boolean call(AppDetails appDetails) {
                                                    return checkIfHasNewUpdate(appDetails, package_name);
                                                }
                                            });
                                }
                            });
                }
            };
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

        private boolean checkIfHasNewUpdate(AppDetails appDetails, String package_name){
            if (Utils.containsNumber(appDetails.getSoftwareVersion())){
                try {
                    PackageInfo info = context.getPackageManager().getPackageInfo(package_name,
                            PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);

                    if (!info.versionName.equalsIgnoreCase(appDetails.getSoftwareVersion())){
                        return true;
                    }
                } catch (PackageManager.NameNotFoundException e){
                    e.printStackTrace();
                }
            }

            return false;
        }
    }

}
