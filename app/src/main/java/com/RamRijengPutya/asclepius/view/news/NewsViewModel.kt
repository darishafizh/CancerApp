package com.RamRijengPutya.asclepius.view.news

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.RamRijengPutya.asclepius.data.remote.response.ArticlesItem
import com.RamRijengPutya.asclepius.data.remote.response.NewsResponse
import com.RamRijengPutya.asclepius.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel : ViewModel() {
    private val newsLiveData = MutableLiveData<List<ArticlesItem>>()

    fun getNews() {
        ApiConfig.getApiService().getHealthNews("cancer", "health", "en", "01d6e4dd0f954e4f8971b178c76811df").enqueue(object :
            Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    newsLiveData.postValue(response.body()?.articles?.filterNotNull())
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.e("NewsViewModel", "API call failed: ${t.message}")
            }
        })
    }

    fun getNewsLiveData(): LiveData<List<ArticlesItem>> {
        return newsLiveData
    }
}
