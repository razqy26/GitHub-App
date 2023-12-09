package com.dicoding.githubuser.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.data.response.ItemsItem
import com.dicoding.githubuser.databinding.FragmentFollowBinding
import com.dicoding.githubuser.ui.adapter.UsersAdapter
import com.dicoding.githubuser.ui.viewmodel.DetailViewModel

class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(DetailViewModel::class.java)

        arguments?.let {
            val position = it.getInt(ARG_POSITION)
            val username = it.getString(ARG_USERNAME) ?: ""

            val usersList = if (position == 1) {
                viewModel.fetchFollowers(username)
                viewModel.listFollowers
            } else {
                viewModel.fetchFollowing(username)
                viewModel.listFollowing
            }
            val progressBar = binding.progressBarFollowers
            usersList.observe(viewLifecycleOwner) { users ->
                progressBar.visibility = View.GONE
                setupRecyclerView(users)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Panggil refreshData() saat fragment aktif kembali
        refreshData()
    }

    private fun refreshData() {
        arguments?.let {
            val position = it.getInt(ARG_POSITION)
            val username = it.getString(ARG_USERNAME) ?: ""

            val usersList = if (position == 1) {
                viewModel.fetchFollowers(username)
                viewModel.listFollowers
            } else {
                viewModel.fetchFollowing(username)
                viewModel.listFollowing
            }
            val progressBar = binding.progressBarFollowers
            usersList.observe(viewLifecycleOwner) { users ->
                progressBar.visibility = View.GONE
                setupRecyclerView(users)
            }
        }
    }

    private fun setupRecyclerView(users: List<ItemsItem>) {
        val adapter = UsersAdapter(users) { clickedUsername ->
            // Handle item click jika diperlukan
        }
        binding.rvUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsers.adapter = adapter
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
}
