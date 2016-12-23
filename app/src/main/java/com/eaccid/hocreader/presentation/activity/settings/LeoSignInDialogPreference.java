package com.eaccid.hocreader.presentation.activity.settings;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.eaccid.hocreader.R;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class LeoSignInDialogPreference extends DialogPreference {

    private EditText emailText;
    private EditText passwordText;

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
//        leoAuthSettings
//                .leoSignInObservable(email, password)
//                .observeOn(Schedulers.io())
//                .subscribe(new Subscriber<Boolean>() {
//                    @Override
//                    public void onCompleted() {
//                        progressDialog.dismiss();
//                        unsubscribe();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(Boolean isAuth) {
//                        if (!isAuth)
//                            Toast.makeText(getContext(), "Sign in failed: " + isAuth, Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

}
