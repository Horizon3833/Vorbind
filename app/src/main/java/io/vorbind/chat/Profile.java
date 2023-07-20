package io.vorbind.chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import io.vorbind.chat.helpers.FirebaseUserStorageHelper;

public class Profile extends AppCompatActivity {

//    ImageView imageView;
//    private ActivityResultLauncher<String> imagePickerLauncher;
    TextInputEditText name, about;
    Button nextBtn;
    FirebaseUserStorageHelper firebaseUserStorageHelper;
    SharedPreferences sharedPreferences;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

//        imageView = findViewById(R.id.imagePicker);
//
//        imagePickerLauncher = registerForActivityResult(new ActivityRes);
//
//
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                imagePickerLauncher.launch("image/*");
//            }
//        });
        sharedPreferences = getSharedPreferences("io.vorbind.chat.loginInfo", MODE_PRIVATE);
        phoneNumber = sharedPreferences.getString("phoneNumber", "null");
        firebaseUserStorageHelper = new FirebaseUserStorageHelper();
        nextBtn = findViewById(R.id.button);
        name = findViewById(R.id.name_text);
        about = findViewById(R.id.about_text);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phoneNumber!="null") {
                    firebaseUserStorageHelper.storeUserData(phoneNumber, name.getText().toString(), about.getText().toString());
                    Intent intent = new Intent(Profile.this, Home.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}