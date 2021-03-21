package com.android.sistemas.whatsapp.helper

import android.net.Uri
import android.util.Log
import com.android.sistemas.whatsapp.config.FireBaseConfig
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import java.lang.Exception

class UsuarioFireBase {

    companion object{

        fun getIdentificadorUsuario() : String {
            val email  = FireBaseConfig.autenticacao.currentUser?.email.toString();
            return Base64Custom.codificarBase64(email);
        }

        fun getUsuarioAtual() : FirebaseUser? {
            val user : FirebaseUser? = FireBaseConfig.autenticacao.currentUser;
            return user;
        }

        fun setFotoUsuario(uri: Uri?) : Boolean {
            try {
                val user = getUsuarioAtual();
                val userProfileRequest = UserProfileChangeRequest.Builder()
                    .setPhotoUri(uri)
                    .build();

               user!!.updateProfile(userProfileRequest).addOnCompleteListener{task ->
                   if(!task.isSuccessful) {
                       Log.d(Constantes.PATH_PERFIL, "Erro ao atualizar foto de perfil do usuário");
                   }
               }
                return true;

            } catch (e : Exception) {
                e.printStackTrace();
                return false;
            }
        }

        fun setNomeUsuario(nome: String) : Boolean {
            try {
                val user = getUsuarioAtual();
                val userProfileRequest = UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build();

                user!!.updateProfile(userProfileRequest).addOnCompleteListener{task ->
                    if(!task.isSuccessful) {
                        Log.d(Constantes.PATH_PERFIL, "Erro ao atualizar nome de perfil usuário");
                    }
                }
                return true;

            } catch (e : Exception) {
                e.printStackTrace();
                return false;
            }
        }
    }
}