package pl.wnb.communicator.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import pl.wnb.communicator.R;
import pl.wnb.communicator.presenter.StompPresenter;
import pl.wnb.communicator.model.sqlite.MessageManager;
import pl.wnb.communicator.model.sqlite.UserManager;
import pl.wnb.communicator.model.util.ContextUtil;
import pl.wnb.communicator.model.util.GlobalUserUtil;
import pl.wnb.communicator.view.ChatActivity;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class UsersFragment extends Fragment implements StompPresenter.View {

    private StompPresenter stompPresenter;
    private ListView listViewUsers;
    private ArrayAdapter arrayAdapterUsers;
    private GlobalUserUtil globalUserUtil;
    private EditText editTextClearName;
    private MessageManager messageManager;
    private UserManager userManager;

    public UsersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(View methodView, @Nullable Bundle savedInstanceState) {
        globalUserUtil = GlobalUserUtil.getInstance();
        stompPresenter = new StompPresenter(this);
        messageManager = new MessageManager();

        if (!globalUserUtil.isSubscribed()) {
            globalUserUtil.setSubscribed(true);
            stompPresenter.subscribeToGlobalTopic();
            stompPresenter.getOnlineUsers();
            stompPresenter.notifyOnline();
        }
        stompPresenter.subscribeToMyTopic("notInChat");

        AwesomeValidation awesomeValidation = new AwesomeValidation(BASIC);
        editTextClearName = getView().findViewById(R.id.editTextClearName);
        awesomeValidation.addValidation(getActivity(), R.id.editTextClearName, RegexTemplate.NOT_EMPTY, R.string.invalid_name);

        listViewUsers = getView().findViewById(R.id.listViewUsers);
        arrayAdapterUsers = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, globalUserUtil.getOnlineUsers());
        listViewUsers.setAdapter(arrayAdapterUsers);

        listViewUsers.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            try{
                userManager = new UserManager();
                userManager.getUser();
            } catch (Exception e){
                e.printStackTrace();
                showNotify("Before using chat You need to create or import key.");
                return;
            }
            String name = ((TextView) view).getText().toString();
            stompPresenter.getEmail(name).subscribe((String receiverEmail) -> {
                            Bundle bundle = new Bundle();
                            bundle.putString("fullname", name);
                            Intent intent = new Intent(getActivity(), ChatActivity.class);
                            intent.putExtras(bundle);
                            UsersFragment.this.startActivity(intent);
                    },
                    (Throwable e) -> {
                        Log.e("Call", "onError - getEmail " + e.toString());
                        showNotify(name + " does not have key created or imported.");
                    },
                    () -> Log.e("Call", "onComplete - getEmail"));
        });

        Button buttonClearSpecific = getView().findViewById(R.id.buttonClearSpecific);

        buttonClearSpecific.setOnClickListener((View view) -> {
            stompPresenter.getPublicKey(GlobalUserUtil.getInstance().getName());
            if (awesomeValidation.validate()) {
                messageManager.deleteWithName(editTextClearName.getText().toString().trim());
                showNotify("Messages deleted.");
            }
        });

        Button buttonClearAll = getView().findViewById(R.id.buttonClearAll);
        buttonClearAll.setOnClickListener((View view) -> {
            messageManager.deleteAll();
            showNotify("Messages deleted.");
        });
    }

    @Override
    public void onResume() {
        stompPresenter.subscribeToMyTopic("notInChat");
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users, container, false);
    }

    @Override
    public void showMessage(String name, String message, boolean myMsg) {

    }

    @Override
    public void showNotify(String message) {
        getActivity().runOnUiThread(() -> {
            Toast toast = Toast.makeText(ContextUtil.getAppContext(), message, Toast.LENGTH_LONG);
            toast.show();
        });
    }

    @Override
    public void updateList() {
        getActivity().runOnUiThread(() -> arrayAdapterUsers.notifyDataSetChanged());
    }

    @Override
    public void redirect(Class myClass) {
    }
}
