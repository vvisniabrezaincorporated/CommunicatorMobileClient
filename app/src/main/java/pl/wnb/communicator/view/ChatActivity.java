package pl.wnb.communicator.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import pl.wnb.communicator.R;
import pl.wnb.communicator.view.adapter.MessageAdapter;
import pl.wnb.communicator.model.Message;
import pl.wnb.communicator.presenter.StompPresenter;
import pl.wnb.communicator.model.sqlite.MessageManager;
import pl.wnb.communicator.model.util.ContextUtil;
import pl.wnb.communicator.model.util.GlobalUserUtil;

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
        GlobalUserUtil.getInstance().setCurrentActivity(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        name = bundle.getString("fullname");

        stompPresenter = new StompPresenter(this);
        stompPresenter.subscribeToMyTopic(name);

        editTextMessage = findViewById(R.id.editTextMessage);
        messageAdapter = new MessageAdapter(this);
        messagesView = findViewById(R.id.list);
        messagesView.setAdapter(messageAdapter);

        MessageManager messageManager = new MessageManager();
        ArrayList<Message> messagesList = messageManager.getMessagesWithUser(name);
        for(Message message : messagesList){
            messageAdapter.add(new Message(
                    message.getName(),
                    message.getContent(),
                    message.getDate(),
                    message.isMyMsg()));
            messagesView.setSelection(messagesView.getCount() - 1);
        }

        Button buttonSend = findViewById(R.id.buttonSend);

        buttonSend.setOnClickListener((View v) -> {
            stompPresenter.sendMessage(name, editTextMessage.getText().toString().trim());
            editTextMessage.setText("");
        });
    }

    protected void onResume() {
        super.onResume();
        GlobalUserUtil.getInstance().setCurrentActivity(this);
    }

    public String getName(){
        return this.name;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void showMessage(String name, String message, boolean myMsg) {
        runOnUiThread(() -> {
            messageAdapter.add(new Message(
                    name,
                    message,
                    new SimpleDateFormat("HH.mm").format(new Date()),
                    myMsg));
            messagesView.setSelection(messagesView.getCount() - 1);
        });
    }

    @Override
    public void showNotify(String message) {
        runOnUiThread(() -> {
        Toast toast = Toast.makeText(ContextUtil.getAppContext(), message, Toast.LENGTH_LONG);
        toast.show();
        });
    }

    @Override
    public void updateList() {

    }

    @Override
    public void redirect(Class myClass) {
        Intent intent = new Intent(ChatActivity.this, myClass);
        ChatActivity.this.startActivity(intent);
    }
}
