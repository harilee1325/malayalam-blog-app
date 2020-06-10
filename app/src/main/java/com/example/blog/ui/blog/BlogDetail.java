package com.example.blog.ui.blog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.blog.R;
import com.example.blog.ui.Config;
import com.example.blog.ui.home.BlogModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class BlogDetail extends Fragment {


    private static BlogModel.Datum blogData;
    @BindView(R.id.back_bt)
    ImageView backBt;
    @BindView(R.id.share_blog)
    ImageView shareBlog;
    @BindView(R.id.toolbar_detail)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.blog_title)
    TextView blogTitle;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.body)
    TextView body;
    @BindView(R.id.scrolling_container)
    NestedScrollView scrollingContainer;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;
    @BindView(R.id.blog_image)
    ImageView blogImage;
    private Context mContext;

    public static BlogDetail newInstance(BlogModel.Datum datum) {

        BlogDetail.blogData = datum;
        return new BlogDetail();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.blog_details, container, false);
        ButterKnife.bind(this, view);

        setView();
        return view;
    }

    @SuppressLint("CheckResult")
    private void setView() {

        collapsingToolbar.setContentScrimColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        collapsingToolbar.setTitle("Blog App");
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedToolbar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedToolbar);
        collapsingToolbar.setTitleEnabled(true);

        if (toolbar != null) {
            if (getActivity() != null) {
                ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

            }
        }

        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/malayalam.ttf");
        blogTitle.setTypeface(typeface);
        body.setTypeface(typeface);
        blogTitle.setText(blogData.getTitle());
        date.setText(blogData.getDatetime());
        body.setText(blogData.getDescription());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.placeholder);
        requestOptions.error(R.drawable.placeholder);

        Glide.with(mContext)
                .load(Config.imageBaseUrl + blogData.getImage())
                .apply(requestOptions)
                .into(blogImage);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() != null) {
            Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
            toolbar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getActivity() != null) {
            Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
            toolbar.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.back_bt, R.id.share_blog})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_bt:
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                }
                break;
            case R.id.share_blog:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Blog App");
                    String shareMessage = "\nRead  :" + blogData.getTitle() + "\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                    Log.e(TAG, "onViewClicked: " + e.getLocalizedMessage());
                }
                break;
        }
    }
}
