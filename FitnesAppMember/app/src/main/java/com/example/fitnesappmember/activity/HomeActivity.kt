package com.example.fitnesappmember.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fitnesappmember.R
import com.example.fitnesappmember.databinding.ActivityHomeBinding
import com.example.fitnesappmember.databinding.ActivityMainBinding

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}