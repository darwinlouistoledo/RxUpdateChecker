package me.darwinlouistoledo.rxupdatechecker;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by darwinlouistoledo on 3/14/17.
 */

class HttpClient {
  private final int CONNECTION_TIMEOUT_SECONDS = 5000;
  private final int IO_TIMEOUT_SECONDS = 5000;

  public InputStream makeHttpCall(String url_to_load) throws IOException {
    URL url = new URL(url_to_load);
    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    urlConnection.setConnectTimeout(CONNECTION_TIMEOUT_SECONDS);
    urlConnection.setReadTimeout(IO_TIMEOUT_SECONDS);
    urlConnection.connect();

    InputStream is = urlConnection.getInputStream();

    return is;
  }

}
