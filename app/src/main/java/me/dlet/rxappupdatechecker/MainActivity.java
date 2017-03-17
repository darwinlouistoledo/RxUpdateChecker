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
