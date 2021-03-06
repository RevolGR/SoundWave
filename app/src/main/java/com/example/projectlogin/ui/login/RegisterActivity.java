package com.example.projectlogin.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectlogin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {

    EditText etRegEmail;
    EditText etRegPassword;
    EditText etRegUsername;
    EditText etRegPhoneNumber;
    TextView tvLoginHere;
    Button btnRegister;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = FirebaseFirestore.getInstance();

        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPass);
        etRegUsername = findViewById(R.id.etRegName);
        etRegPhoneNumber = findViewById(R.id.etRegPhone);
        tvLoginHere = findViewById(R.id.tvLoginHere);
        btnRegister = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(view ->{
            createUser();
        });

        tvLoginHere.setOnClickListener(view ->{
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }

    private void createUser(){
        String email = etRegEmail.getText().toString();
        String password = etRegPassword.getText().toString();
        String phone = etRegPhoneNumber.getText().toString();

        if (TextUtils.isEmpty(email)){
            etRegEmail.setError("Email cannot be empty");
            etRegEmail.requestFocus();
        }else if (!PasswordCheck.password(password)){
            etRegPassword.setError("Invalid Password");
            etRegPassword.requestFocus();
        }else if (!Patterns.PHONE.matcher(phone).matches()){
            etRegPhoneNumber.setError("Invalid phone number +030 (231) 021-2635");
            etRegPhoneNumber.requestFocus();
        }
        else if (TextUtils.isEmpty(etRegUsername.getText().toString())){
            etRegUsername.setError("Invalid username, must be longer than 4 characters.");
            etRegUsername.requestFocus();
        }
        else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                        Map<String, Object> user = new HashMap<>();
                        user.put("username",etRegUsername.getText().toString());
                        user.put("email", etRegEmail.getText().toString());
                        user.put("phoneNumber", etRegPhoneNumber.getText().toString());
                        db.collection("users").document(etRegEmail.getText().toString()).set(user);
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }else{
                        Toast.makeText(RegisterActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}