package io.vorbind.chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.vorbind.chat.helpers.FirebaseUserStorageHelper;

public class Login extends AppCompatActivity implements View.OnClickListener {

    public CountryCodePicker ccp;
    public EditText phoneTextBox;
    public LinearLayout phoneNumberInputSection;
    public PinView pinView;
    public Button one, two, three, four, five, six, seven, eight, nine, zero, backspace, nextScreen;
    public TextView heading;

    protected SharedPreferences localData;
    private SharedPreferences.Editor localDataEditor;
    //Firebase authentication variables
    private FirebaseAuth mAuth;
    private String verificationId;
    private FirebaseUserStorageHelper firebaseUserStorageHelper;
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            verificationId = s;
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();

            // checking if the code
            // is null or not.
            if (code != null) {
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                pinView.setText(code);

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                verifyCode(code);
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Firebase authentication variables
        mAuth = FirebaseAuth.getInstance();

        firebaseUserStorageHelper = new FirebaseUserStorageHelper();

        ccp = findViewById(R.id.ccp);
        phoneTextBox = findViewById(R.id.PhoneTextBox);
        phoneTextBox.setEnabled(false);
        phoneTextBox.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        phoneTextBox.setTextColor(Color.BLACK);
        phoneNumberInputSection = findViewById(R.id.phoneNumberInputSection);
        pinView = findViewById(R.id.pinView);
        pinView.setPasswordHidden(true);
        heading = findViewById(R.id.heading);
        localData = getSharedPreferences("io.vorbind.chat.loginInfo", MODE_PRIVATE);
        localDataEditor = localData.edit();
        pinView.setVisibility(View.GONE);
        phoneNumberInputSection.setVisibility(View.VISIBLE);


        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        five = findViewById(R.id.five);
        six = findViewById(R.id.six);
        seven = findViewById(R.id.seven);
        eight = findViewById(R.id.eight);
        nine = findViewById(R.id.nine);
        zero = findViewById(R.id.zero);
        backspace = findViewById(R.id.backspace);
        nextScreen = findViewById(R.id.next);

        ccp.registerCarrierNumberEditText(phoneTextBox);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);
        zero.setOnClickListener(this);
        backspace.setOnClickListener(this);
        nextScreen.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        String currentText = phoneTextBox.getText().toString();
        String currentOTP = Objects.requireNonNull(pinView.getText()).toString();

        if (id == R.id.backspace) {
            // Handle backspace button
            if (phoneNumberInputSection.getVisibility() == View.VISIBLE) {
                if (!currentText.isEmpty()) {
                    phoneTextBox.setText(currentText.substring(0, currentText.length() - 1));
                }
            } else {
                if (!currentOTP.isEmpty()) {
                    pinView.setText(currentOTP.substring(0, currentOTP.length() - 1));
                }
            }
        } else if (id == R.id.next) {
            if (phoneNumberInputSection.getVisibility() == View.VISIBLE) {
                if (ccp.isValidFullNumber()) {
                    phoneNumberInputSection.setVisibility(View.GONE);
                    heading.setText(R.string.enter_otp);
                    pinView.setVisibility(View.VISIBLE);
                    Toast.makeText(Login.this, "Follow the command for opening browser for verification", Toast.LENGTH_SHORT).show();

                    //Firebase authentication
                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(mAuth)
                                    .setPhoneNumber(ccp.getFullNumberWithPlus())            // Phone number to verify
                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                    .setActivity(this)                 // Activity (for callback binding)
                                    .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                } else {
                    Toast.makeText(Login.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                }
            } else {
                verifyCode(pinView.getText().toString());
            }
        } else {
            // Handle number buttons
            Button button = (Button) view;
            String buttonText = button.getText().toString();
            if (phoneNumberInputSection.getVisibility() == View.VISIBLE) {
                phoneTextBox.setText(currentText.concat(buttonText));
            } else {
                pinView.setText(currentOTP.concat(buttonText));
            }
        }
    }

    private void verifyCode(String code) {
        // below line is used for getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // if the code is correct and the task is successful
                // we are sending our user to new activity.
                otpVerified(task.isSuccessful());

            } else {
                // if the code is not correct then we are
                // displaying an error message to the user.
                Toast.makeText(Login.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void otpVerified(boolean isVerified) {
        if (isVerified) {
            firebaseUserStorageHelper.storeUserData(ccp.getFullNumberWithPlus(), "User", " ");
            Toast.makeText(Login.this, "OTP verified successfully", Toast.LENGTH_SHORT).show();
            localDataEditor.putString("phoneNumber", ccp.getFullNumberWithPlus()).apply();
            localDataEditor.putBoolean("loggedIn", true).apply();
            Intent intent = new Intent(Login.this, Profile.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}