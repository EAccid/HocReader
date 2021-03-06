package com.eaccid.hocreader.presentation.main.serchadapter;

import android.content.Intent;
import android.view.View;

import com.eaccid.hocreader.presentation.pager.PagerActivity;

import java.io.File;

public class OnChildItemClickListener implements View.OnClickListener{
    private final File file;

    public OnChildItemClickListener(File file) {
        this.file = file;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), PagerActivity.class);
        intent.putExtra("fileName", file.getName());
        intent.putExtra("filePath", file.getPath());
        view.getContext().startActivity(intent);
    }
}
