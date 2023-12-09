package com.dicoding.githubuser.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.database.FavoriteUser
import com.dicoding.githubuser.repository.FavoriteUserRepository

class FavoriteUserViewModel (application: Application) : ViewModel() {
    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)
    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> = mFavoriteUserRepository.getAllFavoriteUsers()
}