package lib.linhomhom.touch3d;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * Created by Linhh on 16/5/11.
 */
public class TouchMenuUtil {
    @SuppressLint("NewApi")
    public static Point getScreenSize(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point pt = new Point();
        if(Build.VERSION.SDK_INT < 13){
            pt.x = display.getWidth();
            pt.y = display.getHeight();
        }else{
            display.getSize(pt);
        }

        return pt;
    }

    public static int getStatusBarHeight(Context context){

        Class<?> c = null;
        Object obj = null;
        Field field = null;

        String bar = "status_bar_height";
        int height = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField(bar);
            int x = Integer.parseInt(field.get(obj).toString());
            height = context.getResources().getDimensionPixelSize(x);
        } catch(Exception e1) {
            height = 0;
        }
        height = Math.max(0, height);
        return height;
    }

    public static Bitmap blur(Context context,Bitmap bitmap){
        final RenderScript rs = RenderScript.create(context);
        final Allocation input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(20);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap);
        return bitmap;
    }
}
