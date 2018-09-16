package com.crossover.crosschat.android.utils;

import android.widget.EditText;

import com.crossover.crosschat.android.R;

/**
 * Created by Mahmoud Abdurrahman (mahmoud.abdurrahman@crossover.com) on 2/10/18.
 */
public class ValidationUtils {

    public static boolean isValidString(EditText view) {
        return isValidString(view, view.getContext().getString(R.string.error_required));
    }

    public static boolean isValidString(EditText view, String error) {
        boolean isValid = true;
        String str = view.getText().toString();
        if (str.trim().length() == 0) {
            isValid = false;
            view.setError(error);
        } else {
            view.setError(null);
        }
        return isValid;
    }
}
