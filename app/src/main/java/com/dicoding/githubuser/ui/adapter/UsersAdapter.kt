package com.dicoding.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuser.data.response.ItemsItem
import com.dicoding.githubuser.databinding.ItemUsersBinding

class UsersAdapter(private var userList: List<ItemsItem>, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    class UserViewHolder(private val binding: ItemUsersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem, onItemClick: (String) -> Unit) {
            binding.tvUserName.text = user.login
            Glide.with(binding.imgAvatar.context)
                .load(user.avatarUrl)
                .into(binding.imgAvatar)

            binding.root.setOnClickListener {
                onItemClick(user.login)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user, onItemClick)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun updateData(newList: List<ItemsItem>) {
        userList = newList
        notifyDataSetChanged()
    }
}
