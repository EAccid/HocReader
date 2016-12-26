package com.eaccid.hocreader.presentation.preferences;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.eaccid.hocreader.R;
import rx.Subscriber;

public class LeoAuthenticationDialogPreference extends DialogPreference {

    private EditText emailText;
    private EditText passwordText;
    private String persistentValue;

    public LeoAuthenticationDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        emailText = (EditText) view.findViewById(R.id.email);
        passwordText = (EditText) view.findViewById(R.id.password);
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.setPositiveButton("SIGN IN", (dialogInterface, i) -> {
            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();
            authenticate(email, password);
        });
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            persistentValue = getPersistedString(persistentValue);
        } else {
            String value = (String) defaultValue;
            persistentValue = value;
            persistString(value);
        }
    }

    private void authenticate(final String email, final String password) {
        ProgressDialog progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        LeoAuthenticationSettings leoAuthenticationSettings = new LeoAuthenticationSettings(getContext());
        leoAuthenticationSettings
                .leoSignInObservable(email, password)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        onAuthorized(false);
                    }

                    @Override
                    public void onNext(Boolean isAuth) {
                        onAuthorized(isAuth);
                    }
                });
    }

    private void onAuthorized(boolean isAuth) {
        if (isAuth) {
            showAuthorizedToast();
            persistValue("Authorized");
        } else {
            showUnauthorizedToast();
            persistValue("Unauthorized");
        }
    }

    private void showAuthorizedToast() {
        Toast.makeText(getContext(), "Sign in succeed", Toast.LENGTH_SHORT).show();
    }

    private void showUnauthorizedToast() {
        Toast.makeText(getContext(), "Sign in failed", Toast.LENGTH_SHORT).show();
    }

    private void persistValue(String value) {
        if (callChangeListener(value)) {
            persistentValue = value;
            persistString(persistentValue);
        }
    }

}
