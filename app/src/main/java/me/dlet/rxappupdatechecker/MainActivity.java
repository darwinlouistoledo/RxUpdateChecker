package me.dlet.rxappupdatechecker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import me.dlet.rxupdatecheckerlib.RxUpdateChecker;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String mPackage = "com.fotoku.mobile";
        RxUpdateChecker.with(this)
                .enableLog(true)
                .isDebug(true)
                .packageName(mPackage)
                .check()
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "An error occured while checking update", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean){
                            Toast.makeText(MainActivity.this, "There's an update available for package "+mPackage, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "There's no update for package "+mPackage, Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
}
