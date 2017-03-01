package com.eaccid.hocreader.presentation.main.ins.directories;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.widget.Button;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.eaccid.hocreader.R;
import com.eaccid.hocreader.App;
import com.nononsenseapps.filepicker.FilePickerActivity;

import javax.inject.Inject;

public class DirectoryChooser {

    public static final int FILE_CODE = 16;

    @Inject
    DirectoriesPreferences customDirectories;

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
        Button delete_directories = (Button) materialDialog.getView().findViewById(R.id.delete_directories);
        delete_directories.setOnClickListener(v -> {
            startDirectoryDeleter(context);
            materialDialog.cancel();
        });
        materialDialog.show();
    }

    private void startOnResultDirectoryChooser(Activity context, String filePath) {
        Intent i = new Intent(context, FilePickerActivity.class);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, true);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);
        i.putExtra(FilePickerActivity.EXTRA_START_PATH, filePath);
        context.startActivityForResult(i, FILE_CODE);
    }

    private void startDirectoryDeleter(Activity context) {
        App.get(context).getAppComponent().inject(this);
        AlertDialog.Builder builderDialog = new AlertDialog.Builder(context);
        CharSequence[] dirs = customDirectories.getFiles();
        boolean[] isChecked = new boolean[dirs.length];
        builderDialog
                .setTitle("Select to delete")
                .setMultiChoiceItems(dirs, isChecked, (dialog, whichButton, isChd) -> {
                })
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    ListView list = ((AlertDialog) dialog).getListView();
                    for (int i = 0; i < list.getCount(); i++) {
                        if (list.isItemChecked(i)) {
                            customDirectories.deleteByPath((String) list.getItemAtPosition(i));
                        }
                    }
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                .show();
    }
}
