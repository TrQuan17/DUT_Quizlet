package com.quizlet_dut;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.quizlet_dut.databinding.ActivityLoginBinding;
import com.quizlet_dut.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private FirebaseAuth mAuth;
    private String emailStr, passStr, confirmPassStr, nameStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);

        mAuth = FirebaseAuth.getInstance();

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    signUpNewUser();
                }
            }
        });
    }

    private boolean validate(){
        nameStr = binding.userName.getText().toString().trim();
        passStr = binding.password.getText().toString().trim();
        emailStr = binding.email.getText().toString().trim();
        confirmPassStr = binding.confirmPass.getText().toString().trim();

        if (nameStr.isEmpty()){
            binding.userName.setError("Enter Your Name");
            return false;
        }

        if (emailStr.isEmpty()){
            binding.email.setError("Enter Email ID");
            return false;
        }

        if (passStr.isEmpty()){
            binding.password.setError("Enter Pass");
            return false;
        }

        if (confirmPassStr.isEmpty()){
            binding.confirmPass.setError("Enter confirm pass");
            return false;
        }

        if (passStr.compareTo(confirmPassStr) != 0){
            Toast.makeText(SignUpActivity.this,
                    "Password and confirm password should be same !",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void signUpNewUser(){
        mAuth.createUserWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Sign Up  Successfull",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SignUpActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                    SignUpActivity.this.finish();
                } else {
                    Toast.makeText(SignUpActivity.this, "Authentication failed",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}