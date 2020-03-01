package com.example.simpleclient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PostsFragment extends Fragment implements View.OnClickListener {

    private TextView mPostText, mPostDate;
    private Button mDeleteButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.posts_fragment, null);

        mPostText = view.findViewById(R.id.post_text_view);
        mPostDate = view.findViewById(R.id.post_date_view);
        mDeleteButton = view.findViewById(R.id.delete_post_button);

        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
