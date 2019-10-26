package pl.wnb.communicator.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class ExportKeyFragment extends Fragment implements EncryptionPresenter.View {

    private EditText editTextAesPass;
    private EncryptionPresenter encryptionPresenter;

    public ExportKeyFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View methodView, @Nullable Bundle savedInstanceState) {
        AwesomeValidation awesomeValidation = new AwesomeValidation(BASIC);

        editTextAesPass = getView().findViewById(R.id.editTextAesPass);
        awesomeValidation.addValidation(getActivity(), R.id.editTextAesPass, RegexTemplate.NOT_EMPTY, R.string.invalid_name);

        encryptionPresenter = new EncryptionPresenter(this);

        Button buttonExportKey = getView().findViewById(R.id.buttonExportKey);

        buttonExportKey.setOnClickListener((View view) -> {
            if (awesomeValidation.validate()) {
                try {
                    encryptionPresenter.exportKeys(editTextAesPass.getText().toString().trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_export_key, container, false);
    }

    @Override
    public void showNotify(String info) {
        Toast toast = Toast.makeText(ContextUtil.getAppContext(), info, Toast.LENGTH_LONG);
        toast.show();
    }
}