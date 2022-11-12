package com.example.equipmentstore.mpin_login;

import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.example.equipmentstore.R;

public class GenericKeyEvent implements View.OnKeyListener {
    EditText currentView,previous;

    public GenericKeyEvent(EditText currentView, EditText previous) {
        this.currentView = currentView;
        this.previous = previous;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL
        && currentView.getId() != R.id.otp_edit_box1 && currentView.getText().toString().isEmpty()){
            previous.setText(null);
            previous.requestFocus();
            return true;
        }

        return false;
    }
}
