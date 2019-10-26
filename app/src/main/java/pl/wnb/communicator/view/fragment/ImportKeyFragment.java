package pl.wnb.communicator.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import pl.wnb.communicator.R;
import pl.wnb.communicator.presenter.EncryptionPresenter;
import pl.wnb.communicator.model.util.ContextUtil;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class ImportKeyFragment extends Fragment implements EncryptionPresenter.View {

    private EditText editTextEmailImp;
    private EditText editTextNameImp;
    private EditText editTextPassImp;

    private EditText editTexteEncPass;

    private EncryptionPresenter encryptionPresenter;

    public ImportKeyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View methodView, @Nullable Bundle savedInstanceState) {
        AwesomeValidation awesomeValidation = new AwesomeValidation(BASIC);

        editTextEmailImp = getView().findViewById(R.id.editTextEmailImp);
        awesomeValidation.addValidation(getActivity(), R.id.editTextEmailImp, Patterns.EMAIL_ADDRESS, R.string.invalid_email);

        editTextNameImp = getView().findViewById(R.id.editTextNameImp);
        awesomeValidation.addValidation(getActivity(), R.id.editTextNameImp, RegexTemplate.NOT_EMPTY, R.string.invalid_name);

        editTextPassImp = getView().findViewById(R.id.editTextPassImp);
        awesomeValidation.addValidation(getActivity(), R.id.editTextPassImp, ".{8,}", R.string.invalid_pass);

        awesomeValidation.addValidation(getActivity(), R.id.editTextConfPassImp, R.id.editTextPassImp, R.string.invalid_pass_conf);

        editTexteEncPass = getView().findViewById(R.id.editTexteEncPass);
        awesomeValidation.addValidation(getActivity(), R.id.editTexteEncPass, RegexTemplate.NOT_EMPTY, R.string.invalid_name);

        encryptionPresenter = new EncryptionPresenter(this);

        Button buttonImportKey = getView().findViewById(R.id.buttonImportKey);

        buttonImportKey.setOnClickListener((View view) -> {
            if (awesomeValidation.validate()) {
                String email = editTextEmailImp.getText().toString().trim();
                String username = editTextNameImp.getText().toString().trim();
                String pass = editTextPassImp.getText().toString().trim();
                String encPass = editTexteEncPass.getText().toString().trim();
                try {
                    encryptionPresenter.importKeys(username, email, pass, encPass);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_import_key, container, false);
    }

    @Override
    public void showNotify(String info) {
        Toast toast = Toast.makeText(ContextUtil.getAppContext(), info, Toast.LENGTH_LONG);
        toast.show();
    }
}