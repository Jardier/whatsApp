package com.android.sistemas.whatsapp.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.sistemas.whatsapp.R
import com.android.sistemas.whatsapp.model.Usuario
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView


class ContatosAdapter(val contatos : List<Usuario>) : RecyclerView.Adapter<ContatosAdapter.MyViewHolder>() {

    private lateinit var context: Context;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context;
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_contatos, parent, false);

        return MyViewHolder(view);
    }

    override fun getItemCount(): Int {
        return contatos.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val usuario = contatos[position];
        holder.nome.text = usuario.nome;
        holder.email.text = usuario.email;

        if(!TextUtils.isEmpty(usuario.foto)) {
            val uri = usuario.foto;
            Glide.with(context)
                .load(uri)
                .into(holder.foto);
        } else {
            holder.foto.setImageResource(R.drawable.padrao);
        }

    }


    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foto : CircleImageView = itemView.findViewById(R.id.imFotoContato);
        val nome : TextView = itemView.findViewById(R.id.tvNomeContato);
        val email : TextView = itemView.findViewById(R.id.tvEmailContato);

    }
}