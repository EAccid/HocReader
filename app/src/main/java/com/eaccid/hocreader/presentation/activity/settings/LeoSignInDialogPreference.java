package com.eaccid.hocreader.presentation.activity.settings;

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

public class LeoSignInDialogPreference extends DialogPreference {

    private EditText emailText;
    private EditText passwordText;
    private String valueMsg;

    public LeoSignInDialogPreference(Context context, AttributeSet attrs) {
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

    private void authenticate(final String email, final String password) {
        ProgressDialog progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        LeoAuthSettings leoAuthSettings = new LeoAuthSettings(getContext());
        leoAuthSettings
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
                        onUnAuthorized();
                    }

                    @Override
                    public void onNext(Boolean isAuth) {
                        if (!isAuth) {
                            onUnAuthorized();
                        } else {
                            onAuthorized();
                        }
                    }
                });
    }

    private void onUnAuthorized() {
        Toast.makeText(getContext(), "Sign in failed", Toast.LENGTH_SHORT).show();
        persistValue("Unauthorized");
    }

    private void onAuthorized() {
        Toast.makeText(getContext(), "Sign in succeed", Toast.LENGTH_SHORT).show();
        persistValue("Authorized");
    }

    private void persistValue(String value) {
        if (callChangeListener(value)) {
            valueMsg = value;
            persistString(valueMsg);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            valueMsg = getPersistedString(valueMsg);
        } else {
            String value = (String) defaultValue;
            valueMsg = value;
            persistString(value);
        }
    }
}
