package com.eaccid.hocreader.presentation.preferences;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.eaccid.hocreader.R;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LeoAuthenticationDialogPreference extends DialogPreference {

    private EditText emailText;
    private EditText passwordText;
    private String persistentValue;
    private String authorized_status;
    private String authorizing_status;
    private String unauthorized_status;

    public LeoAuthenticationDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setStringAuthStatusFromContext(context);
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
        builder.setPositiveButton("SIGN IN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                authenticate(emailText.getText().toString(), passwordText.getText().toString());
            }
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
        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(authorizing_status);
        progressDialog.show();
        new LeoAuthenticationSettings()
                .leoSignInObservable(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        progressDialog.dismiss();
                        unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        onAuthorized(false);
                        unsubscribe();
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
            persistValue(authorized_status);
        } else {
            showUnauthorizedToast();
            persistValue(unauthorized_status);
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

    private void setStringAuthStatusFromContext(Context context) {
        authorized_status = context.getString(R.string.authorized_status);
        authorizing_status = context.getString(R.string.authorizing_status);
        unauthorized_status = context.getString(R.string.unauthorized_status);
    }

}
