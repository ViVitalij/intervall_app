package interval.com.intervalapp.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import interval.com.intervalapp.R;

public class ResetPasswordActivity extends AppCompatActivity {

    @BindView(R.id.email)
    protected EditText emailEditText;

    @BindView(R.id.progress_bar)
    protected ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_activity);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.back_button)
    protected void backButtonClicked() {
        finish();
    }

    @OnClick(R.id.reset_password_button)
    protected void resetPasswordButtonClicked() {
        String email = emailEditText.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplication(), R.string.enter_email_id, Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPasswordActivity.this, R.string.instruction_sent,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ResetPasswordActivity.this, R.string.reset_email_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}