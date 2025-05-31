package com.uilover.project2152.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uilover.project2152.databinding.ActivitySignupBinding;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends BaseActivity {
    ActivitySignupBinding binding;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        binding.signupBtn.setOnClickListener(v -> {
            String name = binding.nameInput.getText().toString().trim();
            String ageStr = binding.ageInput.getText().toString().trim();
            String email = binding.emailInput.getText().toString().trim();
            String password = binding.passwordInput.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(ageStr) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid age", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.progressBar.setVisibility(android.view.View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        binding.progressBar.setVisibility(android.view.View.GONE);

                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String uid = user.getUid();

                                // Prepare user data
                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put("name", name);
                                userMap.put("age", age);
                                userMap.put("email", email);

                                db.collection("users").document(uid)
                                        .set(userMap)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(this, "Firestore error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            }
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
