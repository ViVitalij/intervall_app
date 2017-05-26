package interval.com.intervalapp.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import interval.com.intervalapp.R;

/**
 * Created by m.losK on 19.05.2017.
 */

public class ModeActivity extends AppCompatActivity {

    TextView textView;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mode_layout);
        initToolbar();

        textView = (TextView) findViewById(R.id.description);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                palette();
            }
        });

    }

    private void palette() {
        Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.wal_1);
        if (myBitmap != null && !myBitmap.isRecycled()) {
            Palette palette = Palette.from(myBitmap).generate();
            textView.setBackgroundColor(palette.getDominantColor(0X000000));
        }
    }

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}

