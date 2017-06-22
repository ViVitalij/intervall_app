package interval.com.intervalapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import interval.com.intervalapp.R;

@SuppressWarnings("Unchecked")
public class UserAccountSettingsActivity extends AppCompatActivity
        implements FirebaseAuth.AuthStateListener {

    @BindView(R.id.old_email_edit_text)
    protected EditText oldEmailEditText;

    @BindView(R.id.new_email_edit_text)
    protected EditText newEmailEditText;

    @BindView(R.id.new_password_edit_text)
    protected EditText newPasswordEditText;
    private FirebaseAuth firebaseAuth;

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_account_settings_layout);
        ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
    }

    @OnClick(R.id.change_email_button)
    protected void changeEmailClicked() {
        if (user != null && !newEmailEditText.getText().toString().trim().equals("")) {
            user.updateEmail(newEmailEditText.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserAccountSettingsActivity.this,
                                        R.string.email_updated, Toast.LENGTH_LONG).show();
                                signOut();
                            } else {
                                Toast.makeText(UserAccountSettingsActivity.this,
                                        R.string.email_update_failed, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else if (newEmailEditText.getText().toString().trim().equals("")) {
            newEmailEditText.setError(getString(R.string.enter_email));
        }
    }

    @OnClick(R.id.change_password_button)
    protected void changePasswordClicked() {
        if (user != null && !newPasswordEditText.getText().toString().trim().equals("")) {
            if (newPasswordEditText.getText().toString().trim().length() < 6) {
                newPasswordEditText.setError(getString(R.string.minimum_password));
            } else {
                user.updatePassword(newPasswordEditText.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(UserAccountSettingsActivity.this,
                                            R.string.password_updated, Toast.LENGTH_SHORT).show();
                                    signOut();
                                } else {
                                    Toast.makeText(UserAccountSettingsActivity.this,
                                            R.string.password_update_failed, Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        });
            }
        } else if (newPasswordEditText.getText().toString().trim().equals("")) {
            newPasswordEditText.setError(getString(R.string.enter_password));
        }
    }

    @OnClick(R.id.remove_user_button)
    protected void removeUserClicked() {
        if (user != null) {
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserAccountSettingsActivity.this,
                                        R.string.profile_deleted, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(UserAccountSettingsActivity.this,
                                        LoginActivity.class));
                                finish();
                            } else {
                                Toast.makeText(UserAccountSettingsActivity.this,
                                        R.string.deleting_account_failed, Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
        }
    }

    @OnClick(R.id.send_email_button)
    protected void sendEmailClicked() {
        if (!oldEmailEditText.getText().toString().trim().equals("")) {
            firebaseAuth.sendPasswordResetEmail(oldEmailEditText.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserAccountSettingsActivity.this,
                                        R.string.reset_email_sent, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UserAccountSettingsActivity.this,
                                        R.string.reset_email_failed, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            oldEmailEditText.setError(getString(R.string.enter_email));
        }
    }

    @OnClick(R.id.sign_out_button)
    protected void signOutClicked() {
        signOut();
        startActivity(new Intent(UserAccountSettingsActivity.this,
                LoginActivity.class));
        finish();
    }

    private void signOut() {
        firebaseAuth.signOut();
    }


    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(this);
    }

    @SuppressWarnings("NullPointerException")
    @Override
    public void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(UserAccountSettingsActivity.this, ModeActivity.class));
            finish();
        }
    }
}