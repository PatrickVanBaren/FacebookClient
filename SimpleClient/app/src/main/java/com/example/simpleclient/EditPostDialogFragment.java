package com.example.simpleclient;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.example.simplefacebookclient.core.posts.Post;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EditPostDialogFragment extends DialogFragment {

    private EditPostDialogFragment.Listener mListener;
    private Post mPost;

    public void setPost(Post post){
        mPost = post;
    }

    public Post getPost(){
        return this.mPost;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListener = (EditPostDialogFragment.Listener) getActivity();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_post_dialog, null);
        TextInputEditText editText = view.findViewById(R.id.view_post);
        editText.setText(mPost.getMessage());
        builder.setView(view);

        builder
                .setTitle(R.string.edit_post)
                .setPositiveButton(R.string.save_post, (dialog, which) -> {
                    final TextInputEditText postTextInputEditText =
                            getDialog().findViewById(R.id.view_post);
                    final String text = postTextInputEditText.getText().toString();
                    if (TextUtils.isEmpty(text)) {
                        postTextInputEditText.setError(getString(R.string.error_no_text));
                    } else {
                        mListener.onEditPost(EditPostDialogFragment.this, text);
                    }
                })
                .setNegativeButton(R.string.cancel, null);
        return builder.create();
    }

    public interface Listener {
        void onEditPost(EditPostDialogFragment fragment, String text);
    }
}
