package com.example.equipmentstore.mpin_login;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.equipmentstore.R;

public class GenericTextWatcher implements TextWatcher {

    private final EditText[] editText;
    private View view;

    public GenericTextWatcher(EditText[] editText, View view) {
        this.editText = editText;
        this.view = view;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String text = editable.toString();
        switch (view.getId()) {
            case R.id.otp_edit_box1:
                if (text.length() == 1)
                    editText[1].requestFocus();
                break;
            case R.id.otp_edit_box2:
                if (text.length() == 1)
                    editText[2].requestFocus();
                else if (text.length() == 0)
                    editText[0].requestFocus();
                break;
            case R.id.otp_edit_box3:
                if (text.length() == 1)
                    editText[3].requestFocus();
                else if (text.length() == 0)
                    editText[1].requestFocus();
                break;
            case R.id.otp_edit_box4:
                if (text.length() == 0)
                    editText[2].requestFocus();
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }

    }
}
