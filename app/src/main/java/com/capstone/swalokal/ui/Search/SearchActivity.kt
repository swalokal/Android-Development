package com.capstone.swalokal.ui.Search

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.swalokal.R
import com.capstone.swalokal.ViewModelFactory
import com.capstone.swalokal.databinding.ActivitySearchBinding
import com.capstone.swalokal.ui.ProductImage.ProductImageViewModel
import com.example.storyapp.di.Injection
import com.capstone.swalokal.api.Result

class SearchActivity : AppCompatActivity() {
    private lateinit var searchViewModel: SearchViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()

        // search
        val searchView = binding.searchProductField
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        // rv
        binding.rvItemStore.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        binding.rvItemStore.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvItemStore.addItemDecoration(itemDecoration)

        binding.progressBar.visibility = View.GONE

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query == null || query == "") {
                    return false
                }
                searchViewModel.getSearchUser(query).observe(this@SearchActivity) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                binding.progressBar.visibility = View.GONE
                                val adapter = SearchAdapter()
                                val storeData = result.data.filter {
                                    it.name.contains(query)
                                }
                                adapter.submitList(storeData)
                            }
                            is Result.Error -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(
                                    this@SearchActivity,
                                    "Terjadi kesalahan" + result.err,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })


    }

    private fun setupViewModel() {
        val repository = Injection.provideRepository(this)
        val viewModelFactory = ViewModelFactory(repository)
        searchViewModel = ViewModelProvider(this, viewModelFactory)[SearchViewModel::class.java]

    }
}