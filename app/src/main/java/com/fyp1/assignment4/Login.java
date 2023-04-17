package com.fyp1.assignment4;

import static com.android.volley.VolleyLog.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp1.assignment4.POJOS.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private TextView register, forgotPassword;
    private EditText editTextEmail, editTextPassword;
    private Button signIn;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = findViewById(R.id.register);
        register.setOnClickListener(this);

        signIn = findViewById(R.id.btnLogin);
        signIn.setOnClickListener(this);

        editTextEmail = findViewById(R.id.inputEmail);
        editTextPassword = findViewById(R.id.inputPassword);

        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        forgotPassword = findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                Intent registerIntent = new IntentBuilder(this, Register.class)
                        .build();
                startActivity(registerIntent);
                break;

            case R.id.btnLogin:
                userLogin();
                break;

            case R.id.forgotPassword:
                Intent forgotPasswordIntent = new IntentBuilder(this, ForgotPassword.class)
                        .build();
                startActivity(forgotPasswordIntent);
                break;
        }
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email!");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 6) {
            editTextPassword.setError("Min password length is 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user.isEmailVerified()) {
                        String uid = user.getUid();

                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    boolean isAdmin = snapshot.child("admin").getValue(Boolean.class);
                                    if (isAdmin) {
                                        Intent adminMenuIntent = new IntentBuilder(Login.this, AdminMenu.class)
                                                .build();
                                        startActivity(adminMenuIntent);
                                    } else {
                                        Intent menuIntent = new IntentBuilder(Login.this, Menu.class)
                                                .build();
                                        startActivity(menuIntent);
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle error
                            }
                        });
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(Login.this, "Check email to verify your account!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(Login.this, "Failed to login! Please check credentials!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private static class IntentBuilder {
        private final Context context;
        private final Class<?> cls;
        private Bundle extras;

        public IntentBuilder(Context context, Class<?> cls) {
            this.context = context;
            this.cls = cls;
            extras = new Bundle();
        }

        public IntentBuilder withExtra(String key, Serializable value) {
            extras.putSerializable(key, value);
            return this;
        }

        public IntentBuilder withFlags(int flags) {
            extras.putInt("flags", flags);
            return this;
        }

        public Intent build() {
            Intent intent = new Intent(context, cls);
            intent.putExtras(extras);
            return intent;
        }
    }

}

