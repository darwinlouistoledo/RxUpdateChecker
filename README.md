RxUpdateChecker
===================

A library that will check for an update of an app. Currently, this is only application for checking an update from Google PlayStore.

----------


Gradle configuration
-------------------------------
First, you need to add this gradle configuration in your `build.gradle` file.

```gradle
repositories {
    jcenter()
}

..

dependencies {
    ..
    compile 'io.reactivex:rxjava:1.2.2'
    compile 'io.reactivex:rxandroid:1.2.1'
    compile 'me.darwinlouistoledo:rxupdatechecker:0.1.0'
    ..
}

```

Usage
---------
You can use this by adding the code in your `Application` class or in your `Launcher Activity` class. See code snippet below.


**Using Lambda Expression**
```java
@Override
public void onCreate() {
        super.onCreate();

        new RxUpdateChecker(this)
                .check()
                .subscribe(hasUpdate -> {
                    if (hasUpdate) {
                        //Do anything you want when has an update in this block
                    } else {
                        //Do anything you want when has no update in this block
                    }
                }, throwable -> {
                    //Do anything you want when there's an error in this block
                });
}
```

**Using Normal Expression**
```java
@Override
public void onCreate() {
        super.onCreate();

        new RxUpdateChecker(this)
                .check()
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean hasUpdate) {
                        if (hasUpdate) {
                            //Do anything you want when has an update in this block
                        } else {
                            //Do anything you want when has no update in this block
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        //Do anything you want when there's an error in this block
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
