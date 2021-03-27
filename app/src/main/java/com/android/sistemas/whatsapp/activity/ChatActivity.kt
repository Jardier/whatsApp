package com.android.sistemas.whatsapp.activity

import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.sistemas.whatsapp.R
import com.android.sistemas.whatsapp.model.Usuario
import com.bumptech.glide.Glide
import com.bumptech.glide.util.Util
import de.hdodenhof.circleimageview.CircleImageView


class ChatActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar;
    private lateinit var fotoContato : CircleImageView;
    private lateinit var nomeContato : TextView;
    private lateinit var contato : Usuario;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        toolbar = findViewById(R.id.toolbar);
        toolbar.title = "";
        setSupportActionBar(toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        fotoContato =  findViewById(R.id.civFotoContatoChat);
        nomeContato = findViewById(R.id.txvNomeContatoChat);

        val bundle : Bundle = intent.extras!!;

        if(bundle != null) {
            contato = bundle.getSerializable("CONTATO") as Usuario;
            nomeContato.setText(contato.nome);

            val foto = contato.foto;
            if(!TextUtils.isEmpty(foto)) {
               Glide.with(this)
                   .load(foto)
                   .into(fotoContato)
            } else {
                fotoContato.setImageResource(R.drawable.padrao);
            }

        }

    }
}


