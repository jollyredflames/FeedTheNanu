package com.ramsy.GameCentre.SlidingTiles;

import android.graphics.Bitmap;

/**
 * A class that encapsulates static image related methods
 */

public class ImageUtils {

    private ImageUtils() {}

    /**
     * Returns a new Bitmap with original aspect ratio, that is scaled
     * by its height or width (shortest dimension) so as to fit inside
     * a square of side length n.
     * @param i
     * @param n
     * @return
     */

    public static Bitmap scaleImageToFitInsideSquareOfSideLength(Bitmap i, double n) {

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
     * PRECONDITION: image is a square image
     *
     * Returns a Bitmap[] with n^2 images, where each image is a square,
     * and the array is ordered such that when all the elements are placed together in row major order,
     * they form the original image with the original image's size.
     * @param i
     * @param n
     * @return
     */

    public static Bitmap[] sliceImageIntoGridOfSideLength(Bitmap i, int n) {

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

}
