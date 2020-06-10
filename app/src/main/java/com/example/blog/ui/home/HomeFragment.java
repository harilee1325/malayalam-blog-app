package com.example.blog.ui.home;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog.R;
import com.example.blog.Utility;
import com.example.blog.ui.blog.BlogDetail;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends Fragment implements IHomeFragment, BlogListAdapter.OnItemClickListener {

    @BindView(R.id.blog_list)
    RecyclerView blogList;
    @BindView(R.id.cancel_cat)
    FloatingActionButton cancelCat;
    @BindView(R.id.filter)
    FloatingActionButton filter;
    private HomeViewModel homeViewModel;
    private BlogListAdapter blogListAdapter;
    private Context mContext;
    private ArrayList<BlogModel.Datum> blogArrayList = new ArrayList<>();
    private Dialog dialog;
    private ArrayList<CatModel.Datum> catList = new ArrayList<>();
    private BottomSheetDialog catDialog;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new Dialog(mContext);
        Utility.getUtilityInstance().showGifPopup(mContext, true, dialog, "");

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel = new HomeViewModel(this);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, root);
        setView();

        return root;
    }


    private void setView() {
        blogList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    homeViewModel.getPageNumber();
                }
            }
        });

        blogListAdapter = new BlogListAdapter(mContext, blogArrayList);
        blogListAdapter.setOnItemClickListener(this);
        blogList.setAdapter(blogListAdapter);

    }

    @Override
    public void getResponse(boolean b) {
        Utility.getUtilityInstance().showGifPopup(mContext, false, dialog, "");
        homeViewModel.getBlogs().observe(getViewLifecycleOwner(), blogModel -> {

            if (blogModel.getData().size() <= 0) {
                return;
            }

            if (!blogArrayList.containsAll(blogModel.getData()))
                blogArrayList.addAll(blogModel.getData());
            blogListAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void getCatResponse() {
        Utility.getUtilityInstance().showGifPopup(mContext, false, dialog, "");
        homeViewModel.catData().observe(getViewLifecycleOwner(), catModel -> {
            if (catModel.getData().size() <= 0) {
                return;
            }
            this.catList.addAll(catModel.getData());
        });
    }

    @Override
    public void showLoader() {

        Utility.getUtilityInstance().showGifPopup(mContext, true, dialog, "");
    }


    private void callCatDialog() {


        catDialog = new BottomSheetDialog(mContext);
        catDialog.setContentView(R.layout.cat_fragment);
        RecyclerView catList = catDialog.findViewById(R.id.cat_list);
        catDialog.show();

        CatAdapter catAdapter = new CatAdapter(this.catList);
        catList.setAdapter(catAdapter);

    }

    @Override
    public void showMessage(String message) {
        Utility.getUtilityInstance().showGifPopup(mContext, false, dialog, "");
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetailsClick(int position) {

        BlogDetail blogDetail = BlogDetail.newInstance(blogArrayList.get(position));
        if (getActivity() != null) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
            ft.replace(R.id.nav_host_fragment, blogDetail, "home");
            ft.addToBackStack(null);
            ft.commitAllowingStateLoss();
        }
    }

    @OnClick({R.id.cancel_cat, R.id.filter})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_cat:
                showLoader();
                homeViewModel.getBlog(1, "");
                cancelCat.setVisibility(View.GONE);
                this.blogArrayList.clear();
                break;
            case R.id.filter:
                callCatDialog();
                break;
        }
    }


    public class CatAdapter extends RecyclerView.Adapter<CatAdapter.ViewHolder> {

        private ArrayList<CatModel.Datum> catModels;

        public CatAdapter(ArrayList<CatModel.Datum> catList) {

            this.catModels = catList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.cat_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.catName.setText(catModels.get(position).getCategoryName());
            holder.catName.setOnClickListener(v -> {
                Utility.getUtilityInstance().showGifPopup(mContext, true, dialog, "");
                homeViewModel.getBlog(1, String.valueOf(catModels.get(position).getId()));
                catDialog.cancel();
                cancelCat.setVisibility(View.VISIBLE);
                blogArrayList.clear();
                blogListAdapter.notifyDataSetChanged();
            });
        }

        @Override
        public int getItemCount() {
            return catModels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView catName;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                catName = itemView.findViewById(R.id.cat_name);
            }
        }
    }
}
