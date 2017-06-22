package interval.com.intervalapp.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import interval.com.intervalapp.R;

public class SongDragAndDropActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_draganddrop_activity);
        if (savedInstanceState == null) {
            showFragment(BoardFragment.newInstance());
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat
                .getColor(this, R.color.darker_gray)));
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, getString(R.string.fragment)).commit();
    }
}
