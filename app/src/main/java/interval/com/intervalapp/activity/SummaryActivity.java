package interval.com.intervalapp.activity;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import interval.com.intervalapp.R;

public class SummaryActivity extends AppCompatActivity {

    @BindView(R.id.goodbye_text_view)
    TextView goodbyeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_activity);
        ButterKnife.bind(this);
        setSummaryTextStyle();
    }

    private void setSummaryTextStyle() {
        String fontPath = getString(R.string.pacifico_font_path);
        Typeface type = Typeface.createFromAsset(getAssets(), fontPath);
        goodbyeTextView.setTypeface(type);
    }
}
