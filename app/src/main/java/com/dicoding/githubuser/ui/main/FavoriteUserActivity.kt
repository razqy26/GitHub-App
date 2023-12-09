package com.dicoding.githubuser.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.databinding.ActivityFavoriteUserBinding
import com.dicoding.githubuser.helper.ViewModelFactory
import com.dicoding.githubuser.ui.adapter.FavoriteUsersAdapter
import com.dicoding.githubuser.ui.viewmodel.FavoriteUserViewModel


class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var adapter: FavoriteUsersAdapter
    private lateinit var viewModel : FavoriteUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = FavoriteUsersAdapter() { username ->
            navigateToDetail(username)
        }

        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this.application)).get(
            FavoriteUserViewModel::class.java)
        viewModel.getAllFavoriteUsers().observe(this) { favoritUserList ->
            if (favoritUserList != null) {
                adapter.setListFavoriteUsers(favoritUserList)
            }
        }

        binding?.rvUsers?.layoutManager = LinearLayoutManager(this)
        binding?.rvUsers?.setHasFixedSize(true)
        binding?.rvUsers?.adapter = adapter

    }

    private fun navigateToDetail(username: String) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
    }

}