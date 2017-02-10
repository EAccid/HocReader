package com.eaccid.hocreader.presentation.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.eaccid.hocreader.R;
import com.eaccid.hocreader.presentation.main.ins.Storage;
import com.nononsenseapps.filepicker.FilePickerActivity;

public class DirectoryChooser {

    public static final int FILE_CODE = 16;

    public void startOnResultDirectoryChooser(Activity context) {
        MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                .title(Build.MODEL)
                .customView(R.layout.choose_directory_dialog, false)
                .build();
        Button internal_storage = (Button) materialDialog.getView().findViewById(R.id.internal_storage);
        internal_storage.setOnClickListener(v -> {
            startOnResultDirectoryChooser(
                    context,
                    new Storage().getExternalStorage().getPath())
            ;
            materialDialog.cancel();
        });

        Button sd_card = (Button) materialDialog.getView().findViewById(R.id.sd_card);
        sd_card.setOnClickListener(v -> {
            startOnResultDirectoryChooser(
                    context,
                    new Storage().getMountedStorage().getPath()
            );
            materialDialog.cancel();
        });
        materialDialog.show();
    }

    private void startOnResultDirectoryChooser(Activity context, String filePath) {
        Intent i = new Intent(context, FilePickerActivity.class);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, true);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, true);
        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);
        i.putExtra(FilePickerActivity.EXTRA_START_PATH, filePath);
        context.startActivityForResult(i, FILE_CODE);
    }

}
