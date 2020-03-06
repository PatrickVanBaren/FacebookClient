package com.example.simpleclient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.simplefacebookclient.core.posts.Post;
import com.example.simplefacebookclient.core.posts.PostsModule;

import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PostsFragment extends Fragment {

    PostsModule postsModule = PostsModule.getInstance();

    private RecyclerView mPostsRecyclerView;
    private PostAdapter mAdapter;

    public PostsFragment () {}

    private final PostsModule.Listener postsListener = new PostsModule.Listener() {

        @Override
        public void onStateChanged(PostsModule.State state) {

        }

        @Override
        public void onPostCreatedSuccessfully(Post post) {
            mAdapter.setPosts(postsModule.getPosts().toArray());
        }

        @Override
        public void onPostCreationFailed(Post post, Throwable t) {
            Toast.makeText(getContext(), "Creating posts failed! " + t.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPostsLoadedSuccessfully(Set<Post> posts) {
            mAdapter.setPosts(postsModule.getPosts().toArray());
        }

        @Override
        public void onPostsLoadFailed(Throwable t) {
            Toast.makeText(getContext(), "Loading posts failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPostDeletedSuccessfully(Post post) {
            mAdapter.setPosts(postsModule.getPosts().toArray());
        }

        @Override
        public void onPostDeletingFailed(Post post, Throwable t) {

        }

        @Override
        public void onPostUpdatedSuccessfully(Post post) {
            mAdapter.setPosts(postsModule.getPosts().toArray());
        }

        @Override
        public void onPostUpdatingFailed(Post post, Throwable t) {

        }
    };

    PostAdapter.ClickListener mPostsClickListener = new PostAdapter.ClickListener() {
        @Override
        public void onPostClick(int position) {
            Post editPost = (Post)postsModule.getPosts().toArray()[position];
            EditPostDialogFragment editPostDF = new EditPostDialogFragment();
            editPostDF.setPost(editPost);
            editPostDF.show(getFragmentManager(), "EditPostDialogFragment");
        }

        @Override
        public void onDeletePostButton(int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_message_remove)
                    .setTitle(R.string.dialog_message_remove_title);

            builder.setPositiveButton(R.string.ok, (dialog, id) -> {
                Post removePost = (Post)postsModule.getPosts().toArray()[position];
                postsModule.delete(removePost);
            });
            builder.setNegativeButton(R.string.cancel, (dialog, id) -> {});

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.post_recycler, container, false);
        mAdapter = new PostAdapter(LayoutInflater.from(getContext()));
        mPostsRecyclerView = view.findViewById(R.id.view_post_recycler);
        mPostsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter.setDateFormat(getString(R.string.date_format));
        mAdapter.setOnItemClickListener(mPostsClickListener);
        mPostsRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MainActivity.isAuthorized) {
            postsModule.addListener(postsListener);
            mAdapter.setPosts(postsModule.getPosts().toArray());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        postsModule.removeListener(postsListener);
    }
}
