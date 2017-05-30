package interval.com.intervalapp.sample;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import interval.com.intervalapp.R;


public class NewAccountActivity extends AppCompatActivity {

    @BindView(R.id.accountName)
    TextView accountName;

    @BindView(R.id.accountSurname)
    TextView accountSurname;

    @BindView(R.id.accountEmail)
    TextView accountEmail;

    @BindView(R.id.accountPassword)
    TextView accountPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account_activity);

        final View root = LayoutInflater.from(NewAccountActivity.this).inflate(R.layout.activity_new_account_activity, null, false);
        ButterKnife.bind(root, this);

        new AlertDialog.Builder(NewAccountActivity.this)
                .setTitle("Czy chcesz usunac usera?")
                .setView(root);
    }
}
