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

/**
 * Created by darwinlouistoledo on 6/27/16.
 */
class Constants {
  public static final String PREFS_RX_UPDATE_CHECKER = "_pres_rxupdatechecker";
  public static final String KEY_RUC_VERSION = "_key_version";

  public static final String URL_PLAYSTORE = "https://play.google.com/store/apps/details?id=";
  public static final String PS_HTML_DIV_ITEMPROP_SOFTWAREVERSION =
      "itemprop=\"softwareVersion\"> ";
  public static final String PS_HTML_TO_REMOVE_USELESS_DIV_CONTENT = "  </div> </div>";
  public static final String PS_PACKAGE_NOT_PUBLISHED_IDENTIFIER =
      "We're sorry, the requested URL was not found on this server.";
}
