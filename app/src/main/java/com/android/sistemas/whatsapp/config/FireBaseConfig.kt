package com.android.sistemas.whatsapp.config

import com.android.sistemas.whatsapp.config.FireBaseConfig.Companion.reference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

/**
 * Padrão Singloton
 */
class FireBaseConfig private constructor(){

    private object FIREBASE {
        val AUTENTICACAO = FirebaseAuth.getInstance();
        val REFERENCE = FirebaseDatabase.getInstance().reference;
        val STORAGE = FirebaseStorage.getInstance().reference;
    }

    companion object {
        val autenticacao: FirebaseAuth by lazy { FIREBASE.AUTENTICACAO };
        val reference : DatabaseReference by lazy { FIREBASE.REFERENCE };
        val storage : StorageReference by lazy { FIREBASE.STORAGE };
    }
}