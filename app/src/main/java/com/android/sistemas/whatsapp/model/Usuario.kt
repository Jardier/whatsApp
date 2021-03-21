package com.android.sistemas.whatsapp.model

import android.util.Log
import com.android.sistemas.whatsapp.config.FireBaseConfig
import com.android.sistemas.whatsapp.helper.Constantes
import com.android.sistemas.whatsapp.helper.UsuarioFireBase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlin.collections.HashMap

@IgnoreExtraProperties
class Usuario constructor(@get: Exclude var idUsuario : String = ""
                        , var nome : String = ""
                        , var email : String = ""
                        , @get: Exclude var senha : String = ""
                        , var foto : String = "") {


    fun salvar() {
        val firebase : DatabaseReference = FireBaseConfig.reference;

        firebase.child(Constantes.PATH_USUARIOS)
            .child(this.idUsuario)
            .setValue(this)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    Log.d(Constantes.PATH_USUARIOS, "Dados do usuário salvo com sucesso.")
                } else {
                    throw task.exception!!
                }
            }
    }
    fun atualizar() {
        val identificadorUsuario = UsuarioFireBase.getIdentificadorUsuario();
        val firebase = FireBaseConfig.reference;
        val usuarioRef = firebase.child(Constantes.PATH_USUARIOS)
                                                  .child(identificadorUsuario);

        usuarioRef.updateChildren(converterParaMap());
    }

    private fun  converterParaMap() : Map<String, String> {
        val usuarioMap : HashMap<String, String> = HashMap<String,String>();
        usuarioMap.put("nome", this.nome);
        usuarioMap.put("email", this.email);
        usuarioMap.put("foto", this.foto);

        return usuarioMap;
    }
}