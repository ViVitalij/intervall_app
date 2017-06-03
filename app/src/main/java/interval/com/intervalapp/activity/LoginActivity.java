package interval.com.intervalapp.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
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
import interval.com.intervalapp.helper.ResetPasswordHelper;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.appName_textView)
    TextView aboutUsTextView;

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private Button btnLogin;
    private TextView btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        ButterKnife.bind(this);
        String fontPath = "fonts/Pacifico.ttf";
        Typeface type = Typeface.createFromAsset(getAssets(), fontPath);
        aboutUsTextView.setTypeface(type);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, ResetPasswordHelper.class));
            finish();
        }
        //TODO butterknife
        inputEmail = (EditText) findViewById(R.id.loginEmail);
        inputPassword = (EditText) findViewById(R.id.loginPassword);
        btnSignup = (TextView) findViewById(R.id.new_account);
        btnLogin = (Button) findViewById(R.id.login_button);
        auth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.new_account)
    void newAccountClicked() {
        Intent intent = new Intent(getApplicationContext(), NewAccountActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.login_button)
    protected void loginButtonClicked() {
        String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }
        //TODO getApplicationContex nawiast!
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            if (password.length() < 6) {
                                inputPassword.setError(getString(R.string.minimum_password));
                            } else {
                                Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
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
