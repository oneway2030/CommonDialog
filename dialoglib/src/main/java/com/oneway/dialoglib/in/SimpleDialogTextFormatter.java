package com.oneway.dialoglib.in;

import android.text.SpannableString;

public class SimpleDialogTextFormatter implements DialogTextFormatter {

    @Override
    public String format(Object item) {
        return new SpannableString(item.toString()).toString();
    }
}
