package com.ramsy.slidingtiles;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SignUpPage extends AppCompatActivity {

    private Vibrator vib;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private boolean usernameExists;
    private boolean firstTrial = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signuppage);
        setTitle("Back to Login");
        vib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        mAuth = FirebaseAuth.getInstance();
    }

    public void sendSignUp (View view) {
        EditText username_field = findViewById(R.id.username_field);
        EditText email_field = findViewById(R.id.email_field);
        EditText password_field = findViewById(R.id.password_field);
        EditText verifyPassword_field = findViewById(R.id.verifyPassword_field);

        String username = username_field.getText().toString();
        String email = email_field.getText().toString();
        String password = password_field.getText().toString();
        String verify = verifyPassword_field.getText().toString();

        firebaseCheckUserNameExists(username);


        if (username.equals("")){
            Toast.makeText(this, "Username Field cannot be empty!",
                    Toast.LENGTH_SHORT).show();
            vib.vibrate(100);
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            username_field.startAnimation(shake);
        }else if(email.equals("")){
            Toast.makeText(this, "Email Field cannot be empty!",
                    Toast.LENGTH_SHORT).show();
            vib.vibrate(100);
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            email_field.startAnimation(shake);
        }else if(password.equals("")){
            Toast.makeText(this, "Password cannot be empty!",
                    Toast.LENGTH_SHORT).show();
            vib.vibrate(100);
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            password_field.startAnimation(shake);
        }else if(verify.equals("")){
            Toast.makeText(this, "Please verify Password",
                    Toast.LENGTH_SHORT).show();
            vib.vibrate(100);
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            verifyPassword_field.startAnimation(shake);
        }else if(check_verifyPassword(password, verify)){
            Toast.makeText(this, "Verify Password must match original password",
                    Toast.LENGTH_SHORT).show();
            vib.vibrate(100);
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            verifyPassword_field.startAnimation(shake);
        }else if(!usernameExists) {
            if(firstTrial){
                Toast.makeText(this, "Connection Error! Try Again!",
                        Toast.LENGTH_SHORT).show();
                vib.vibrate(100);
                Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                username_field.startAnimation(shake);
                firstTrial = !firstTrial;
            }else {
                Toast.makeText(this, "This username has been taken",
                        Toast.LENGTH_SHORT).show();
                vib.vibrate(100);
                Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                username_field.startAnimation(shake);
            }
        }else{
            createUser(username, email, password);
        }
    }

    public boolean check_verifyPassword(String password, String verify){
        return !password.equals(verify);
    }

    public void firebaseCheckUserNameExists(String username){
        Log.e("Penis ", " Stinnky");
        mDatabase.child("users").orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object comparator = snapshot.getValue();
                Log.e("Snapshot: ", String.valueOf(comparator));
                Log.e("bool: ", String.valueOf(comparator == null));
                setUserNameExists(comparator == null);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                setUserNameExists(true);
            }
        });
    }

    public void setUserNameExists(boolean exists){
        usernameExists = exists;
        Log.e("Username: ", String.valueOf(usernameExists));
    }

    public void createUser(String userID, String emailID, String password){
        final String username = userID;
        final String email = emailID;
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.w("Account", "createUserWithEmail:success");
                            Toast.makeText(SignUpPage.this, "Account Created",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            setUsernameAndInitialize(username, email, user);
                            updateUI(user, task.getException());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e("Account", "createUserWithEmail:failure", task.getException());
                            updateUI(null, task.getException());
                        }

                        // ...
                    }
                });
    }

    private void setUsernameAndInitialize(String username, String emailID, FirebaseUser refUser){
        User newUser = new User(username, emailID, refUser.getUid(), new HashMap<String, ArrayList<String>>(), new ArrayList<SaveState>());
        String uid = refUser.getUid();
        mDatabase.child("users").child(uid).setValue(newUser);
    }


    public void updateUI(FirebaseUser uname, Exception e){
        //Method invoked with null if login unsuccessful, else with FirebaseUser instance
        if (e != null){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Intent goToLogin = new Intent(this, LoginPage.class);
            startActivity(goToLogin);
        }
    }

}
