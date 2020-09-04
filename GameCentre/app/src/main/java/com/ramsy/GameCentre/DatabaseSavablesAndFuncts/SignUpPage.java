package com.ramsy.GameCentre.DatabaseSavablesAndFuncts;

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
import com.ramsy.GameCentre.GameCentreCommon.LoginPage;
import com.ramsy.GameCentre.R;

import java.util.ArrayList;
import java.util.HashMap;

public class SignUpPage extends AppCompatActivity {

    private Vibrator vib;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signuppage);
        setTitle("Back to Login");
        vib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Run when user clicks sign-in. Calls method to check all fields and make user if appropriate
     * @param view the "sign-up" button
     */
    public void sendSignUp (View view) {
        firebaseCheckUserNameExists(((EditText)findViewById(R.id.username_field)).getText().toString());
    }

    /**
     * Long series of ifs which make sure no user errors in text fields filled (ex. incorrect email, short password, etc.)
     * Also makes sure the Username has not already been taken
     * @param usernameExists boolean representing whether username has been taken or not.
     */
    public void checkInput(boolean usernameExists){
        EditText username_field = findViewById(R.id.username_field);
        EditText email_field = findViewById(R.id.email_field);
        EditText password_field = findViewById(R.id.password_field);
        EditText verifyPassword_field = findViewById(R.id.verifyPassword_field);

        String username = username_field.getText().toString();
        String email = email_field.getText().toString();
        String password = password_field.getText().toString();
        String verify = verifyPassword_field.getText().toString();

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
        }else if(!password.equals(verify)){
            Toast.makeText(this, "Verify Password must match original password",
                    Toast.LENGTH_SHORT).show();
            vib.vibrate(100);
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            verifyPassword_field.startAnimation(shake);
        }else if(usernameExists) {
                Toast.makeText(this, "This username has been taken",
                        Toast.LENGTH_SHORT).show();
                vib.vibrate(100);
                Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                username_field.startAnimation(shake);
        }else{
            createUser(username, email, password);
        }
    }

    /**
     * Checks whether the current name has been taken by another user. Ensures names are unique.
     * Once we hear back from database, about whether username is taken, check the rest of the fields.
     * @param username String representing what user wants to be called.
     */
    public void firebaseCheckUserNameExists(String username){
        mDatabase.child("users").orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object comparator = snapshot.getValue();
                Log.e("Snapshot: ", String.valueOf(comparator));
                Log.e("bool: ", String.valueOf(comparator == null));
                checkInput(!(comparator == null));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                checkInput(true);
            }
        });
    }

    /**
     * Run when all ifs are satisfied. Calls Database and sets new user with email and password
     * @param userID string username this user wants to be called
     * @param emailID string email of user
     * @param password string password of user
     */
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

    /**
     * Creates user object to represent this user.
     * @param username what to call this user
     * @param emailID String emailid of this user
     * @param refUser unique string to represent user in database
     */
    private void setUsernameAndInitialize(String username, String emailID, FirebaseUser refUser){
        User newUser = new User(username, emailID, refUser.getUid(), new HashMap<String, ArrayList<String>>(), new HashMap<String, ArrayList<SaveState>>());
        String uid = refUser.getUid();
        mDatabase.child("users").child(uid).setValue(newUser);
    }

    /**
     * Once sign-up button is pressed, either show user a pop-up describing problem preventing account creation. Else log the user in.
     * @param uname FirebaseUser object to represent user in database
     * @param e Optional argument parameter to describe error if one occurred.
     */
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
