package interval.com.intervalapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import interval.com.intervalapp.R;
import interval.com.intervalapp.activity.SelectedMode;
import interval.com.intervalapp.model.RunningMode;


public class ModeRowAdapter extends ArrayAdapter<RunningMode> {

    @BindView(R.id.row_button)
    protected Button rowButton;

    public ModeRowAdapter(Context context, List<RunningMode> mode) {
        super(context, 0, mode);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final RunningMode model = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tryb_low, parent, false);
            convertView.setLongClickable(true);
        }
        ButterKnife.bind(this, convertView);
        rowButton.setText(model.getName());
        rowButton.setLongClickable(true);

        return convertView;
    }

    @OnClick(R.id.row_button)
    protected void rowButtonClicked() {
        Intent intent = new Intent(getContext(), SelectedMode.class);
        intent.putExtra("modeName", rowButton.getText());
        getContext().startActivity(intent);
        Toast.makeText(getContext(), R.string.run_screen, Toast.LENGTH_SHORT).show();
    }
}