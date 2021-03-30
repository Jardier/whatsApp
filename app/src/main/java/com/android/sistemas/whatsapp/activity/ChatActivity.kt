package com.android.sistemas.whatsapp.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.sistemas.whatsapp.R
import com.android.sistemas.whatsapp.adapter.MensagensAdapter
import com.android.sistemas.whatsapp.config.FireBaseConfig
import com.android.sistemas.whatsapp.helper.Base64Custom
import com.android.sistemas.whatsapp.helper.Constantes
import com.android.sistemas.whatsapp.helper.MessageCustom
import com.android.sistemas.whatsapp.helper.UsuarioFireBase
import com.android.sistemas.whatsapp.model.Mensagem
import com.android.sistemas.whatsapp.model.Usuario
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.collections.ArrayList


class ChatActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar;
    private lateinit var fotoContato : CircleImageView;
    private lateinit var nomeContato : TextView;
    private lateinit var textoMensagem : EditText;
    private lateinit var btnEnviar : FloatingActionButton;
    private lateinit var recyclerViewMensagens : RecyclerView

    private lateinit var usuarioDestino : Usuario;
    private lateinit var idUsuarioLogado : String;
    private lateinit var listaMensagens: ArrayList<Mensagem>;

    private lateinit var mensagemRef : DatabaseReference;
    private lateinit var mensagensEventListener : ChildEventListener;


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
        recyclerViewMensagens = findViewById(R.id.recyclerViewMensagens);

        idUsuarioLogado = UsuarioFireBase.getIdentificadorUsuario();
        mensagemRef = FireBaseConfig.reference;
        listaMensagens = ArrayList();



        //Recuperando contato selecionado
        val bundle : Bundle = intent.extras!!;

        if(bundle != null) {
            usuarioDestino = bundle.getSerializable("CONTATO") as Usuario;
            usuarioDestino.idUsuario = Base64Custom.codificarBase64(usuarioDestino.email);

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

        //criar o adpater
        val adapter = MensagensAdapter(listaMensagens);
        //configurar o adpater
        recyclerViewMensagens.layoutManager = LinearLayoutManager(this);
        recyclerViewMensagens.setHasFixedSize(true);
        recyclerViewMensagens.addItemDecoration((DividerItemDecoration(this, 0)));
        recyclerViewMensagens.adapter = adapter;


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

    override fun onStart() {
        super.onStart()
        recuperarMensagens();
    }

    override fun onStop() {
        super.onStop()
        mensagemRef.removeEventListener(mensagensEventListener);
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

    private fun recuperarMensagens() {
        mensagensEventListener = mensagemRef.child(Constantes.PATH_MENSAGENS)
            .child(idUsuarioLogado)
            .child(usuarioDestino.idUsuario)
            .addChildEventListener(object: ChildEventListener{

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val mensagem = snapshot.getValue<Mensagem>(Mensagem::class.java);
                listaMensagens.add(mensagem!!);
                recyclerViewMensagens.adapter?.notifyDataSetChanged();
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }



            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

        })
    }
}


