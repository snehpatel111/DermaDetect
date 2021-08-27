package com.example.android.dermadetect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignUp extends AppCompatActivity {

    private static final String TAG ="1" ;
    private static final int SG = 1;
    private FirebaseAuth firebaseAuth;
    FirebaseUser user;
    GoogleSignInClient mgoogleSignInClient;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final EditText email = findViewById(R.id.Sign_up_email);
        final EditText password = findViewById(R.id.Sign_up_password);
        final EditText confirmPassword = findViewById(R.id.Sign_up_confirm_password);
        Button signUp = findViewById(R.id.Sign_Up_Button);
        SignInButton gSignUp = findViewById(R.id.google_button);
        Button Login = findViewById(R.id.SignUp_Login_Button);
        final EditText username = findViewById(R.id.Sign_up_username);
        getSupportActionBar().hide();

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this,SignIn.class);
                startActivity(intent);
            }
        });

        gSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId())
                {
                    case R.id.google_button:
                        signIn();
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(email.getText().toString(),password.getText().toString(),confirmPassword.getText().toString(),username.getText().toString());
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1046307866502-tcb13cf3mi4ks0qeqgss9fcvin7j5d9p.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mgoogleSignInClient = GoogleSignIn.getClient(this,gso);
    }

    private void signIn() {

        Intent signInIntent = mgoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, SG);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==SG)
        {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignIn(task);
        }
    }

    private void handleSignIn(Task<GoogleSignInAccount> task) {
        try{
            GoogleSignInAccount account = task.getResult(ApiException.class);

            firebaseAuthWithGoogle(account.getIdToken());


        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {


                    user = firebaseAuth.getCurrentUser();
                    updateUI(user);

                }
            }
        });
    }

    private void createAccount(final String email, String password, String confirmPassword, String username) {
        if(password.equals(confirmPassword)) {

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        sendEmailVerification(email);
                    }
                    else{
                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                        switch (errorCode) {
                            case "ERROR_INVALID_CUSTOM_TOKEN":
                                Toast.makeText(getApplicationContext(), "The custom token format is incorrect. Please check the documentation.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                                Toast.makeText(getApplicationContext(), "The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_WRONG_PASSWORD":
                                Toast.makeText(getApplicationContext(), "The custom token format is incorrect. Please check the documentation.", Toast.LENGTH_LONG).show();
                                break;


                            case "ERROR_INVALID_CREDENTIAL":
                                Toast.makeText(getApplicationContext(), "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_INVALID_EMAIL":
                                Toast.makeText(getApplicationContext(), "The email address is badly formatted.", Toast.LENGTH_LONG).show();

                            case "ERROR_USER_MISMATCH":
                                Toast.makeText(getApplicationContext(), "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_REQUIRES_RECENT_LOGIN":
                                Toast.makeText(getApplicationContext(), "This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show();
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

                            case "ERROR_USER_TOKEN_EXPIRED":
                                Toast.makeText(getApplicationContext(), "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_INVALID_USER_TOKEN":
                                Toast.makeText(getApplicationContext(), "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_OPERATION_NOT_ALLOWED":
                                Toast.makeText(getApplicationContext(), "This operation is not allowed. You must enable this service in the console.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_USER_NOT_FOUND":
                                Toast.makeText(getApplicationContext(), "There is no user record corresponding to this identifier. The user may have been deleted.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_WEAK_PASSWORD":
                                Toast.makeText(getApplicationContext(), "The given password is Weak.", Toast.LENGTH_LONG).show();

                                break;
                        }
                    }
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(),"PASSWORD DOES NOT MATCHED",Toast.LENGTH_SHORT).show();
            Log.d(TAG, "PASSWORD DOES NOT MATCHED" );
        }
    }

    public void sendEmailVerification(String email) {
        // [START send_email_verification]
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Successfully Login : please verify your email",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),SignIn.class));
                        }
                    }
                });
        // [END send_email_verification]
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI(user);
    }

    private void updateUI(FirebaseUser user) {
        if(user!= null)
        {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        else
        {
            Toast.makeText(getApplicationContext(),"User does not signed in",Toast.LENGTH_SHORT).show();
        }
    }
}