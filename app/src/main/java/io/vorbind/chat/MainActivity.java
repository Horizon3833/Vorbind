package io.vorbind.chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    protected Handler handler;
    protected SharedPreferences localData;
    private boolean alreadyLoggedIn;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        localData = getSharedPreferences("io.vorbind.chat.loginInfo", MODE_PRIVATE);
        handler = new Handler();

        handler.postDelayed(this::OpenNewScreen, 500);

    }

    private void OpenNewScreen() {
        alreadyLoggedIn = localData.getBoolean("loggedIn", false);
        Intent intent = alreadyLoggedIn ? new Intent(MainActivity.this, Home.class) : new Intent(MainActivity.this, Login.class);
        startActivity(intent);
    }
}