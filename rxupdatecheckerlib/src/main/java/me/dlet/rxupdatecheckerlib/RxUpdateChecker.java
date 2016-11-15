package me.dlet.rxupdatecheckerlib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
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

    public void checkForAnUpdate(OnResultListener onResultListener){
        this.builder.check(onResultListener);
    }

    private void initiateBuilder(){
        this.builder = new Builder();
    }

    private class Builder{
        int sourceFrom = Sources.PLAYSTORE;
        boolean enableLogging=false;
        boolean isDebug = false;
        String appPackageName;
        OnResultListener onResultListener;

        private void check(OnResultListener listener){
            onResultListener=listener;
            if ((appPackageName == null || appPackageName.isEmpty()) && !isDebug)
                appPackageName = context.getPackageName();

            startHttpCall();
        }
    }

    private void startHttpCall(){
        get().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AppDetails>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage(), builder.enableLogging);
                    }

                    @Override
                    public void onNext(AppDetails details) {
                        Logger.d(details.toString(), builder.enableLogging);
                        checkIfHasNewUpdate(details);
                    }
                });
    }

    private Observable<AppDetails> get(){
        return Observable.create(new Observable.OnSubscribe<AppDetails>() {
            @Override
            public void call(Subscriber<? super AppDetails> subscriber) {
                if (builder.sourceFrom == Sources.PLAYSTORE){
                    try {
                        Document doc = Jsoup.connect(Constants.URL_PLAYSTORE.concat(builder.appPackageName))
                                .header(Constants.HEADER_ACCEPT_LANGUAGE, Constants.VALUE_ACCEPT_LANGUAGE)
                                .referrer(Constants.REFERRER)
                                .get();

                        subscriber.onNext(disectInformationFromPS(doc));
                        subscriber.onCompleted();
                    }catch (Exception e){
                        e.printStackTrace();
                        Logger.d(e.getMessage(), builder.enableLogging);
                        subscriber.onError(e);
                    }
                } else {
                    subscriber.onError(new UnknownError("Source not supported."));
                }
            }
        });
    }

    private AppDetails disectInformationFromPS(Document doc){
        AppDetails appDetails = new AppDetails();

        appDetails.setSoftwareVersion(doc.select(Constants.DIV_ITEMPROP_SOFTWAREVERSION).first().ownText());
        appDetails.setOperatingSystems(doc.select(Constants.DIV_ITEMPROP_OPERATINGSYSTEM).first().ownText());
        appDetails.setDatePublished(doc.select(Constants.DIV_ITEMPROP_DATEPUBLISHED).first().ownText());
        appDetails.setNumDownloads(doc.select(Constants.DIV_ITEMPROP_NUMDOWNLOADS).first().ownText());

        return appDetails;
    }

    private void checkIfHasNewUpdate(AppDetails appDetails){
        if (Utils.containsNumber(appDetails.getSoftwareVersion())){
            try {
                PackageInfo info = context.getPackageManager().getPackageInfo(builder.appPackageName,
                        PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);

                if (!info.versionName.equalsIgnoreCase(appDetails.getSoftwareVersion())){
                    Logger.d("New updates available", builder.enableLogging);
                    builder.onResultListener.onNewUpdateAvailable(appDetails);
                    Utils.saveAppDetails(context, appDetails);
                } else {
                    Logger.d("No updates available", builder.enableLogging);
                    Utils.saveAppDetails(context, appDetails);
                }
            } catch (PackageManager.NameNotFoundException e){
                Logger.e(e.getMessage(), builder.enableLogging);
            }
        } else {
            Logger.d("Doesn't contain any number", builder.enableLogging);
        }
    }

    public interface OnResultListener{
        void onNewUpdateAvailable(AppDetails appDetails);
    }

}
