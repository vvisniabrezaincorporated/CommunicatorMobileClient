package pl.wnb.communicator.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.sql.Timestamp;
import java.util.Date;

import pl.wnb.communicator.R;
import pl.wnb.communicator.adapter.MessageAdapter;
import pl.wnb.communicator.model.Message;
import pl.wnb.communicator.presenter.StompPresenter;

public class ChatActivity extends AppCompatActivity implements StompPresenter.View {

    private EditText editTextMessage;
    private MessageAdapter messageAdapter;
    private ListView messagesView;
    private StompPresenter stompPresenter;
    private String name;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        name = bundle.getString("fullname");

        stompPresenter = new StompPresenter(this);
        stompPresenter.subscribeToMyTopic(name);

        editTextMessage = findViewById(R.id.editTextMessage);
        messageAdapter = new MessageAdapter(this);
        messagesView = findViewById(R.id.list);
        messagesView.setAdapter(messageAdapter);

        Button buttonSend = findViewById(R.id.buttonSend);

        buttonSend.setOnClickListener((View v) -> {
            stompPresenter.sendMessage(name, editTextMessage.getText().toString().trim());
        });
    }

    @Override
    public void showMessage(String name, String message, boolean myMsg) {
        runOnUiThread(() -> {
            messageAdapter.add(new Message(
                    name,
                    message,
                    new Timestamp(new Date().getTime()).toString(),
                    myMsg));
            messagesView.setSelection(messagesView.getCount() - 1);
        });
    }

    @Override
    public void updateList() {

    }
}
