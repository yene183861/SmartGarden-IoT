package vn.hust.soict.project.iotapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.hust.soict.project.iotapp.R;
import vn.hust.soict.project.iotapp.api.ApiService;
import vn.hust.soict.project.iotapp.api.RetrofitInstance;
import vn.hust.soict.project.iotapp.model.User;

public class SignUpActivity extends AppCompatActivity {

    private EditText edtTxtEmail, edtTxtPassword, edtTxtConfirmPassword;
    private TextView btnSignUp, txtViewNotification;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initUi();
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void initUi() {
        edtTxtEmail = findViewById(R.id.edtTxtEmail);
        edtTxtPassword = findViewById(R.id.edtTxtPassword);
        edtTxtConfirmPassword = findViewById(R.id.edtTxtConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        txtViewNotification = findViewById(R.id.txtViewNotification);
        progressDialog = new ProgressDialog(this);
    }

    private void register() {
        String email = edtTxtEmail.getText().toString().trim();
        String password = edtTxtPassword.getText().toString().trim();
        String confirmPassword = edtTxtConfirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            txtViewNotification.setText("You must enter all the fields!");
            txtViewNotification.setVisibility(View.VISIBLE);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtViewNotification.setText("Email invalidate!");
            txtViewNotification.setVisibility(View.VISIBLE);
        } else if (password.length() < 6) {
            txtViewNotification.setText("Password must be at least 6 characters!");
            txtViewNotification.setVisibility(View.VISIBLE);
        } else if (password.compareTo(confirmPassword) != 0) {
            txtViewNotification.setText("Confirm password wrong!");
            txtViewNotification.setVisibility(View.VISIBLE);
        } else {
            progressDialog.show();
            User user = new User(email, password);
            ApiService apiService = RetrofitInstance.getRetrofitClient().create(ApiService.class);
            Call<User> call = apiService.registerUser(user);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        Toast.makeText(SignUpActivity.this, "Register success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Register account failed", Toast.LENGTH_SHORT).show();
                        try{
                            Log.e("register user","error: " + response.code()+ " " + response.errorBody().string());
                        } catch (IOException e){
                            e.printStackTrace();
                            Log.e("register user","error" + e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
                    Log.e("register user", String.valueOf(t));
                }
            });
        }
    }
}