package me.dlet.rxupdatecheckerlib;

/**
 * Created by darwinlouistoledo on 6/27/16.
 */
class Constants {
    public static final String URL_PLAYSTORE="https://play.google.com/store/apps/details?id=";

    public static final String HEADER_ACCEPT_LANGUAGE = "Accept-Language";
    public static final String VALUE_ACCEPT_LANGUAGE = "en-US";
    public static final String REFERRER = "http://www.google.com";

    public static final String DIV_ITEMPROP_SOFTWAREVERSION = "div[itemprop=softwareVersion]";
    public static final String DIV_ITEMPROP_OPERATINGSYSTEM = "div[itemprop=operatingSystems]";
    public static final String DIV_ITEMPROP_DATEPUBLISHED = "div[itemprop=datePublished]";
    public static final String DIV_ITEMPROP_NUMDOWNLOADS = "div[itemprop=numDownloads]";

    public static final String PREFS_RX_UPDATE_CHECKER="pref_rxupdatechekcer_";
    public static final String PREFS_KEY_SOFTWARE_VERSION="pref_key_software_version";
    public static final String PREFS_KEY_OPERATING_SYSTEM="pref_key_operating_system";
    public static final String PREFS_KEY_DATE_PUBLISHED="pref_key_date_published";
    public static final String PREFS_KEY_NUM_DOWNLOADS="pref_key_num_downloads";
}
