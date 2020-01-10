
package com.example.kargobike.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kargobike.R;
import com.example.kargobike.ui.order.OrdersActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class LogActivity extends AppCompatActivity {

    private static final String TAG = "LogActivity";
    static final int GOOGLE_SIGN = 123;

    private Button googleLoginBtn;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private CheckBox safetyCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        safetyCheck = (CheckBox) findViewById(R.id.chbSafetyCheck);

        safetyCheck.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               // Get token
                                               // [START retrieve_current_token]
                                               FirebaseInstanceId.getInstance().getInstanceId()
                                                       .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                           @Override
                                                           public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                               if (!task.isSuccessful()) {
                                                                   Log.w(TAG, "getInstanceId failed", task.getException());
                                                                   return;
                                                               }

                                                               // Get new Instance ID token
                                                               String token = task.getResult().getToken();

                                                               // Log and toast
                                                               String msg = getString(R.string.msg_token_fmt, token);
                                                               Log.d(TAG, msg);
                                                               Toast.makeText(LogActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                           }
                                                       });
                                           }
                                       });



        googleLoginBtn = findViewById(R.id.ButtonGoogle);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        googleLoginBtn.setOnClickListener(v -> SignInGoogle());


    }


    void SignInGoogle(){

        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, GOOGLE_SIGN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GOOGLE_SIGN){
            Task<GoogleSignInAccount> task = GoogleSignIn
                    .getSignedInAccountFromIntent(data);

            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account != null){
                    firebaseAuthWithGoogle(account);
                }
            } catch (ApiException e){
                e.printStackTrace();
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("TAG", "FirebaseAuthWithGoogle: " + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()){
                        Log.d("TAG", "Signin success !");

                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(LogActivity.this, "Signin success !" + user.getEmail(), Toast.LENGTH_SHORT);
                        updateUI(user);
                    } else {
                        Log.w("TAG","Signin failure !");
                        Toast.makeText(LogActivity.this, "Signin failed !", Toast.LENGTH_SHORT);
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if(user != null ){
            String name = user.getDisplayName();
            String email = user.getEmail();

            System.out.println(" SIGN IN : " + name + email);


            Intent orderA = new Intent(this, MainActivity.class);
            orderA.putExtra("user_name", name);
            orderA.putExtra("user_email", email);
            startActivity(orderA);
        }



    }

    /* LOGOUT METHOD
    private void Logout() {
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                task -> updateUI(null));
    }
    */


}
