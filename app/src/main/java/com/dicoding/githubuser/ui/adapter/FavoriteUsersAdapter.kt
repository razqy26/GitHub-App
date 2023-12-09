package com.dicoding.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuser.database.FavoriteUser
import com.dicoding.githubuser.databinding.ItemUsersBinding
import com.dicoding.githubuser.helper.FavoriteUserDiffCallback

class FavoriteUsersAdapter(private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<FavoriteUsersAdapter.FavoriteUserViewHolder>() {

    private val listFavoriteUsers = ArrayList<FavoriteUser>()
    fun setListFavoriteUsers(listFavoriteUsers: List<FavoriteUser>) {
        val diffCallback = FavoriteUserDiffCallback(this.listFavoriteUsers, listFavoriteUsers)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavoriteUsers.clear()
        this.listFavoriteUsers.addAll(listFavoriteUsers)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteUserViewHolder {
        val binding = ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteUserViewHolder, position: Int) {
        val favoriteUser = listFavoriteUsers[position]
        holder.bind(favoriteUser, onItemClick)
    }

    override fun getItemCount(): Int {
        return listFavoriteUsers.size
    }

    inner class FavoriteUserViewHolder(private val binding: ItemUsersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteUser: FavoriteUser,onItemClick: (String) -> Unit) {
            with(binding) {
                tvUserName.text = favoriteUser.username
                Glide.with(binding.imgAvatar.context)
                    .load(favoriteUser.avatarUrl)
                    .into(binding.imgAvatar)
                binding.root.setOnClickListener {
                    onItemClick(favoriteUser.username)
                }
            }
        }
    }
}