package com.dicoding.githubuser.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.response.ItemsItem

class UserViewModel : ViewModel() {
    var userList: List<ItemsItem> = emptyList()
}