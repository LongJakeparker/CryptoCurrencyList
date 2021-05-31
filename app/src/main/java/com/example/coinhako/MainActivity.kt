package com.example.coinhako

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.coinhako.databinding.ActivityMainBinding
import com.example.coinhako.screen.home.HomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().replace(binding.content.id, HomeFragment(), null)
            .commitAllowingStateLoss()
    }
}