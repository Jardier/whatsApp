package com.android.sistemas.whatsapp.helper

import android.content.Context
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.android.sistemas.whatsapp.R

class MessageCustom {

    companion object {
        fun error(context : Context, campo : EditText, mensage : String) {
            val icon  = AppCompatResources.getDrawable(context, R.drawable.ic_error_red_24dp);
            icon?.setBounds(0, 0 , icon.intrinsicWidth, icon.intrinsicHeight);

            campo.setError(mensage, icon);
            campo.requestFocus();
        }
        fun messagem (mensage : String, context: Context) {
            Toast.makeText(context, mensage, Toast.LENGTH_LONG).show();
        }
    }
}