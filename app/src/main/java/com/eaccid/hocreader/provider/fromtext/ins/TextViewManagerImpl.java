package com.eaccid.hocreader.provider.fromtext.ins;

import android.widget.TextView;

public class TextViewManagerImpl implements TextViewManager {
    @Override
    public String getSelectedText(TextView tv) {
        final int selStart = tv.getSelectionStart();
        final int selEnd = tv.getSelectionEnd();
        int min = Math.max(0, Math.min(selStart, selEnd));
        int max = Math.max(0, Math.max(selStart, selEnd));
        return tv.getText().subSequence(min, max).toString();
    }
}
