package com.ramsy.GameCentre.SlidingTiles;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.NumberPicker;

import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.FirebaseFuncts;
import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.User;
import com.ramsy.GameCentre.GameCentreCommon.LoginPage;
import com.ramsy.GameCentre.MemoryMatrix.MemoryMatrixActivity;
import com.ramsy.slidingtiles.R;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * check if the user wants to play with pictures or tiles and then take them to the game
 */
public class SlidingTilesSizeActivity extends AppCompatActivity {
    String bg;
    int size;
    Button[] group1;
    User meUser;
    Button next;
    NumberPicker np;
    int slot;
    public static Bitmap image;
    private static final int PICK_IMAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new FirebaseFuncts(LoginPage.uid);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game_size2);
        meUser = FirebaseFuncts.getUser();
        np = findViewById(R.id.numberPicker);
        next = findViewById(R.id.four);
        np.setMinValue(2);
        np.setMaxValue(10);
        image = null;
        Button picture = findViewById(R.id.picture);
        Button number = findViewById(R.id.number);
        this.group1 = new Button[] {picture, number};
        SetupPictureListener();
        SetupNumberListener();
        SetupNextListener();

    }

    /**
     * see if the user clicked on tiles game or picture game, also change button color
     */
    public void SetupPictureListener(){
        this.group1[0].setOnClickListener((V) ->{
            this.bg = "p";
            next.setBackgroundColor(getColor(R.color.app_button1));
            for (Button b: this.group1){
                b.setBackgroundColor(getColor(R.color.app_button1));
            }
            this.group1[0].setBackgroundColor(getColor(R.color.app_button));
            });
        }

    /**
     *  see if the user wants to play tiles or picture game
     */
    public void SetupNumberListener(){
        this.group1[1].setOnClickListener((V) ->{
            this.bg = "n";
            next.setBackgroundColor(getColor(R.color.app_button1));
            for (Button b: this.group1){
                b.setBackgroundColor(getColor(R.color.app_button1));
            }
            this.group1[1].setBackgroundColor(getColor(R.color.app_button));
        });
    }

    /**
     * take the user to the game after selecting the image they want to use
     */
    public void SetupNextListener(){
        this.next.setOnClickListener((V) ->{
            this.next.setBackgroundColor(getColor(R.color.app_button));
            this.size = this.np.getValue();
            this.slot = meUser.correctSlot("SlidingTiles");
            if (this.bg != null) {
                if(bg.equals("p")){
                openGallery();
                }
                else{
                    Intent t = new Intent(this, MemoryMatrixActivity.class);
                    image = null;
                    t.putExtra("size", this.size);
                    t.putExtra("slot", this.slot);
                    startActivity(t);
                }
            }
            });
    }

    /**
     * if the user wants to play with a picture, take them to the gallery to select their image
     */
    private void openGallery() {
        Intent gallery = new Intent (Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);



    }
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri imageUri = data.getData();
            try {
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Intent t = new Intent(this, MemoryMatrixActivity.class);
            t.putExtra("size", this.size);
            t.putExtra("slot", 0);
            startActivity(t);
        }
    }
    //citation:
    // https://stackoverflow.com/questions/9107900/how-to-upload-image-from-gallery-in-android

}



