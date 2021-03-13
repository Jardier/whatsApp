package com.android.sistemas.whatsapp.activity

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.android.sistemas.whatsapp.R
import com.android.sistemas.whatsapp.helper.Permissao

class ConfiguracoesActivity : AppCompatActivity() {

    private lateinit var toolbarPrincipal: Toolbar;
    private lateinit var permissoesNecessarias : ArrayList<String>

    companion object{
         const val REQUEST_CODE : Int = 1;
     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracoes)

        toolbarPrincipal = findViewById(R.id.toolbarPrincipal);
        toolbarPrincipal.title = getString(R.string.configuracoes);
        setSupportActionBar(toolbarPrincipal);

        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        //Permissões Necessárias
        permissoesNecessarias = arrayListOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);

        //Validar Permissões
        Permissao.validarPermissoes(permissoesNecessarias, this, REQUEST_CODE);
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        grantResults.forEach { grantResult : Int ->
            if(grantResult == PackageManager.PERMISSION_DENIED) {
                alertaValidacaoPermissao();
            }
        }
    }

    private fun alertaValidacaoPermissao() {
        val builder = AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o App é necessário aceitar as permissões");
        builder.setCancelable(false);

        builder.setPositiveButton(getString(R.string.sim), DialogInterface.OnClickListener{dialog, which ->
              finish();
        });
        val dialog : AlertDialog = builder.create();
        dialog.show();

    }
}