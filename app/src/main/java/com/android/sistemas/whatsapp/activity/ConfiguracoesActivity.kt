package com.android.sistemas.whatsapp.activity

import android.Manifest
import android.app.Activity

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri


import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog

import androidx.appcompat.widget.Toolbar
import com.android.sistemas.whatsapp.R
import com.android.sistemas.whatsapp.config.FireBaseConfig
import com.android.sistemas.whatsapp.helper.Constantes
import com.android.sistemas.whatsapp.helper.MessageCustom
import com.android.sistemas.whatsapp.helper.Permissao
import com.android.sistemas.whatsapp.helper.UsuarioFireBase
import com.android.sistemas.whatsapp.model.Usuario
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_configuracoes.*
import java.io.ByteArrayOutputStream
import java.lang.Exception

class ConfiguracoesActivity : AppCompatActivity() {

    private lateinit var toolbarPrincipal: Toolbar;
    private lateinit var permissoesNecessarias : ArrayList<String>
    private lateinit var botaoCamera : ImageButton;
    private lateinit var botaoGaleria : ImageButton;
    private lateinit var imagemPerfil : CircleImageView;
    private lateinit var perfilNome: EditText
    private lateinit var botaoEditPerfil : ImageView;

    private lateinit var storage : StorageReference;
    private lateinit var usuarioFireBase: FirebaseUser;
    private lateinit var usuarioLogado: Usuario


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
        perfilNome = findViewById(R.id.etvPerfilNome);
        botaoEditPerfil = findViewById(R.id.ivEditPerfil);

        storage = FireBaseConfig.storage;
        usuarioLogado = UsuarioFireBase.getDadosUsuarioLogado();

        //Recuperar o Usuário
        usuarioFireBase = UsuarioFireBase.getUsuarioAtual()!!;
        val uri = usuarioFireBase.photoUrl;

        if(uri != null) {
            try {
                Glide.with(this)
                    .load(uri)
                    .into(imagemPerfil);

            } catch (e : Exception) {
                MessageCustom.messagem("Ocorreu um erro ao carregar imagem do perfil: ${e.message}", this);
                e.printStackTrace();
            }

        } else {
            imagemPerfil.setImageResource(R.drawable.padrao);
        }
        perfilNome.setText(usuarioFireBase.displayName);



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

        //Ação botão Editar Perfil
        botaoEditPerfil.setOnClickListener(View.OnClickListener {
            val nomeUsuario : String =  perfilNome.text.toString();

            if(UsuarioFireBase.setNomeUsuario(nomeUsuario)) {
                usuarioLogado.nome = nomeUsuario;
                usuarioLogado.atualizar();

                MessageCustom.messagem("Perfil do usuário atualizado com sucesso.", this);
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
                try {
                    salvarImagemPerfil(imagem);
                    MessageCustom.messagem("Sucesso ao fazer upload da imagem", this);
                } catch (e : Exception) {
                    MessageCustom.messagem("Erro ao fazer upload da imagem", this)
                }

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
    private fun salvarImagemPerfil(imagem : Bitmap) {
        val baos : ByteArrayOutputStream = ByteArrayOutputStream();
        imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        val dadosImagem = baos.toByteArray();

        val imagemReference = storage.child(Constantes.PATH_IMAGENS)
            .child(Constantes.PATH_PERFIL)
            .child(UsuarioFireBase.getIdentificadorUsuario().plus(".jpeg"));

        val uploadTask: UploadTask = imagemReference.putBytes(dadosImagem);
        uploadTask.addOnFailureListener { exception ->
            Log.e("CONFIGURACOES", exception.message);
            exception.stackTrace;
            throw  exception;
        }.addOnSuccessListener { listener ->
            imagemReference.downloadUrl.addOnCompleteListener{task ->
                val uri : Uri? =  task.result;
                atualizarFotoUsuario(uri);//ao carregar imagem, vamos atualizar no FireBase Perfil
            }
        };

    }

    private fun atualizarFotoUsuario(uri: Uri?) {
        if(UsuarioFireBase.setFotoUsuario(uri)) {
            usuarioLogado.foto = uri.toString();
            usuarioLogado.atualizar();
            MessageCustom.messagem("Sua foto foi alterada.", this);
        }
    }
}