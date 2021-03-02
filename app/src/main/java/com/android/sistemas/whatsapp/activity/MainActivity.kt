package com.android.sistemas.whatsapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.android.sistemas.whatsapp.R

class MainActivity : AppCompatActivity() {

    private lateinit var toolbarPrincipal: Toolbar;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbarPrincipal = findViewById(R.id.toolbarPrincipal);
        toolbarPrincipal.title = "WhatsApp";
        setSupportActionBar(toolbarPrincipal);

    }
}
