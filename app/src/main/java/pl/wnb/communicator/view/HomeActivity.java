package pl.wnb.communicator.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import pl.wnb.communicator.R;
import pl.wnb.communicator.presenter.StompPresenter;
import pl.wnb.communicator.util.GlobalUserUtil;

public class HomeActivity extends AppCompatActivity implements StompPresenter.View {

    private StompPresenter stompPresenter;
    private ListView listViewUsers;
    private ArrayAdapter arrayAdapterUsers;
    private GlobalUserUtil globalUserUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        globalUserUtil = GlobalUserUtil.getInstance();
        stompPresenter = new StompPresenter(this);
        stompPresenter.subscribeToGlobalTopic();
        stompPresenter.subscribeToMyTopic("notInChat");
        stompPresenter.getOnlineUsers();
        stompPresenter.notifyOnline();


        listViewUsers = findViewById(R.id.listViewUsers);
        arrayAdapterUsers = new ArrayAdapter(this, android.R.layout.simple_list_item_1, globalUserUtil.getOnlineUsers());
        listViewUsers.setAdapter(arrayAdapterUsers);

        listViewUsers.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            String name = ((TextView) view).getText().toString();
            Bundle bundle = new Bundle();
            bundle.putString("fullname", name);
            Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
            intent.putExtras(bundle);
            HomeActivity.this.startActivity(intent);
        });
    }

    @Override
    public void showMessage(String name, String message, boolean myMsg) {
    }

    @Override
    public void updateList() {
        runOnUiThread(() -> arrayAdapterUsers.notifyDataSetChanged());
    }
}
