package com.android.sistemas.whatsapp.model

import android.util.Log
import com.android.sistemas.whatsapp.config.FireBaseConfig
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Usuario constructor(@get: Exclude var idUsuario : String = ""
                        , var nome : String = ""
                        , var email : String = ""
                        , @get: Exclude var senha : String = "") {

    companion object {
        const val PATH = "usuarios";
    }

    fun salvar() {
        val firebase : DatabaseReference = FireBaseConfig.reference;

        firebase.child(Usuario.PATH)
            .child(this.idUsuario)
            .setValue(this)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    Log.i(Usuario.PATH, "Dados do usuário salvo com sucesso.")
                } else {
                    throw task.exception!!
                }
            }
    }
}