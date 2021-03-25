package com.android.sistemas.whatsapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu

import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.android.sistemas.whatsapp.R
import com.android.sistemas.whatsapp.config.FireBaseConfig
import com.android.sistemas.whatsapp.fragment.ContatosFragment
import com.android.sistemas.whatsapp.fragment.ConversasFragment
import com.google.firebase.auth.FirebaseAuth
import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems

class MainActivity : AppCompatActivity() {

    private lateinit var toolbarPrincipal: Toolbar;
    private lateinit var smartTabLayout: SmartTabLayout;
    private lateinit var viewPager: ViewPager;

    private lateinit var autenticacao : FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbarPrincipal = findViewById(R.id.toolbarPrincipal);
        toolbarPrincipal.title = "WhatsApp";
        setSupportActionBar(toolbarPrincipal);

        autenticacao = FireBaseConfig.autenticacao;
        smartTabLayout = findViewById(R.id.smartTabLayout);
        viewPager = findViewById(R.id.viewPager);


        //Configurando a SmartTabLayout
        var adapter = FragmentPagerItemAdapter(supportFragmentManager, FragmentPagerItems.with(this)
            .add(R.string.conversas, ConversasFragment::class.java)
            .add(R.string.contatos, ContatosFragment::class.java)
            .create());

        viewPager.adapter = adapter;
        smartTabLayout.setViewPager(viewPager);
    }

    //Inserindo o menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Item do menu selecionado
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.menuPesquisa -> {
                Toast.makeText(this, "Menu Pesquisa", Toast.LENGTH_LONG).show();
            }
            R.id.menuConfiguracoes -> {
                abrirTelaConfiguracoes();

            }
            R.id.menuSair -> {
                deslogarUsuario();
                finish();
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun deslogarUsuario() {
        try {
            autenticacao.signOut();

        } catch (e: Exception) {
            e.printStackTrace();
        }
    }
    private fun abrirTelaConfiguracoes() {
        val intent = Intent(this, ConfiguracoesActivity::class.java);
        startActivity(intent);
    }
}
