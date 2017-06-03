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
import interval.com.intervalapp.R;
import interval.com.intervalapp.activity.SelectedMode;
import interval.com.intervalapp.model.RunningMode;


public class TrybRowAdapter extends ArrayAdapter<RunningMode> {
    @BindView(R.id.row)
    protected Button rowButton;


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
        rowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SelectedMode.class);
                intent.putExtra("modeName", rowButton.getText());
                getContext().startActivity(intent);
                Toast.makeText(getContext(), R.string.run_screen, Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    public TrybRowAdapter(Context context, List<RunningMode> mode) {
        super(context, 0, mode);
    }

}