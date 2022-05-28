package com.quizlet_dut;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);

        progressDialog = new Dialog(SignUpActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mAuth = FirebaseAuth.getInstance();

        binding.buttonBack.setOnClickListener( (view) -> {
            finish();
        });

        binding.buttonSignup.setOnClickListener( (view)-> {
            if (validate()) {
                signUpNewUser();
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
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Sign Up  Successfull",
                            Toast.LENGTH_SHORT).show();
                    DbQuery.createUserData(emailStr, nameStr, new MyCompeleteListenner() {

                        @Override
                        public void onSuccess() {
                            progressDialog.dismiss();
                            Intent intent = new Intent(SignUpActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                            SignUpActivity.this.finish();
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(SignUpActivity.this, "Something went wrong! Please Try Again Later", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Authentication failed",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}