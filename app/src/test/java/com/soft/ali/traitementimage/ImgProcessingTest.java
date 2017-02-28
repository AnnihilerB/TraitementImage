package com.soft.ali.traitementimage;

import android.graphics.Bitmap;
import android.graphics.Color;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.File;
import java.io.FileOutputStream;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
public class ImgProcessingTest{

    @Test
    public void colorizeTest() throws Exception {

        Bitmap bitmap = Bitmap.createBitmap(1920, 1080, Bitmap.Config.ARGB_8888);
        Img image = new Img(bitmap);

        File f = new File("image.png");
        FileOutputStream fout = new FileOutputStream(f);

        bitmap.compress(Bitmap.CompressFormat.PNG, 85, fout);
        fout.flush();
        fout.close();

        boolean res = true;
        ImgProcessing.setImage(image);
        ImgProcessing.colorize();
        float[] hsv = new float[3];

        int[] pixelArray = image.getArraypixel();
        for (int i = 0; i < pixelArray.length; i++){
            Color.colorToHSV(pixelArray[i], hsv);
            if (hsv[0] != (float)120) {
                res = false;
            }
        }
        assertTrue(res);
    }
}