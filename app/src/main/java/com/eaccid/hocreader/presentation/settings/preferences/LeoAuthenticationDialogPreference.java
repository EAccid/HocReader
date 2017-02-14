package com.eaccid.hocreader.presentation.settings.preferences;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.eaccid.hocreader.R;
import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.dictionary.AuthParameters;
import com.eaccid.hocreader.presentation.settings.Preference;

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
    private boolean sing_out;

    public LeoAuthenticationDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.persistentValue = "";
        setStringAuthStatusFromContext(context);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        emailText = (EditText) view.findViewById(R.id.email);
        passwordText = (EditText) view.findViewById(R.id.password);
    }

    @Override
    public void onActivityDestroy() {
        super.onActivityDestroy();
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        loadEmail();
        if (persistentValue.equals(authorized_status)) {
            emailText.setClickable(false);
            passwordText.setVisibility(View.GONE);
            builder.setPositiveButton(getContext().getString(R.string.sign_out), (dialog, which) -> authenticate("", ""));
            sing_out = true;
            return;
        }
        builder.setPositiveButton(getContext().getString(R.string.sign_in),
                (dialog, which) ->
                        authenticate(emailText.getText().toString(), passwordText.getText().toString()
                        )
        );
        sing_out = false;
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
                .leoSignInAndReturnObservable(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AuthParameters>() {
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
                    public void onNext(AuthParameters authParameters) {
                        onAuthorized(authParameters);
                    }
                });
    }

    private void onAuthorized(AuthParameters authParameters) {
        onAuthorized(authParameters.isAuth());
        SharedPreferences.Editor editor =
                getContext().getSharedPreferences(Preference.SHP_NAME_AUTH, Context.MODE_PRIVATE).edit();
        editor.putString(Preference.FULL_NAME_LEO, authParameters.getFullName());
        editor.putString(Preference.PICTURE_URL_LEO, authParameters.getPicUrl());
        editor.apply();
        saveEmail(emailText.getText().toString());
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

    private void saveEmail(String email) {
        SharedPreferences.Editor editor =
                getContext().getSharedPreferences(Preference.SHP_NAME_AUTH, Context.MODE_PRIVATE).edit();
        editor.putString(Preference.EMAIL_LEO, email);
        editor.apply();
    }

    private void loadEmail() {
        SharedPreferences sp = getContext().getSharedPreferences(Preference.SHP_NAME_AUTH, Context.MODE_PRIVATE);
        emailText.setText(sp.getString(Preference.EMAIL_LEO, ""));
    }

    private void showAuthorizedToast() {
        Toast.makeText(getContext(), getContext().getString(R.string.sign_in_succeed), Toast.LENGTH_SHORT).show();
    }

    private void showUnauthorizedToast() {
        String msg = getContext().getString(R.string.sign_in_failed);
        if (sing_out)
            msg = getContext().getString(R.string.sign_out_succeed);
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
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
