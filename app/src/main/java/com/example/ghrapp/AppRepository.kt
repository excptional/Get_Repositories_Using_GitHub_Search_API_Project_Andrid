package com.example.ghrapp

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest

class AppRepository(private val application: Application) {

    private val responseLiveData = MutableLiveData<Response<String>>()
    val response: LiveData<Response<String>>
        get() = responseLiveData

    private val repoContentsLiveData = MutableLiveData<ArrayList<RepoItems>>()
    val repoContents: LiveData<ArrayList<RepoItems>>
        get() = repoContentsLiveData

    fun getRepo(q: CharSequence) {
        val url = "https://api.github.com/search/repositories?q=$q"

        val jsonArrayRequest = JsonObjectRequest(
            Request.Method.GET, url, null, {
                val itemsArray = it.getJSONArray("items")
                val repoArray = arrayListOf<RepoItems>()
                for (i in 0 until itemsArray.length()) {
                    val repoObject = itemsArray.getJSONObject(i)
                    val content = RepoItems(
                        repoObject.getString("name"),
                        repoObject.getString("description"),
                        repoObject.getString("language"),
                        repoObject.getString("watchers"),
                        repoObject.getString("open_issues"),
                        repoObject.getString("visibility"),
                        repoObject.getString("html_url")
                    )
                    repoArray.add(content)
                }
                repoContentsLiveData.postValue(repoArray)
                responseLiveData.postValue(Response.Success())
            }, {
                responseLiveData.postValue(Response.Failure(getErrorMassage(it)))
            })
        MySingleton.getInstance(application.applicationContext).addToRequestQueue(jsonArrayRequest)
    }

    private fun getErrorMassage(e: Exception): String {
        val colonIndex = e.toString().indexOf(":")
        return e.toString().substring(colonIndex + 2)
    }

}