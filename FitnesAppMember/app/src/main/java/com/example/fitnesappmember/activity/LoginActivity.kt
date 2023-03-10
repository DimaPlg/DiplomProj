package com.example.fitnesappmember.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.fitnesappmember.R
import com.example.fitnesappmember.databinding.ActivityLoginBinding
import com.example.fitnesappmember.databinding.ActivityMainBinding
import com.example.fitnesappmember.global.DB
import com.example.fitnesappmember.manager.SessionManager

class LoginActivity : AppCompatActivity() {
    var db:DB?=null
    var session: SessionManager?=null
    var edtUserName : EditText?=null
    var edtPassword : EditText?=null
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db =DB(this)
        session = SessionManager(this)
        edtUserName = binding.edtUserName
        edtPassword = binding.edtPassword


        binding.btnLogin.setOnClickListener{
            if(validateLogin()){
                getLogin()
            }
        }
        binding.txtForgotPassword.setOnClickListener{

        }
    }
    private fun getLogin() {
        try {

            val sqlQuery ="SELECT * FROM ADMIN WHERE USER_NAME='"+edtUserName?.text.toString().trim()+"' " +
                    "AND PASSWORD ='"+edtPassword?.text.toString().trim()+"'AND ID ='1'"
            db?.fireQuery(sqlQuery)?.use {
                if (it.count>0){
                    session?.setLogin(true)
                    Toast.makeText(this,"Successfully Log In",Toast.LENGTH_LONG).show()
                    val intent = Intent(this,HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    session?.setLogin(false)
                    Toast.makeText(this,"Log In failed",Toast.LENGTH_LONG).show()
                }
            }

        }catch (e:Exception){
            e.printStackTrace()
        }

    }
    private fun validateLogin():Boolean {
        if (edtUserName?.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter User Name", Toast.LENGTH_LONG).show()
            return false
        } else if (edtPassword?.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }
}


