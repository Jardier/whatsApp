package com.android.sistemas.whatsapp.activity

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.android.sistemas.whatsapp.R
import com.android.sistemas.whatsapp.helper.Permissao

class ConfiguracoesActivity : AppCompatActivity() {

    private lateinit var toolbarPrincipal: Toolbar;
    private lateinit var permissoesNecessarias : ArrayList<String>
    private lateinit var botaoCamera : ImageButton;
    private lateinit var botaoGaleria : ImageButton;

    companion object{
         const val REQUEST_CODE : Int = 1;
         const val REQUEST_CODE_CAMERA : Int = 100;
         const val REQUEST_CODE_GALERIA : Int = 200;
     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracoes)

        toolbarPrincipal = findViewById(R.id.toolbarPrincipal);
        toolbarPrincipal.title = getString(R.string.configuracoes);
        setSupportActionBar(toolbarPrincipal);

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        botaoGaleria = findViewById(R.id.btnGaleria);
        botaoCamera = findViewById(R.id.btnCamera);


        //Permissões Necessárias
        permissoesNecessarias = arrayListOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);

        //Validar Permissões
        Permissao.validarPermissoes(permissoesNecessarias, this, REQUEST_CODE);

        //Ação botao camera
        botaoCamera.setOnClickListener(View.OnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if(intent.resolveActivity(packageManager) != null) {
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        })
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