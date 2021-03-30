package com.android.sistemas.whatsapp.adapter

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.sistemas.whatsapp.R
import com.android.sistemas.whatsapp.helper.Constantes
import com.android.sistemas.whatsapp.helper.UsuarioFireBase
import com.android.sistemas.whatsapp.model.Mensagem
import com.bumptech.glide.Glide

class MensagensAdapter(val listaMensagens : List<Mensagem>) : RecyclerView.Adapter<MensagensAdapter.MyViewHolder>() {

    private lateinit var context: Context;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context;
        var view: View? = null;

        if(viewType == Constantes.TIPO_REMETENTE) {
           view = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_mensagem_remetente, parent, false);

        } else if(viewType == Constantes.TIPO_DESTINATARIO) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_mensagem_destinatario, parent, false);
        }
        return MyViewHolder(view!!);
    }

    override fun getItemCount(): Int {
        return listaMensagens.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mensagem = listaMensagens.get(position);
        val msg = mensagem.mensage;
        val imagem = mensagem.imagem;

        if(!TextUtils.isEmpty(imagem)) {
            Glide.with(context)
                .load(Uri.parse(imagem))
                .into(holder.imagem);
            //esconder o texto
            holder.mensagem.visibility = View.GONE;
        } else {
            holder.mensagem.setText(mensagem.mensage);
            holder.imagem.visibility = View.GONE;
        }
    }

    //Verificar qual tipo de Adapter será retornado
    override fun getItemViewType(position: Int): Int {
        val mensagem = listaMensagens.get(position);
        val idUsuarioLogado = UsuarioFireBase.getIdentificadorUsuario();

        if(idUsuarioLogado.equals(mensagem.idUsuario)) {
            return Constantes.TIPO_REMETENTE;
        }
        return Constantes.TIPO_DESTINATARIO;
    }

    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mensagem : TextView = itemView.findViewById(R.id.tvMensagemTexto);
        val imagem : ImageView = itemView.findViewById(R.id.ivMensagemFoto);

    }
}