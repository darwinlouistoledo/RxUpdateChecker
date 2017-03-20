/**
 * MIT License
 *
 * Copyright (c) 2016 Darwin Louis Toledo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
