package com.dicoding.githubuser.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.data.retrofit.ApiConfig
import com.dicoding.githubuser.data.response.DetailUserResponse
import com.dicoding.githubuser.database.FavoriteUser
import com.dicoding.githubuser.databinding.ActivityDetailBinding
import com.dicoding.githubuser.ui.viewmodel.DetailViewModel
import com.dicoding.githubuser.helper.ViewModelFactory
import com.dicoding.githubuser.ui.adapter.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var viewModel: DetailViewModel
    private lateinit var binding: ActivityDetailBinding
    private lateinit var progressBar: ProgressBar
    private lateinit var username: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this.application)).get(
            DetailViewModel::class.java
        )
        progressBar = binding.progressBar

        username = intent.getStringExtra("username")!!

        if (username != null) {
            showLoading()
            val apiService = ApiConfig.getApiService()
            apiService.getDetailUser(username).enqueue(object : Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    hideLoading()
                    if (response.isSuccessful) {
                        val user = response.body()
                        if (user != null) {
                            displayUserDetails(user)
                            setupViewPager(user.login)
                        } else {
                            showErrorToast()
                        }
                    } else {
                        showErrorToast()
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    hideLoading()
                    showErrorToast()
                }
            })
        } else {
            showErrorToast()
        }
    }

    private fun displayUserDetails(user: DetailUserResponse) {
        binding.apply {
            usernameDetail.text = user.login
            nameDetail.text = user.name
            totalFollowers.text = resources.getString(R.string.data_follower, user.followers)
            totalFollowing.text = resources.getString(R.string.data_following, user.following)

            Glide.with(this@DetailActivity)
                .load(user.avatarUrl)
                .into(imgAvatar)

            viewModel.getFavoriteUserByUsername().observe(this@DetailActivity) { favoriteUsers ->
                var isFavorite = false
                for (favorite in favoriteUsers) {
                    if (favorite.username == username) {
                        isFavorite = true
                        break
                    }
                }
                btnRightBottom.setImageResource(if (isFavorite) R.drawable.ic_favorite_full else R.drawable.ic_favorite)

                btnRightBottom.setOnClickListener {
                    if (isFavorite) {
                        viewModel.DeleteFavoriteUsers(FavoriteUser(user.login, user.avatarUrl))
                    } else {
                        viewModel.InsertFavoriteUsers(FavoriteUser(user.login, user.avatarUrl))
                    }
                }
            }
        }
    }

    private fun showErrorToast() {
        Toast.makeText(this, "Failed to fetch user data", Toast.LENGTH_SHORT).show()
    }

    private fun setupViewPager(username: String) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        val tabLayout = binding.tabs
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Followers"
                1 -> tab.text = "Following"
            }
        }.attach()
    }

    private fun showLoading() {
        progressBar.visibility = ProgressBar.VISIBLE
    }

    private fun hideLoading() {
        progressBar.visibility = ProgressBar.GONE
    }
}

