package interval.com.intervalapp.sample;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import interval.com.intervalapp.R;

public class LoginLayout extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        String fontPath = "fonts/Pacifico.ttf";
        TextView aboutus = (TextView) findViewById(R.id.textView2);
        Typeface type = Typeface.createFromAsset(getAssets(), fontPath);
        aboutus.setTypeface(type);

//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
//        ActionBar actionBar = getActionBar();
//        actionBar.hide();
    }
}

