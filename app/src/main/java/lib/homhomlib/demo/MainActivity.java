package lib.homhomlib.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import lib.linhomhom.touch3d.TouchMenuHelper;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private TouchMenuHelper mTouchMenuHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.btnToggle);
        mTouchMenuHelper = new TouchMenuHelper(this,button,View.inflate(this, R.layout.menu, null), getResources().getDimensionPixelSize(R.dimen.menu_width), getResources().getDimensionPixelSize(R.dimen.menu_height),null);
        mTouchMenuHelper.update();
    }
}
