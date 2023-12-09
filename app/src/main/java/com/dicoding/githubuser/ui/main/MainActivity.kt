package com.dicoding.githubuser.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.data.retrofit.ApiConfig
import com.dicoding.githubuser.data.response.GithubResponse
import com.dicoding.githubuser.databinding.ActivityMainBinding
import com.dicoding.githubuser.helper.SettingPreferences
import com.dicoding.githubuser.helper.ThemeViewModelFactory
import com.dicoding.githubuser.helper.dataStore
import com.dicoding.githubuser.ui.adapter.UsersAdapter
import com.dicoding.githubuser.ui.viewmodel.ThemeViewModel
import com.dicoding.githubuser.ui.viewmodel.UserViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UsersAdapter
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        adapter = UsersAdapter(userViewModel.userList) { username ->
            navigateToDetail(username)
        }

        binding.rvUsers.adapter = adapter
        binding.rvUsers.layoutManager = LinearLayoutManager(this)

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                showLoading() // Menampilkan ProgressBar saat pencarian dimulai
                performSearch()
                true
            } else {
                false
            }
        }
        binding?.btnFav?.setOnClickListener {
            val intent = Intent(this@MainActivity, FavoriteUserActivity::class.java)
            startActivity(intent)
        }
        binding?.btnSetting?.setOnClickListener {
            val intent = Intent(this@MainActivity, ThemeActivity::class.java)
            startActivity(intent)
        }
        val pref = SettingPreferences.getInstance(application.dataStore)
        val themeViewModel = ViewModelProvider(this, ThemeViewModelFactory(pref)).get(ThemeViewModel::class.java)
        themeViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun performSearch() {
        val query = binding.etSearch.text.toString().trim()
        val apiService = ApiConfig.getApiService()
        apiService.searchGithubUsers(query).enqueue(object : Callback<GithubResponse> {
            override fun onResponse(call: Call<GithubResponse>, response: Response<GithubResponse>) {
                hideLoading()
                if (response.isSuccessful) {
                    val githubResponse = response.body()
                    val users = githubResponse?.items ?: emptyList()
                    userViewModel.userList = users

                    adapter.updateData(users)
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                hideLoading()
            }
        })
    }

    private fun navigateToDetail(username: String) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
    }

    private fun showLoading() {
        binding.progressBar.visibility = ProgressBar.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = ProgressBar.GONE
    }
}
