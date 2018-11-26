package com.ramsy.GameCentre.DatabaseSavablesAndFuncts;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseFuncts {

    private static final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static User meUser;
    private static GlobalLeaderBoard leaderBoard;

    /**
     * Constructor for singletons meUser and leaderboard.
     * Allows asyncrounous call to happen in background without making others worry about it.
     * @param userUID
     */
    public FirebaseFuncts(String userUID){
        mDatabase.keepSynced(true);
        Log.e("FBFUNCTS: ","Constructor was run");
        setFirebaseUser(userUID);
        setLeaderBoardFromFireBase();
    }

    /**
     * Return the User semi-singleton
     * @return meUser
     */
    public static User getUser(){
        return meUser;
    }

    /**
     * Return the globalLeaderBoard semi-singleton. If none is set, return a new Global Leader Board.
     * @return globalLeaderBoard
     */
    public static GlobalLeaderBoard getGlobalLeaderBoard(){
        return leaderBoard == null ? new GlobalLeaderBoard() : leaderBoard;
    }

    /**
     * Fire an asyncronous call to set the Leaderboard from Firebase
     */
    private static void setLeaderBoardFromFireBase(){
        mDatabase.child("GlobalLeaderBoard").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot)  {
                GlobalLeaderBoard glb = snapshot.getValue(GlobalLeaderBoard.class);
                setLeaderBoard(glb);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * Once the asyncronous call hears back from the database, set globalLeaderBoard to its value.
     * @param glb global leader board from firebase
     */
    private static void setLeaderBoard(GlobalLeaderBoard glb){
        leaderBoard = glb;
        if (glb == null){
            Log.e("Null GLB", "GLB is null");
        }else {
            Log.e("Non-Null GLB", "GLB is not null");
            Log.e("Data is: ", glb.toString());
        }
        //leaderBoard.setGlobalLeaderBoard(glb.getGlobalLeaderBoard());
    }

    /**
     * Fire an asyncronous call to set the Leaderboard from Firebase
     */
    private static void setFirebaseUser(String uid){
        Log.e("FBFUNCTS: ", "setFirebase was run");
        mDatabase.child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User thisUser = snapshot.getValue(User.class);
                setUser(thisUser);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    /**
     * Once the asyncronous call hears back from the database, set globalLeaderBoard to its value.
     * @param thisUser the User returned from Firebase
     */
    private static void setUser(User thisUser){
        meUser = thisUser;
        if (thisUser == null){
            Log.e("FBFUNCTS: Null User", "User is null");
        }else {
            Log.e("FBFUNCTS: Non-Null User", "User is not null");
        }

        Log.e("FBFUNCTS: User Scores: ", "" + meUser.getMyScores());
    }
}
