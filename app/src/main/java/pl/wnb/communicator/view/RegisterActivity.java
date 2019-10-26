package pl.wnb.communicator.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import pl.wnb.communicator.R;
import pl.wnb.communicator.model.User;
import pl.wnb.communicator.presenter.AuthenticationPresenter;
import pl.wnb.communicator.model.util.ContextUtil;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class RegisterActivity extends AppCompatActivity implements AuthenticationPresenter.View {

    private EditText editTextEmail;
    private EditText editTextName;
    private EditText editTextPass;

    private AuthenticationPresenter authenticationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AwesomeValidation awesomeValidation = new AwesomeValidation(BASIC);

        editTextEmail = findViewById(R.id.editTextEmail);
        awesomeValidation.addValidation( this, R.id.editTextEmail, Patterns.EMAIL_ADDRESS, R.string.invalid_email);

        editTextName = findViewById(R.id.editTextName);
        awesomeValidation.addValidation( this, R.id.editTextEmail, RegexTemplate.NOT_EMPTY, R.string.invalid_name);

        editTextPass = findViewById(R.id.editTextPass);
        awesomeValidation.addValidation( this, R.id.editTextPass,".{8,}", R.string.invalid_pass);

        awesomeValidation.addValidation(this, R.id.editTextConfPass, R.id.editTextPass, R.string.invalid_pass_conf);

        Button buttonRegister = findViewById(R.id.buttonRegister);
        Button buttonBack = findViewById(R.id.buttonBack);

        authenticationPresenter = new AuthenticationPresenter(this);

        buttonRegister.setOnClickListener((View view) -> {
            if(awesomeValidation.validate()) {
                String email = editTextEmail.getText().toString().trim();
                String username = editTextName.getText().toString().trim();
                String pass = editTextPass.getText().toString().trim();

                authenticationPresenter.signUp(new User(email, username, pass));
            }
        });

        buttonBack.setOnClickListener((View view) -> {
            redirectHome(LoginActivity.class);
        });
    }

    @Override
    public void showNotify(String info) {
        Toast toast = Toast.makeText(ContextUtil.getAppContext(), info, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void redirectHome(Class myClass) {
        Intent intent = new Intent(RegisterActivity.this, myClass);
        RegisterActivity.this.startActivity(intent);
    }
}
