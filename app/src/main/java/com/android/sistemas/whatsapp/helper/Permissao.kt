package com.android.sistemas.whatsapp.helper

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.sistemas.whatsapp.config.FireBaseConfig
import java.util.*
import kotlin.collections.ArrayList


class Permissao {

    companion object{

        fun validarPermissoes(permissoes : ArrayList<String>, activity: Activity, requestCode : Int) : Boolean{

            //Verifica a versão do SDK
            if(Build.VERSION.SDK_INT >= Constantes.SDK_MARSHMALLOW) {
                val listaPermissoes : ArrayList<String> = ArrayList();

                permissoes.forEach {permissao : String ->
                    val temPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                    if(!temPermissao) {
                        listaPermissoes.add(permissao);
                    }
                }
                //Caso a lista esteja vazia, não é necessário solicitar as permissões
                if(listaPermissoes.isEmpty()) return true;

                //Solicitar permissões
                ActivityCompat.requestPermissions(activity, listaPermissoes.toTypedArray(), requestCode);
            }

            return true;
        }
    }
}