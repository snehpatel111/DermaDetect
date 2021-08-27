package com.example.android.dermadetect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {

    private static final int SG = 1;

    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        final EditText email = findViewById(R.id.Sign_in_email);
        final EditText password = findViewById(R.id.Sign_in_password);
        Button signup = findViewById(R.id.Login_Signup_Button);
        Button forgot_password = findViewById(R.id.forgot_Password);
        Button Login = findViewById(R.id.Login_Button);
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String forgot_Password =email.getText().toString();
                sendForgotPassword(forgot_Password);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this,SignUp.class);
                startActivity(intent);
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAccount(email.getText().toString(),password.getText().toString(),emailPattern);
            }
        });
    }

    private void sendForgotPassword(final String forgot_Password) {
        mAuth = FirebaseAuth.getInstance();
        if(forgot_Password.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Please give user email",Toast.LENGTH_SHORT).show();
        }
        else{
            mAuth.sendPasswordResetEmail(forgot_Password)
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"Email has been sent to" + forgot_Password,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void validateAccount(String s,String s1,String emailPattern) {
        if (s.matches(emailPattern)) {
            mAuth.signInWithEmailAndPassword(s, s1)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                if(firebaseUser.isEmailVerified())
                                {
                                    updateUI(firebaseUser);
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Please verify your Email" ,Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                                switch (errorCode) {

                                    case "ERROR_WRONG_PASSWORD":
                                        Toast.makeText(getApplicationContext(), "Password is incorrect", Toast.LENGTH_LONG).show();
                                        break;


                                    case "ERROR_INVALID_CREDENTIAL":
                                        Toast.makeText(getApplicationContext(), "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_INVALID_EMAIL":
                                        Toast.makeText(getApplicationContext(), "The email address is badly formatted.", Toast.LENGTH_LONG).show();

                                    case "ERROR_USER_MISMATCH":
                                        Toast.makeText(getApplicationContext(), "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                                        Toast.makeText(getApplicationContext(), "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_EMAIL_ALREADY_IN_USE":
                                        Toast.makeText(getApplicationContext(), "The email address is already in use by another account.   ", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                                        Toast.makeText(getApplicationContext(), "This credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_USER_DISABLED":
                                        Toast.makeText(getApplicationContext(), "The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_USER_NOT_FOUND":
                                        Toast.makeText(getApplicationContext(), "There is no user record corresponding to this identifier. The user may have been deleted.", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }
                        }
                    });
        }
        else{
            Toast.makeText(getApplicationContext(), "INVALID EMAIL", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(FirebaseUser user) {
        if(user!= null )
        {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        else
        {
            Toast.makeText(getApplicationContext(),"User is not signed in",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser != null && firebaseUser.isEmailVerified())
        {
            updateUI(firebaseUser);
        }
    }

    @Override
    public void onBackPressed() {
        firebaseUser = null;
        updateUI(null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }

    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }
}