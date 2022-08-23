package appgodx.opencv;

import android.graphics.Bitmap;

public class OpencvUtil {
    static {
        System.loadLibrary("godx-opencv");
    }
    public static native Bitmap idcardRead(Bitmap bitmap);
}
