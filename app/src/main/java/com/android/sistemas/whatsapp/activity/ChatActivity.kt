package com.android.sistemas.whatsapp.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.sistemas.whatsapp.R
import com.android.sistemas.whatsapp.config.FireBaseConfig
import com.android.sistemas.whatsapp.helper.Base64Custom
import com.android.sistemas.whatsapp.helper.Constantes
import com.android.sistemas.whatsapp.helper.MessageCustom
import com.android.sistemas.whatsapp.helper.UsuarioFireBase
import com.android.sistemas.whatsapp.model.Mensagem
import com.android.sistemas.whatsapp.model.Usuario
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import de.hdodenhof.circleimageview.CircleImageView


class ChatActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar;
    private lateinit var fotoContato : CircleImageView;
    private lateinit var nomeContato : TextView;
    private lateinit var textoMensagem : EditText;
    private lateinit var btnEnviar : FloatingActionButton;

    private lateinit var usuarioDestino : Usuario;
    private lateinit var idUsuarioLogado : String;
    private lateinit var mensagemRef : DatabaseReference;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        toolbar = findViewById(R.id.toolbar);
        toolbar.title = "";
        setSupportActionBar(toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        fotoContato =  findViewById(R.id.civFotoContatoChat);
        nomeContato = findViewById(R.id.txvNomeContatoChat);
        textoMensagem = findViewById(R.id.etMensagem);
        btnEnviar = findViewById(R.id.fabEnviar);

        idUsuarioLogado = UsuarioFireBase.getIdentificadorUsuario();
        mensagemRef = FireBaseConfig.reference;



        //Recuperando contato selecionado
        val bundle : Bundle = intent.extras!!;

        if(bundle != null) {
            usuarioDestino = bundle.getSerializable("CONTATO") as Usuario;
            nomeContato.setText(usuarioDestino.nome);
            val foto = usuarioDestino.foto;

            if(!TextUtils.isEmpty(foto)) {
               Glide.with(this)
                   .load(foto)
                   .into(fotoContato)
            } else {
                fotoContato.setImageResource(R.drawable.padrao);
            }

        }

        //Ação do botão Enviar mensagem
        btnEnviar.setOnClickListener(View.OnClickListener { view ->
            val textoMensagem = textoMensagem.text.toString();

            if(!TextUtils.isEmpty(textoMensagem)) {
                //salvar mensagem
                val mensagem = Mensagem(idUsuarioLogado, mensage = textoMensagem);
                salvarMensagem(Base64Custom.codificarBase64(usuarioDestino.email), mensagem);

            } else {
                MessageCustom.messagem("Favor digitar uma mensagem", this);
            }
        })

    }

    private fun salvarMensagem(idUsuarioDestino : String, mensagem : Mensagem) {
        val dataBase = FireBaseConfig.reference;
        mensagemRef.child(Constantes.PATH_MENSAGENS)
            .child(mensagem.idUsuario)
            .child(idUsuarioDestino)
            .push()
            .setValue(mensagem)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    textoMensagem.text.clear();
                } else {
                    var excecao: String = task.exception.toString();
                    MessageCustom.messagem("Ocorreu um erro ao enviar a mensagem: ${excecao}", this);
                }
            }
    }
}


