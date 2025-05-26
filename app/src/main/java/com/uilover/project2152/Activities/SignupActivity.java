package com.uilover.project2152.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.uilover.project2152.databinding.ActivitySignupBinding;

public class SignupActivity extends BaseActivity {
    ActivitySignupBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.signupBtn.setOnClickListener(v -> {
            String email = binding.emailInput.getText().toString().trim();
            String password = binding.passwordInput.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.progressBar.setVisibility(android.view.View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                binding.progressBar.setVisibility(android.view.View.GONE);
                if (task.isSuccessful()) {
                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.loginLink.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        });
    }
}
