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

public class LoginActivity extends AppCompatActivity {

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
        setContentView(R.layout.login_layout);
        ButterKnife.bind(this);
        setHeaderStyle();
        firebaseAuth = FirebaseAuth.getInstance();
        checkUserLoginStatus();
    }

    private void checkUserLoginStatus() {
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, ModeActivity.class));
            finish();
        }
    }

    private void setHeaderStyle() {
        String fontPath = getString(R.string.pacifico_font_path);
        Typeface type = Typeface.createFromAsset(getAssets(), fontPath);
        appNameTextView.setTypeface(type);
    }

    @OnClick(R.id.new_account_textView)
    void newAccountClicked() {
        startActivity(new Intent(this, NewAccountActivity.class));
    }

    @OnClick(R.id.forget_password_textView)
    void forgetPasswordClicked() {
        startActivity(new Intent(this, ResetPasswordActivity.class));
    }

    @OnClick(R.id.login_button)
    protected void loginButtonClicked() {
        final String email = emailEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), R.string.enter_email, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), R.string.enter_password, Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            if (password.length() < 6) {
                                Toast.makeText(LoginActivity.this, getString(R.string.minimum_password),
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(LoginActivity.this, getString(R.string.auth_failed),
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Intent intent = new Intent(LoginActivity.this, ModeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
}