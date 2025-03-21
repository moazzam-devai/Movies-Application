package com.example.a2by3_android.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding
import com.example.moviesapplication.base.BaseRepository
import com.example.moviesapplication.base.ViewModelFactory

abstract class BaseActivity<B : ViewBinding , R : BaseRepository> : AppCompatActivity() {

    protected lateinit var binding: B
    protected lateinit var factory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayout())
        factory = ViewModelFactory(getRepository())
        onPostInit()
    }

    abstract fun getLayout(): Int

    abstract fun onPostInit()

    abstract fun getRepository(): R
}