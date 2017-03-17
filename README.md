RxUpdateChecker
===================

A library that will check for an update of an App from Google Play Store with the use of RxAndroid and RxJava.

----------


Gradle configuration
-------------------------------
First, you need to add this gradle configuration in your Project's `build.gradle` file.

```gradle
...
repositories {
    jcenter()
}
...
```
Second, you need to add this gradle configuration in your App's `build.gradle` file.
```gradle
...
dependencies {
    ..
    compile 'io.reactivex:rxjava:1.2.7'
    compile 'io.reactivex:rxandroid:1.2.1'

    compile 'me.darwinlouistoledo:rxupdatechecker:1.0.1'
    ..
}
...
```

Usage
---------
To use this library your **minSdkVersion** must be >= 16.

You can use this by adding the code in your `Application` class or in your `Launcher Activity` class. See code snippet below.


**Using Lambda Expression**
```java
@Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    RxUpdateChecker.getInstance(this)
        .check()
        .subscribe(hasUpdate -> {
          if (hasUpdate) {
            //Version Found. Do something here...
          } else {
            //Version Not Found. Do something here...
          }
        }, throwable -> {
            //Exception occurs. Do something here...
        });
}
```

**Using Normal Expression**
```java
@Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    RxUpdateChecker.getInstance(this)
        .check()
        .subscribe(new Action1<Boolean>() {
          @Override public void call(Boolean hasUpdate) {
            if (hasUpdate) {
              //Version Found. Do something here...
            } else {
              //Version Not Found. Do something here...
            }
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            //Exception occurs. Do something here...
          }
        });
  }
```

Remarks
------------
Any suggestions are welcome to improve the implementation. =)


License
------------
```
MIT License

Copyright (c) 2016 Darwin Louis Toledo

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

```
