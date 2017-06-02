package interval.com.intervalapp.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import interval.com.intervalapp.R;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.appName_textView)
    TextView aboutUsTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        ButterKnife.bind(this);
        String fontPath = "fonts/Pacifico.ttf";
        Typeface type = Typeface.createFromAsset(getAssets(), fontPath);
        aboutUsTextView.setTypeface(type);
    }

    @OnClick(R.id.new_account)
    void newAccountClicked() {
        Intent intent = new Intent(getApplicationContext(), NewAccountActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.login_button)
    protected void loginButtonClicked(){
        startActivity(new Intent(this, DrawerActivity.class));
    }
}

