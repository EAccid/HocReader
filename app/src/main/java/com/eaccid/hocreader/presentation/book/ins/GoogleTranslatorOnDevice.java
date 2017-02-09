package com.eaccid.hocreader.presentation.book.ins;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class GoogleTranslatorOnDevice implements GoogleTranslator {

    @Override
    public boolean openAndTranslate(Context context, String text) {
        boolean succeed = false;
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, text);
            intent.putExtra("key_text_input", "hoc_translation");
            intent.putExtra("key_text_output", "");
            intent.putExtra("key_language_from", "en");
            intent.putExtra("key_language_to", "ru");
            intent.putExtra("key_suggest_translation", "");
            intent.putExtra("key_from_floating_window", false);
            intent.setComponent(new ComponentName(
                    "com.google.android.apps.translate",
                    "com.google.android.apps.translate.TranslateActivity"));
            context.startActivity(intent);
            succeed = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return succeed;
    }
}

