package com.example.simpleclient;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;

import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CreatePostDialogFragment extends DialogFragment {

    private Listener mListener;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListener = (Listener) getActivity();
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder
                .setTitle(R.string.create_post)
                .setPositiveButton(R.string.create, (dialog, which) -> {
                    final TextInputEditText postTextInputEditText =
                            getDialog().findViewById(R.id.view_post);
                    final String text = postTextInputEditText.getText().toString();
                    if (TextUtils.isEmpty(text)) {
                        postTextInputEditText.setError(getString(R.string.error_no_text));
                    } else {
                        mListener.onCreatePost(CreatePostDialogFragment.this, text);
                    }
                })
                .setNegativeButton(R.string.cancel, null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setView(R.layout.fragment_post_dialog);
        } else {
            builder.setView(LayoutInflater.from(getActivity()).inflate(R.layout.fragment_post_dialog, null, false));
        }
        return builder.create();
    }

    public interface Listener {

        void onCreatePost(CreatePostDialogFragment fragment, String text);
    }
}
