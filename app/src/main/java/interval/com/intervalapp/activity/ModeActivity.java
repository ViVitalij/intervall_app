package interval.com.intervalapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import interval.com.intervalapp.R;


public class ModeActivity extends Fragment {

    @BindView(R.id.first_mode)
    protected Button firstMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_bar_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.second_mode)
    protected void secondModeButtonClicked() {
        startActivity(new Intent(getContext(), RunActivity.class));
        Toast.makeText(getContext(), R.string.run_screen, Toast.LENGTH_SHORT).show();
    }
}
