package com.android.sistemas.whatsapp.fragment

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
import com.android.sistemas.whatsapp.adapter.ContatosAdapter
import com.android.sistemas.whatsapp.config.FireBaseConfig
import com.android.sistemas.whatsapp.helper.Constantes
import com.android.sistemas.whatsapp.model.Usuario
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class ContatosFragment : Fragment() {

    private lateinit var recyclerViewListaContatos: RecyclerView;
    private lateinit var listaContatos : ArrayList<Usuario>;

    private lateinit var usuarioRef : DatabaseReference;
    private lateinit var contatosEventListener : ValueEventListener


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
        usuarioRef = FireBaseConfig.reference.child(Constantes.PATH_USUARIOS)

        //criar um adapter
        val adapter = ContatosAdapter(listaContatos);

        //configurar o recyclerView
        recyclerViewListaContatos.layoutManager = LinearLayoutManager(activity);
        recyclerViewListaContatos.setHasFixedSize(true);
        recyclerViewListaContatos.addItemDecoration((DividerItemDecoration(activity, LinearLayout.VERTICAL)));
        recyclerViewListaContatos.adapter = adapter;

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
                dataSnapshot.children.forEach{
                    var usuario = it.getValue<Usuario>(Usuario::class.java)!!
                    listaContatos.add(usuario);
                }
                recyclerViewListaContatos.adapter!!.notifyDataSetChanged();
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("ERROR", databaseError.message);
            }
        })
    }


}

