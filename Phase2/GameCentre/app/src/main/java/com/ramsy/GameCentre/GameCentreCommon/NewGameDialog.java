package com.ramsy.GameCentre.GameCentreCommon;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;

/**
 * if the user has 3 saved games, they will get a popup saying they need to delete a
 * save to begin playing
 */
public class NewGameDialog extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        String gameName = getArguments().getString("gameName");
        builder.setTitle("New Game Failed")
                .setMessage("You must select a game to delete as you already have three saves.")
                .setPositiveButton("Delete Game", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent tmp = new Intent(getActivity(), SavedGamesActivity.class);
                        tmp.putExtra("GAME_NAME", gameName);
                        startActivity(tmp);
                    }
                });
        return builder.create();
    }
}
