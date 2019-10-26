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

public class GenerateKeyFragment extends Fragment implements EncryptionPresenter.View {

    private EditText editTextEmailGen;
    private EditText editTextNameGen;
    private EditText editTextPassGen;
    private EditText editTextConfPassGen;

    private EncryptionPresenter encryptionPresenter;

    public GenerateKeyFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View methodView, @Nullable Bundle savedInstanceState) {
        AwesomeValidation awesomeValidation = new AwesomeValidation(BASIC);

        editTextEmailGen = getView().findViewById(R.id.editTextEmailGen);
        awesomeValidation.addValidation( getActivity(), R.id.editTextEmailGen, Patterns.EMAIL_ADDRESS, R.string.invalid_email);

        editTextNameGen = getView().findViewById(R.id.editTextNameGen);
        awesomeValidation.addValidation( getActivity(), R.id.editTextNameGen, RegexTemplate.NOT_EMPTY, R.string.invalid_name);

        editTextPassGen = getView().findViewById(R.id.editTextPassGen);
        awesomeValidation.addValidation( getActivity(), R.id.editTextPassGen,".{8,}", R.string.invalid_pass);

        editTextConfPassGen = getView().findViewById(R.id.editTextConfPassGen);
        awesomeValidation.addValidation(getActivity(), R.id.editTextConfPassGen, R.id.editTextPassGen, R.string.invalid_pass_conf);

        encryptionPresenter = new EncryptionPresenter(this);

        Button buttonGenerateKey = getView().findViewById(R.id.buttonGenerateKey);

        buttonGenerateKey.setOnClickListener((View view) -> {
            if(awesomeValidation.validate()) {
                String email = editTextEmailGen.getText().toString().trim();
                String username = editTextNameGen.getText().toString().trim();
                String pass = editTextPassGen.getText().toString().trim();
                encryptionPresenter.generateKey(username, email, pass);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_generate_key, container, false);
    }

    @Override
    public void showNotify(String info) {
        Toast toast = Toast.makeText(ContextUtil.getAppContext(), info, Toast.LENGTH_LONG);
        toast.show();
    }
}