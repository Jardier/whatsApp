package com.android.sistemas.whatsapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.android.sistemas.whatsapp.R

class ConfiguracoesActivity : AppCompatActivity() {

    private lateinit var toolbarPrincipal: Toolbar;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracoes)

        toolbarPrincipal = findViewById(R.id.toolbarPrincipal);
        toolbarPrincipal.title = getString(R.string.configuracoes);
        setSupportActionBar(toolbarPrincipal);

        supportActionBar?.setDisplayHomeAsUpEnabled(true);

    }
}