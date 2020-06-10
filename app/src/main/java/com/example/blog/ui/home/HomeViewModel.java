package com.example.blog.ui.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.blog.ApiClient;
import com.example.blog.ApiInterface;

import java.util.Calendar;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeViewModel extends ViewModel implements IHomeFragmentViewModel {

    private MutableLiveData<BlogModel> blogList;
    private int number = 1;
    private IHomeFragment iHomeFragment;
    private MutableLiveData<CatModel> catModelMutableLiveData;
    private String id = "";
    private boolean pagination = true;

    public HomeViewModel() {

    }

    public HomeViewModel(IHomeFragment iHomeFragment) {
        blogList = new MutableLiveData<>();
        catModelMutableLiveData = new MutableLiveData<>();
        this.iHomeFragment = iHomeFragment;
        getBlog(number, id);
        getCategory();
    }

    private void getCategory() {
        getCategoryObservable().subscribe(getCategoryObserver());
    }

    public Observable<CatModel> getCategoryObservable() {
        return ApiClient.getRetrofitMain().create(ApiInterface.class)
                .getCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    private DisposableObserver<CatModel> getCategoryObserver() {
        return new DisposableObserver<CatModel>() {

            @Override
            public void onNext(@NonNull CatModel catModel) {
                catModelMutableLiveData.setValue(catModel);
                iHomeFragment.getCatResponse();

            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
                Log.e(TAG, "onError: " + e.getLocalizedMessage());
                iHomeFragment.showMessage("Some error occurred");

            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete: ");
            }
        };
    }

    public LiveData<CatModel> catData() {
        return catModelMutableLiveData;
    }


    void getBlog(int number, String catId) {
        this.number = number;
        id = catId;
        getBlogObservable(number, catId).subscribe(getObserver());

    }

    public Observable<BlogModel> getBlogObservable(int number, String catId) {
        return ApiClient.getRetrofitMain().create(ApiInterface.class)
                .getBlogs(String.valueOf(number), catId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    private DisposableObserver<BlogModel> getObserver() {
        return new DisposableObserver<BlogModel>() {

            @Override
            public void onNext(@NonNull BlogModel blogModel) {
                blogList.setValue(blogModel);
                if (blogModel.getData().size()<=0)
                    pagination = false;
                else
                    pagination = true;
                Log.e(TAG, "onNext: "+id );
                if (id.isEmpty())
                    iHomeFragment.getResponse(false);
                else
                    iHomeFragment.getResponse(true);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
                Log.e(TAG, "onError: " + e.getLocalizedMessage());
                iHomeFragment.showMessage("Some error occurred");

            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete: ");
            }
        };
    }

    public LiveData<BlogModel> getBlogs() {
        return blogList;
    }

    @Override
    public void getPageNumber() {

        if (pagination) {
            number++;
            iHomeFragment.showLoader();
            getBlog(number, id);
        }
    }


}