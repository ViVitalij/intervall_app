package interval.com.intervalapp.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
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
import interval.com.intervalapp.R;

public class NewAccountActivity extends AppCompatActivity {

    @BindView(R.id.appName_textView)
    TextView appNameTextView;
    private EditText inputEmail, inputPassword;
    private Button btnSignUp;
    private FirebaseAuth authentication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_account_activity);
        ButterKnife.bind(this);
        setHeaderStyle();
        //Get Firebase authentication instance
        authentication = FirebaseAuth.getInstance();
        btnSignUp = (Button) findViewById(R.id.create_account_button);
        inputEmail = (EditText) findViewById(R.id.email_editText);
        inputPassword = (EditText) findViewById(R.id.password_editText);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //create user
                authentication.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(NewAccountActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(NewAccountActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                if (!task.isSuccessful()) {
                                    Toast.makeText(NewAccountActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(NewAccountActivity.this, LoginActivity.class));
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    private void setHeaderStyle() {
        String fontPath = getString(R.string.pacifico_font_path);
        Typeface type = Typeface.createFromAsset(getAssets(), fontPath);
        appNameTextView.setTypeface(type);
    }
}
