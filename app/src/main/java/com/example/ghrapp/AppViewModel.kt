package com.example.ghrapp

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class AppViewModel(application: Application) :
    AndroidViewModel(application) {

    private val appRepository: AppRepository = AppRepository(application)
    val repoContentData: LiveData<ArrayList<RepoItems>>
        get() = appRepository.repoContents
    val response: LiveData<Response<String>>
        get() = appRepository.response

    fun getRepo(q: CharSequence) {
        appRepository.getRepo(q)
    }

}