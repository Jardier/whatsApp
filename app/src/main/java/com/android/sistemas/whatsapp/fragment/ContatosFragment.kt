package com.android.sistemas.whatsapp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.sistemas.whatsapp.R
import com.android.sistemas.whatsapp.activity.ChatActivity
import com.android.sistemas.whatsapp.adapter.ContatosAdapter
import com.android.sistemas.whatsapp.config.FireBaseConfig
import com.android.sistemas.whatsapp.helper.Constantes
import com.android.sistemas.whatsapp.helper.RecyclerItemClickListener
import com.android.sistemas.whatsapp.helper.UsuarioFireBase
import com.android.sistemas.whatsapp.model.Usuario
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

import kotlin.collections.ArrayList

class ContatosFragment : Fragment() {

    private lateinit var recyclerViewListaContatos: RecyclerView;
    private lateinit var listaContatos : ArrayList<Usuario>;

    private lateinit var usuarioRef : DatabaseReference;
    private lateinit var contatosEventListener : ValueEventListener;
    private lateinit var usuarioLogado : FirebaseUser;
    private lateinit var contatoSelecionado : Usuario;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contatos, container, false);

        recyclerViewListaContatos = view.findViewById(R.id.recyclerViewListaContatos);
        listaContatos = ArrayList();
        usuarioRef = FireBaseConfig.reference.child(Constantes.PATH_USUARIOS);
        usuarioLogado = UsuarioFireBase.getUsuarioAtual()!!;

        //criar um adapter
        val adapter = ContatosAdapter(listaContatos);

        //configurar o recyclerView
        recyclerViewListaContatos.layoutManager = LinearLayoutManager(activity);
        recyclerViewListaContatos.setHasFixedSize(true);
        recyclerViewListaContatos.addItemDecoration((DividerItemDecoration(activity, LinearLayout.VERTICAL)));
        recyclerViewListaContatos.adapter = adapter;

        //Configurar evento de click no recyclerView
        recyclerViewListaContatos.addOnItemTouchListener(RecyclerItemClickListener(activity, recyclerViewListaContatos, object : RecyclerItemClickListener.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                //abrir activity de chat
                contatoSelecionado = listaContatos.get(position);

                val intent = Intent(activity, ChatActivity::class.java);
                intent.putExtra("CONTATO", contatoSelecionado);
                startActivity(intent);
            }

            override fun onItemLongClick(view: View, position: Int) {
                TODO("Not yet implemented")
            }

        }))

        return view;
    }

    override fun onStart() {
        super.onStart()
        recuperarContatos();
    }

    override fun onStop() {
        super.onStop()
        usuarioRef.removeEventListener(contatosEventListener);
    }


    private fun recuperarContatos() {
        contatosEventListener = usuarioRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listaContatos.clear(); //garantir que a lista esteja vazia

                dataSnapshot.children.forEach{
                    var usuario = it.getValue<Usuario>(Usuario::class.java)!!

                    //Removendo o usuário logado da lista de contatos
                    if(!usuario.email.equals(usuarioLogado.email)) {
                        listaContatos.add(usuario);
                    }
                }
                recyclerViewListaContatos.adapter!!.notifyDataSetChanged();
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("ERROR", databaseError.message);
            }
        })
    }


}

