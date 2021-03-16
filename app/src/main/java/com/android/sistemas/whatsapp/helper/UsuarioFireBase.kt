package com.android.sistemas.whatsapp.helper

import com.android.sistemas.whatsapp.config.FireBaseConfig

class UsuarioFireBase {

    companion object{

        fun identificadorUsuario() : String {
            val email  = FireBaseConfig.autenticacao.currentUser?.email.toString();
            return Base64Custom.codificarBase64(email);
        }
    }
}