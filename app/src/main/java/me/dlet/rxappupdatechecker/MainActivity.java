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
package me.dlet.rxappupdatechecker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import me.darwinlouistoledo.rxupdatechecker.RxUpdateChecker;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final String mPackage = "com.adobe.reader";

    RxUpdateChecker.getInstance(this)
        .enableLog() //To enable the log of the RxUpdateChecker
        .forDebugging() //If you want to use other package just for testing
        .packageName(mPackage)//The package of the app that you want to check that is already in playstore
        .check().subscribe(new Action1<Boolean>() {
      @Override public void call(Boolean aBoolean) {
        if (aBoolean) {
          Toast.makeText(MainActivity.this, "There's an update available for package " + mPackage,
              Toast.LENGTH_LONG).show();
        } else {
          Toast.makeText(MainActivity.this, "There's no update for package " + mPackage,
              Toast.LENGTH_LONG).show();
        }
      }
    }, new Action1<Throwable>() {
      @Override public void call(Throwable throwable) {
        Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
      }
    });
  }
}
