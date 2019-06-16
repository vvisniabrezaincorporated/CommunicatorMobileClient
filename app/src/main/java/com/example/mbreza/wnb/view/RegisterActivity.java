package com.example.mbreza.wnb.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.example.mbreza.wnb.R;
import com.example.mbreza.wnb.model.User;
import com.example.mbreza.wnb.presenter.AuthenticationPresenter;
import com.example.mbreza.wnb.util.ContextUtil;

public class RegisterActivity extends AppCompatActivity implements AuthenticationPresenter.View{

    private TextView textViewOne;
    private EditText editTextOne;

    private TextView textViewTwo;
    private EditText editTextTwo;

    private boolean finalStage = false;
    private String email;
    private String username;
    private AuthenticationPresenter authenticationPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textViewOne = findViewById(R.id.textViewOne);
        editTextOne = findViewById(R.id.editTextOne);

        textViewTwo = findViewById(R.id.textViewTwo);
        editTextTwo = findViewById(R.id.editTextTwo);

        Button buttonMain = findViewById(R.id.buttonMain);
        Button buttonBack = findViewById(R.id.buttonBack);

        authenticationPresenter = new AuthenticationPresenter(this);

        buttonMain.setOnClickListener((View view) -> {

            if(finalStage){
                String pass = editTextOne.getText().toString().trim();
                String passConfirm = editTextTwo.getText().toString().trim();

                if(pass.equals(passConfirm)){
                    authenticationPresenter.signUp(new User(email, username, pass));
                    Intent intentLogin = new Intent(RegisterActivity.this , LoginActivity.class);
                    RegisterActivity.this.startActivity(intentLogin);
                } else {
                    showNotify("Password do not match.");
                }
            } else {
                textViewOne.setText("Password");
                textViewTwo.setText("Confirm Password");
                buttonMain.setText("sign up");

                email = editTextOne.getText().toString().trim();
                username = editTextTwo.getText().toString().trim();

                editTextOne.setText("");
                editTextTwo.setText("");

                finalStage = true;
            }
        });

        buttonBack.setOnClickListener((View view) -> {
            if(finalStage){
                textViewOne.setText("Email");
                textViewTwo.setText("Username");
                buttonMain.setText("next");
                finalStage = false;
            } else {
                Intent intentLogin = new Intent(RegisterActivity.this , LoginActivity.class);
                RegisterActivity.this.startActivity(intentLogin);
            }
        });


    }

    @Override
    public void showNotify(String info) {
        Toast toast = Toast.makeText(ContextUtil.getAppContext(), info, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void redirectHome() {

    }
}
