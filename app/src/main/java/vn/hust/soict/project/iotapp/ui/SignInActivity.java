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
import android.widget.LinearLayout;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.hust.soict.project.iotapp.R;
import vn.hust.soict.project.iotapp.api.ApiService;
import vn.hust.soict.project.iotapp.api.RetrofitInstance;
import vn.hust.soict.project.iotapp.datalocal.DataLocalManager;
import vn.hust.soict.project.iotapp.model.LoginResponse;
import vn.hust.soict.project.iotapp.model.User;

public class SignInActivity extends AppCompatActivity {

    private LinearLayout layoutSignUp;
    private EditText edtEmail, edtPassword;
    private TextView btnSignIn, btnForgotPassword, txtNotification;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initUi();
        initListener();
    }

    private void initUi() {
        layoutSignUp = findViewById(R.id.layoutSignUp);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        txtNotification = findViewById(R.id.txtNotification);
        progressDialog = new ProgressDialog(this);
    }

    private void initListener() {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    txtNotification.setText("You must enter email and password!");
                    txtNotification.setVisibility(View.VISIBLE);
                } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    txtNotification.setText("Email invalid!");
                    txtNotification.setVisibility(View.VISIBLE);
                } else {
                    progressDialog.show();
                    User user = new User(email, password);
                    ApiService apiService = RetrofitInstance.getRetrofitClient().create(ApiService.class);
                    Call<LoginResponse> call = apiService.login(user);
                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            progressDialog.dismiss();
                            User userResponse = response.body().getUser();
                            DataLocalManager.setTokenServer(response.body().getToken());
                            //Log.e("login", userResponse.toString());
//                            Intent intent = new Intent(SignInActivity.this, GardenManageActivity.class);
//                            intent.putExtra("user", userResponse);
//                            startActivity(intent);
//                            finishAffinity();
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            progressDialog.dismiss();
                            txtNotification.setText("Email or password wrong!");
                            txtNotification.setVisibility(View.VISIBLE);
                            Log.e("login", String.valueOf(t));
                        }
                    });
                }
            }
        });
        layoutSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}