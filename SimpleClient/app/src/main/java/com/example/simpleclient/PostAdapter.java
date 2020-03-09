package com.example.simpleclient;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.simplefacebookclient.core.posts.Post;

import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private String mDate;
    private Object[] postsArray;
    private ClickListener mClickListener;

    public PostAdapter(LayoutInflater inflater) {
        mInflater = inflater;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostAdapter.ViewHolder(mInflater.inflate(R.layout.post_view, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = (Post) postsArray[position];
        holder.mPostText.setText(post.getMessage());
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.mDate, Locale.US);
        holder.mPostDate.setText(simpleDateFormat.format(post.getCreatedTime()));
    }

    @Override
    public int getItemCount() {
        return postsArray == null ? 0 : postsArray.length;
    }

    public void setPosts(Object[] postsArray) {
        this.postsArray = postsArray;
        notifyDataSetChanged();
    }

    public String getDateFormat() {
        return mDate;
    }

    public void setDateFormat(String mDate) {
        this.mDate = mDate;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        final TextView mPostText, mPostDate;
        final ImageButton mDeletePostButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mDeletePostButton = itemView.findViewById(R.id.delete_post_button);
            mDeletePostButton.setOnClickListener(v -> {
                if (mClickListener != null) mClickListener.onDeletePostButton(getAdapterPosition());
            });

            mPostText = itemView.findViewById(R.id.post_text_view);
            mPostText.setOnClickListener(v -> {
                if (mClickListener != null) mClickListener.onPostClick(getAdapterPosition());
            });

            mPostDate = itemView.findViewById(R.id.post_date_view);

        }
    }

    public interface ClickListener {
        void onPostClick(int position);
        void onDeletePostButton(int position);
    }
}
