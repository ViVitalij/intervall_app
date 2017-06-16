package interval.com.intervalapp.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import interval.com.intervalapp.R;

public class NewAccountActivity extends AppCompatActivity {

    @BindView(R.id.appName_textView)
    protected TextView appNameTextView;

    @BindView(R.id.email_editText)
    protected EditText emailEditText;

    @BindView(R.id.password_editText)
    protected EditText passwordEditText;

    @BindView(R.id.progress_bar)
    protected ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_account_activity);
        ButterKnife.bind(this);
        setHeaderStyle();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.create_account_button)
    void createAccountClicked() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), R.string.enter_email, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), R.string.enter_password, Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), R.string.minimum_password, Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(NewAccountActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            Toast.makeText(NewAccountActivity.this,
                                    getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(NewAccountActivity.this,
                                    getString(R.string.account_created), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(NewAccountActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                });
    }

    private void setHeaderStyle() {
        String fontPath = getString(R.string.pacifico_font_path);
        Typeface type = Typeface.createFromAsset(getAssets(), fontPath);
        appNameTextView.setTypeface(type);
    }
}
