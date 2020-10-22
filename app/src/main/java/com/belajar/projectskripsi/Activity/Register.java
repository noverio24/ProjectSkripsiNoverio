package com.belajar.projectskripsi.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.belajar.projectskripsi.MainActivity;
import com.belajar.projectskripsi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Register extends AppCompatActivity {
    EditText eName, eNim, eEmail, ePassword;
    Button eRegisterBtn;
    TextView eLoginBtn;

    FirebaseAuth fAuth;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        eName = findViewById(R.id.edNama);
        eNim = findViewById(R.id.edNIM);
        eEmail = findViewById(R.id.edEmail);
        ePassword = findViewById(R.id.edPassword);
        eRegisterBtn = findViewById(R.id.eRegisterBtn);


        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        eRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = eEmail.getText().toString().trim();
                String password = ePassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    eEmail.setError("Email is Required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    ePassword.setError("password is required");
                    return;
                }

                if (password.length() < 6) {
                    ePassword.setError("Password must be 6 character");
                    return;
                }
                // register user in firebase

                showLoading();

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = fAuth.getCurrentUser();

                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(eName.getText().toString())
                                    .build();

//                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(Register.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    void showLoading() {
        progressDialog = new ProgressDialog(Register.this);
        progressDialog.setMessage("Sedang Mendaftar . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

}
