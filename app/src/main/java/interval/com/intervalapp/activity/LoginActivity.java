package interval.com.intervalapp.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.OnClick;
import interval.com.intervalapp.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        ButterKnife.bind(this);
        String fontPath = "fonts/Pacifico.ttf";
        TextView aboutus = (TextView) findViewById(R.id.textView2);
        Typeface type = Typeface.createFromAsset(getAssets(), fontPath);
        aboutus.setTypeface(type);
    }

    @OnClick(R.id.new_account)
    void newAccountClicked() {
        Intent intent = new Intent(getApplicationContext(), NewAccountActivity.class);
        startActivity(intent);
    }
}

