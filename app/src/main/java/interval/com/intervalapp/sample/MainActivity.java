package interval.com.intervalapp.sample;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import interval.com.intervalapp.R;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

            String fontPath="fonts/Pacifico.ttf";
        TextView aboutus=(TextView)findViewById(R.id.textView2);
        Typeface type= Typeface.createFromAsset(getAssets(), fontPath);
        aboutus.setTypeface(type);
    }

//    Typeface fonts = Typeface.createFromAsset(getAssets(), "Pacifico.ttf");
//    TextView txtSampleTxt = (TextView) findViewById(R.id.textView2);
//    txtSampleTxt.setTypeface(fonts);



}

