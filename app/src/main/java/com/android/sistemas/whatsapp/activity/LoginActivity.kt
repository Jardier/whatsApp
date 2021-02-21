package com.android.sistemas.whatsapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.android.sistemas.whatsapp.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }





    fun exibirTelaCadastro(view : View) {
        val intent = Intent(this, CadastroActivity::class.java);
        startActivity(intent);
    }
}