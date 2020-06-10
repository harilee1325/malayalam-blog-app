package com.example.blog;

import com.example.blog.ui.home.BlogModel;
import com.example.blog.ui.home.CatModel;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("getblog?limit=5")
    Observable<BlogModel> getBlogs(@Query("pageno") String valueOf,@Query("category_id") String catId);

    @GET("category")
    Observable<CatModel> getCategory();
}
