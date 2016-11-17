package me.dlet.rxappupdatechecker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import me.darwinlouistoledo.rxupdatechecker.RxUpdateChecker;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String mPackage = "com.dlet.circleswithcolor.android";
        new RxUpdateChecker(this)
                .enableLog()
                .forDebugging()
                .packageName(mPackage)
                .check()
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        Toast.makeText(MainActivity.this, "There's an update available for package " + mPackage, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "There's no update for package " + mPackage, Toast.LENGTH_LONG).show();
                    }
                }, throwable -> {
                    Toast.makeText(MainActivity.this, "An error occured while checking update", Toast.LENGTH_LONG).show();
                });
    }
}
