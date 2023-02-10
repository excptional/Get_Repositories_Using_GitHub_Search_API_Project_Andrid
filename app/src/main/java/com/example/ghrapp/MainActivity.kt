package com.example.ghrapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var appViewModel: AppViewModel
    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var loadingShimmer: ShimmerFrameLayout
    private lateinit var recyclerview: RecyclerView
    private lateinit var repoAdapter: RepoAdapter
    private lateinit var searchEditText: TextInputEditText
    private lateinit var searchBtn: ImageButton
    private lateinit var searchView: LinearLayout
    private lateinit var errorText: TextView
    private var query: CharSequence = "app development"
    private var repoItemsArray = arrayListOf<RepoItems>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appViewModel = ViewModelProvider(this)[AppViewModel::class.java]
        refreshLayout = findViewById(R.id.refresh_layout)
        searchBtn = findViewById(R.id.search_btn)
        searchEditText = findViewById(R.id.search_edittext)
        recyclerview = findViewById(R.id.recyclerview)
        searchView = findViewById(R.id.searchview)
        loadingShimmer = findViewById(R.id.loading_shimmer)
        errorText = findViewById(R.id.error_text)

        loadingShimmer.startShimmer()
        loadingShimmer.visibility = View.VISIBLE
        recyclerview.visibility = View.GONE

        repoAdapter = RepoAdapter(this, repoItemsArray)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.setHasFixedSize(true)
        recyclerview.setItemViewCacheSize(20)
        recyclerview.adapter = repoAdapter

        getData("app development")

        refreshLayout.setOnRefreshListener {
            loadingShimmer.startShimmer()
            loadingShimmer.visibility = View.VISIBLE
            recyclerview.visibility = View.GONE
            errorText.visibility = View.GONE
            searchView.visibility = View.VISIBLE
            getData(query)
        }

        searchBtn.setOnClickListener {
            if(searchEditText.text.toString().isNotEmpty()) {
                query = searchEditText.text.toString()
                getData(query)
                searchEditText.text = null
            }
        }
    }

    private fun getData(q: CharSequence) {
        appViewModel.getRepo(q)
        appViewModel.response.observe(this@MainActivity) {
            when(it) {
                is Response.Success -> {
                    appViewModel.repoContentData.observe(this@MainActivity) {
                        if(it.isNotEmpty()) fetchData(it)
                        else Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
                is Response.Failure -> {
                    Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    searchView.visibility = View.GONE
                    recyclerview.visibility = View.GONE
                    loadingShimmer.visibility = View.GONE
                    errorText.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun fetchData(list: ArrayList<RepoItems>) {
        repoItemsArray = arrayListOf()
        for (i in list) {
            repoItemsArray.add(i)
        }
        repoAdapter.updateRepo(repoItemsArray)
        loadingShimmer.clearAnimation()
        loadingShimmer.visibility = View.GONE
        recyclerview.visibility = View.VISIBLE
        refreshLayout.isRefreshing = false
    }

}