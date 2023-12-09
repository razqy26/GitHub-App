package com.dicoding.githubuser.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.retrofit.ApiConfig
import com.dicoding.githubuser.data.response.ItemsItem
import com.dicoding.githubuser.database.FavoriteUser
import com.dicoding.githubuser.repository.FavoriteUserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {
    private val githubService = ApiConfig.getApiService()
    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)
    private val _listFollowers = MutableLiveData<List<ItemsItem>>()
    val listFollowers: LiveData<List<ItemsItem>>
        get() = _listFollowers

    private val _listFollowing = MutableLiveData<List<ItemsItem>>()
    val listFollowing: LiveData<List<ItemsItem>>
        get() = _listFollowing

    fun fetchFollowers(username: String) {
        githubService.getFollowers(username).enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                if (response.isSuccessful) {
                    _listFollowers.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
            }
        })
    }

    fun fetchFollowing(username: String) {
        githubService.getFollowing(username).enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                if (response.isSuccessful) {
                    _listFollowing.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
            }
        })
    }
    fun InsertFavoriteUsers(favoriteUser:FavoriteUser){
        mFavoriteUserRepository.insert(favoriteUser)
    }
    fun DeleteFavoriteUsers(favoriteUser: FavoriteUser){
        mFavoriteUserRepository.delete(favoriteUser)
    }
    fun getFavoriteUserByUsername(): LiveData<List<FavoriteUser>> = mFavoriteUserRepository.getFavoriteUserByUsername()
}
