package com.example.blog.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.blog.R;
import com.example.blog.Utility;
import com.example.blog.ui.Config;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

class BlogListAdapter extends RecyclerView.Adapter<BlogListAdapter.ViewHolder> {


    private static final String TAG = "List Adapter";
    private Context mContext;
    private ArrayList<BlogModel.Datum> blogArrayList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onDetailsClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public BlogListAdapter(Context mContext, ArrayList<BlogModel.Datum> blogArrayList) {


        this.mContext = mContext;
        this.blogArrayList = blogArrayList;
        Log.e(TAG, "BlogListAdapter: " + this.blogArrayList.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.blog_card, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.placeholder);
        requestOptions.error(R.drawable.placeholder);

        Glide.with(mContext)
                .load(Config.imageBaseUrl + blogArrayList.get(position).getImage())
                .apply(requestOptions)
                .into(holder.readImage);


        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/malayalam.ttf");
        holder.title.setTypeface(typeface);
        holder.date.setText(blogArrayList.get(position).getDatetime());
        Log.e(TAG, "onBindViewHolder: " + Utility.readAll(blogArrayList.get(position).getTitle()));
        holder.title.setText((blogArrayList.get(position).getTitle()));
    }


    @Override
    public int getItemCount() {
        return blogArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.read_image)
        ImageView readImage;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.blog_card)
        LinearLayout blogCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


            blogCard.setOnClickListener(v->{
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onDetailsClick(position);

                    }
                }
            });

        }
    }
}
