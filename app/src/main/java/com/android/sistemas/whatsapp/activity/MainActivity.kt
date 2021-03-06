package com.android.sistemas.whatsapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.android.sistemas.whatsapp.R
import com.android.sistemas.whatsapp.config.FireBaseConfig
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var toolbarPrincipal: Toolbar;
    private lateinit var autenticacao : FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbarPrincipal = findViewById(R.id.toolbarPrincipal);
        toolbarPrincipal.title = "WhatsApp";
        setSupportActionBar(toolbarPrincipal);

        autenticacao = FireBaseConfig.autenticacao;
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
                Toast.makeText(this, "Menu Configurações", Toast.LENGTH_LONG).show();

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
}
