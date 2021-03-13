package com.android.sistemas.whatsapp.helper

import android.util.Base64

class Base64Custom {

    companion object {
        fun codificarBase64(texto : String) : String {
            return Base64.encodeToString(texto.toByteArray(), Base64.DEFAULT)
                .replace(Regex("[\\n|\\r]"), "");
        }
        fun decodificarBase64(texto : String) : String {
            return  Base64.decode(texto, Base64.DEFAULT).toString();
        }
    }
}