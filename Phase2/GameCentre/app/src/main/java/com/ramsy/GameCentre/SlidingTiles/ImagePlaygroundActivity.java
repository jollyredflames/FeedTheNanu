package com.ramsy.GameCentre.SlidingTiles;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ramsy.GameCentre.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ImagePlaygroundActivity extends AppCompatActivity {

    /**
     * Given a picture, size the image to fit into the square to be played in SlidingTiles
     * @param i the picture to be scaled
     * @param n number of rows we want in our game
     * @return picture that will fit perfectly in square
     */
    Bitmap scaleImageToFitInsideSquareOfSideLength(Bitmap i, double n) {

        /*
        Returns a new Bitmap with original aspect ratio, that is scaled
        by its height or width (shortest dimension) so as to fit inside
        a square of side length n.
         */

        int w = i.getWidth();
        int h = i.getHeight();

        double scaleFactor = (w > h) ? n / h : n / w;
        int new_w = (int) (w * scaleFactor);
        int new_h = (int) (h * scaleFactor);

        Bitmap image = Bitmap.createScaledBitmap(i, new_w, new_h, false);

        if (w > h) {
            // Draw the image to size n * n, shifted horizontally
            int delta = image.getWidth() - (int) n;
            int offset = delta / 2;

            Bitmap image2 = Bitmap.createBitmap(image, offset, 0, (int) n, (int) n);
            return image2;

        } else {
            // Draw the image to size n * n, shifted vertically

            int delta = image.getHeight() - (int) n;
            int offset = delta / 2;

            Bitmap image2 = Bitmap.createBitmap(image, 0, offset, (int) n, (int) n);
            return image2;

        }

    }

    /**
     * Given a picture, slice the image into equal sized tiles.
     * @param i picture to be sliced
     * @param n int representing number of rows wanted.
     * @return array of mini-square-pictures in row-major order
     */
    Bitmap[] sliceImageIntoGridOfSideLength(Bitmap i, int n) {
        /*
        PRECONDITION: image is a square image

        Returns a Bitmap[] with n^2 images, where each image is a square,
        and the array is ordered such that when all the elements are placed together in row major order,
        they form the original image with the original image's size.
         */

        Bitmap[] slices = new Bitmap[n * n];
        int w = i.getWidth() / n; // new image slice will be size w * w

        for (int k = 1; k <= n * n; k += 1) {
            // Determine coordinates using i
            int y = (k - 1) / n; // Row (y)
            int x = (k - 1) % n; // Column (x)

            Bitmap slice = Bitmap.createBitmap(i, x * w, y * w, w, w);
            slices[k - 1] = slice;
        }

        return slices;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Create a View Group
        RelativeLayout container = new RelativeLayout(this);
//        this.container = container;

        // Set the Activity's window to the View Group
        setContentView(container);

        ImageView imv = new ImageView(this);

        container.addView(imv);



        // Image data tests


        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.olivia);

        // Bitmap to bytes
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();


        // Bytes to Bitmap
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(bytes);
        Bitmap result = BitmapFactory.decodeStream(arrayInputStream);


        imv.setImageBitmap(result);

    }

    /**
     * @return the width of the screen
     */
    private int screenWidth() {
        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        return display.widthPixels;
    }
}
