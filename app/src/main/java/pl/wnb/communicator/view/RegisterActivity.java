package pl.wnb.communicator.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import pl.wnb.communicator.R;
import pl.wnb.communicator.model.User;
import pl.wnb.communicator.presenter.AuthenticationPresenter;
import pl.wnb.communicator.util.ContextUtil;

public class RegisterActivity extends AppCompatActivity implements AuthenticationPresenter.View{

    private TextView textViewEmailPass;
    private EditText editTextEmailPass;

    private TextView textViewNamePass;
    private EditText editTextNamePass;

    private boolean finalStage = false;
    private String email;
    private String username;
    private AuthenticationPresenter authenticationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textViewEmailPass = findViewById(R.id.textViewEmailPass);
        editTextEmailPass = findViewById(R.id.editTextEmailPass);

        textViewNamePass = findViewById(R.id.textViewNamePass);
        editTextNamePass = findViewById(R.id.editTextNamePass);

        Button buttonMain = findViewById(R.id.buttonMain);
        Button buttonBack = findViewById(R.id.buttonBack);

        authenticationPresenter = new AuthenticationPresenter(this);

        buttonMain.setOnClickListener((View view) -> {

            if(finalStage){
                String pass = editTextEmailPass.getText().toString().trim();
                String passConfirm = editTextNamePass.getText().toString().trim();

                if(pass.equals(passConfirm)){
                    authenticationPresenter.signUp(new User(email, username, pass));
                    redirectHome(LoginActivity.class);
                } else {
                    showNotify("Password do not match.");
                }
            } else {
                textViewEmailPass.setText("Password");
                textViewNamePass.setText("Confirm Password");
                buttonMain.setText("sign up");

                email = editTextEmailPass.getText().toString().trim();
                username = editTextNamePass.getText().toString().trim();

                editTextEmailPass.setText("");
                editTextNamePass.setText("");

                finalStage = true;
            }
        });

        buttonBack.setOnClickListener((View view) -> {
            if(finalStage){
                textViewEmailPass.setText("Email");
                textViewNamePass.setText("Username");
                buttonMain.setText("next");
                finalStage = false;
            } else {
                redirectHome(LoginActivity.class);
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
        Intent intent = new Intent(RegisterActivity.this, myClass);
        RegisterActivity.this.startActivity(intent);
    }
}
