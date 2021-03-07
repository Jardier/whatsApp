package com.android.sistemas.whatsapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.sistemas.whatsapp.R
import com.android.sistemas.whatsapp.config.FireBaseConfig
import com.android.sistemas.whatsapp.helper.MessageCustom
import com.android.sistemas.whatsapp.model.Usuario
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    private lateinit var editTextEmail : EditText;
    private lateinit var editTextSenha: EditText;
    private lateinit var buttonLogar: Button;

    private lateinit var autenticacao : FirebaseAuth;
    private lateinit var usuario: Usuario;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextEmail =  findViewById(R.id.tieEmail);
        editTextSenha = findViewById(R.id.tieSenha);
        buttonLogar = findViewById(R.id.bntLogar);

        autenticacao = FireBaseConfig.autenticacao;

        editTextEmail.requestFocus();

        //Ação do botão logar
        buttonLogar.setOnClickListener {
            if(formularioValido()) {
                autenticacao.signInWithEmailAndPassword(usuario.email, usuario.senha)
                    .addOnCompleteListener{task: Task<AuthResult> ->
                        if(task.isSuccessful) {
                            exibirTelaPrincipal();
                        } else {
                            var excecao : String;
                            try {
                                throw task.exception!!;
                            } catch (e: FirebaseAuthInvalidUserException) {
                                excecao = "Usuário não está cadastrado!";
                            } catch (e : FirebaseAuthInvalidCredentialsException) {
                                excecao = "E-mail e/ou senha não correspondem a um usuário cadastrado!";
                            } catch (e : Exception) {
                                excecao = "Ocorreu um erro ao logar o usuário. Erro: ${e.message}";
                            }
                            Toast.makeText(this, excecao, Toast.LENGTH_LONG).show();
                        }
                    }
            }
        }
    }

    //Sobrescrevendo o Método para verificar se existe um usuário Logado
    override fun onStart() {
        super.onStart();
        verificarUsuarioLogado();
    }

    fun exibirTelaCadastro(view : View) {
        val intent = Intent(this, CadastroActivity::class.java);
        startActivity(intent);
    }

    private fun formularioValido() : Boolean {
        val email : String = editTextEmail.text.toString().trim();
        val senha : String = editTextSenha.text.toString().trim();

        if(TextUtils.isEmpty(email)) {
            MessageCustom.error(this, editTextEmail, "O e-mail é obrigatório");
            return false;
        } else if(TextUtils.isEmpty(senha)) {
            MessageCustom.error(this, editTextSenha, "A senha é obriogatória");
            return false;
        }
        usuario = Usuario(email = email, senha = senha);
        return true;
    }

    private fun exibirTelaPrincipal() {
        val intent = Intent(this, MainActivity::class.java);
        startActivity(intent);
    }

    private fun verificarUsuarioLogado() {
        if(autenticacao.currentUser != null) {
            exibirTelaPrincipal();
        }
    }
}