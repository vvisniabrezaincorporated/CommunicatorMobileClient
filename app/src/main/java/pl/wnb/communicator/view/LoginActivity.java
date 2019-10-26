package pl.wnb.communicator.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import pl.wnb.communicator.R;
import pl.wnb.communicator.presenter.AuthenticationPresenter;
import pl.wnb.communicator.model.util.ContextUtil;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class LoginActivity extends AppCompatActivity implements AuthenticationPresenter.View {

    private EditText editTextUsername;
    private EditText editTextPassword;

    private AuthenticationPresenter authenticationPresenter;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AwesomeValidation awesomeValidation = new AwesomeValidation(BASIC);

        editTextUsername = findViewById(R.id.editTextUsername);
        awesomeValidation.addValidation(this, R.id.editTextUsername, RegexTemplate.NOT_EMPTY, R.string.invalid_name);

        editTextPassword = findViewById(R.id.editTextPassword);
        awesomeValidation.addValidation(this, R.id.editTextPassword, RegexTemplate.NOT_EMPTY, R.string.invalid_name);

        authenticationPresenter = new AuthenticationPresenter(this);

        Button buttonSignIn = findViewById(R.id.buttonSignIn);
        Button buttonSignUp = findViewById(R.id.buttonSignUp);

        buttonSignUp.setOnClickListener((View v) -> {
            Intent intentRegister = new Intent(LoginActivity.this, RegisterActivity.class);
            LoginActivity.this.startActivity(intentRegister);
        });

        buttonSignIn.setOnClickListener((View v) -> {
            if (awesomeValidation.validate()) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                authenticationPresenter.signIn(username, password);
            }
        });
    }

    @Override
    public void showNotify(String info) {
        Toast toast = Toast.makeText(ContextUtil.getAppContext(), info, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void redirectHome(Class myClass) {
        Intent intent = new Intent(LoginActivity.this, myClass);
        LoginActivity.this.startActivity(intent);
    }
}
