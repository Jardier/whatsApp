package com.android.sistemas.whatsapp.activity

import android.Manifest
import android.app.Activity

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder


import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import com.android.sistemas.whatsapp.R
import com.android.sistemas.whatsapp.helper.Constantes
import com.android.sistemas.whatsapp.helper.Permissao
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_configuracoes.*
import java.lang.Exception

class ConfiguracoesActivity : AppCompatActivity() {

    private lateinit var toolbarPrincipal: Toolbar;
    private lateinit var permissoesNecessarias : ArrayList<String>
    private lateinit var botaoCamera : ImageButton;
    private lateinit var botaoGaleria : ImageButton;
    private lateinit var imagemPerfil : CircleImageView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracoes)

        toolbarPrincipal = findViewById(R.id.toolbarPrincipal);
        toolbarPrincipal.title = getString(R.string.configuracoes);
        setSupportActionBar(toolbarPrincipal);

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        botaoGaleria = findViewById(R.id.btnGaleria);
        botaoCamera = findViewById(R.id.btnCamera);
        imagemPerfil = findViewById(R.id.civFotoPerfil);


        //Permissões Necessárias
        permissoesNecessarias = arrayListOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);

        //Validar Permissões
        Permissao.validarPermissoes(permissoesNecessarias, this, Constantes.REQUEST_CODE);

        //Ação botão câmera
        botaoCamera.setOnClickListener(View.OnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if(intent.resolveActivity(packageManager) != null) {
                startActivityForResult(intent, Constantes.REQUEST_CODE_CAMERA);
            }
        });

        //Ação botão galeria
        btnGaleria.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            if(intent.resolveActivity(packageManager) != null) {
                startActivityForResult(intent, Constantes.REQUEST_CODE_GALERIA);
            }
        })
    }

    //Método acionado quando selecionado uma Foto da Galeria ou Câmera
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK) {
            var imagem : Bitmap = BitmapFactory.decodeResource(this.resources, R.drawable.padrao)

            try{
                when(requestCode) {
                    Constantes.REQUEST_CODE_CAMERA -> {
                        imagem = data?.extras?.get("data") as Bitmap
                    }
                    Constantes.REQUEST_CODE_GALERIA -> {
                      val localImagemSelecionada  =  data?.data ;
                      val source = ImageDecoder.createSource(this.contentResolver,localImagemSelecionada!!);
                        imagem = ImageDecoder.decodeBitmap(source);
                    }
                }
                imagemPerfil.setImageBitmap(imagem);



            } catch (e : Exception) {
                Log.e("CONFIGURACOES", e.message);
                e.printStackTrace();
            }
        }
    }

    //Método acionado quando as permissões são concedidas
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