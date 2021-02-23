package com.android.sistemas.whatsapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.sistemas.whatsapp.R
import com.android.sistemas.whatsapp.config.FireBaseConfig
import com.android.sistemas.whatsapp.helper.Base64Custom
import com.android.sistemas.whatsapp.helper.MessageCustom
import com.android.sistemas.whatsapp.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import java.lang.Exception

class CadastroActivity : AppCompatActivity() {

    private lateinit var editTextNome: EditText;
    private lateinit var editTextEmail: EditText;
    private lateinit var editTextSenha: EditText;
    private lateinit var buttonCadastrar: Button;

    private lateinit var autenticacao : FirebaseAuth;
    private lateinit var usuario: Usuario;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        editTextNome = findViewById(R.id.tieNome);
        editTextEmail = findViewById(R.id.tieEmail);
        editTextSenha = findViewById(R.id.tieSenha);
        buttonCadastrar = findViewById(R.id.btnCadastrar);

        editTextNome.requestFocus();

        //Ação do botão cadastrar
        buttonCadastrar.setOnClickListener(View.OnClickListener {

            if(formlarioValido()) {
                autenticacao = FireBaseConfig.autenticacao;

                autenticacao.createUserWithEmailAndPassword(usuario.email, usuario.senha)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            //salvar dados do usuário
                            val idUsuario : String = Base64Custom.codificarBase64(usuario.email);
                            usuario.idUsuario = idUsuario;
                            usuario.salvar();
                            finish();
                        } else {
                            var excecao : String = "";
                            try {
                                throw task.exception!!;
                            } catch (e : FirebaseAuthWeakPasswordException) {
                                excecao = "Digite uma senha mais forte"
                            } catch (e : FirebaseAuthInvalidCredentialsException) {
                                excecao = "Digite um e-mail válido!";
                            } catch (e : FirebaseAuthUserCollisionException) {
                                excecao = "Este e-mail já foi cadastrado!";
                            } catch (e : Exception) {
                                excecao = "Ocorreo o seguinte erro ao cadastrar o usuário: Error - ${e.message}";
                            }
                            Toast.makeText(this, excecao, Toast.LENGTH_LONG).show();
                        }
                    }

            }
        })

    }

    private fun formlarioValido() : Boolean {
        val nome : String = editTextNome.text.toString().trim();
        val email : String = editTextEmail.text.toString().trim();
        val senha : String = editTextSenha.text.toString().trim();

        if(TextUtils.isEmpty(nome)) {
            MessageCustom.error(this, editTextNome, "O nome é obrigatório");
            return false;
        } else if(TextUtils.isEmpty(email)) {
            MessageCustom.error(this, editTextEmail, "O e-mail é obrigatório");
            return false;
        } else if(TextUtils.isEmpty(senha)) {
            MessageCustom.error(this, editTextSenha, "A senha é obrigatória");
            return false;
        }
        usuario =  Usuario(nome = nome, email = email, senha = senha );
        return true;
    }
}