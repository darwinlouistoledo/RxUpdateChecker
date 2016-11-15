package me.dlet.rxappupdatechecker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.dlet.rxupdatecheckerlib.AppDetails;
import me.dlet.rxupdatecheckerlib.RxUpdateChecker;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RxUpdateChecker.with(this)
                .enableLog(true)
                .isDebug(true)
                .packageName("com.fotoku.mobile")
                .checkForAnUpdate(new RxUpdateChecker.OnResultListener() {
                    @Override
                    public void onNewUpdateAvailable(AppDetails appDetails) {

                    }
                });
    }
}
