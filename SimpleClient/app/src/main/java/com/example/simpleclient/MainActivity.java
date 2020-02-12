package com.example.simpleclient;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.simplefacebookclient.core.AuthModule;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        CreatePostDialogFragment.Listener {

    private final AuthModule mAuthModule = AuthModule.getInstance();
    private final LoginManager mLoginManager = LoginManager.getInstance();
    private final CallbackManager mCallbackManager = CallbackManager.Factory.create();
    private final FacebookCallback<LoginResult> mFacebookCallback = new FacebookCallback<LoginResult>() {

        @Override
        public void onSuccess(final LoginResult loginResult) {
            // do nothing
        }

        @Override
        public void onCancel() {
            // do nothing
        }

        @Override
        public void onError(final FacebookException error) {
            // do nothing
        }
    };

    private final AuthModule.Listener mAuthListener = new AuthModule.Listener() {

        @Override
        public void onStateChanged(final AuthModule.State state) {
            switch (state) {
                case NOT_INITIALIZED:
                    mUsernameTextView.setVisibility(View.GONE);
                    mNotAuthenticatedTextView.setVisibility(View.VISIBLE);
                    mLogoutMenuItem.setVisible(false);
                    mProgressBar.setVisibility(View.VISIBLE);
                    mFloatingActionButton.setVisibility(View.GONE);
                    mHeaderView.setBackgroundResource(R.drawable.side_nav_bar);
                    break;

                case NOT_AUTHENTICATED:
                    mUsernameTextView.setVisibility(View.GONE);
                    mNotAuthenticatedTextView.setVisibility(View.VISIBLE);
                    mLogoutMenuItem.setVisible(false);
                    mProgressBar.setVisibility(View.GONE);
                    mFloatingActionButton.setVisibility(View.GONE);
                    mHeaderView.setBackgroundResource(R.drawable.side_nav_bar);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.view_content_container, new LoginFragment())
                            .commit();
                    break;

                case AUTHENTICATED:
                    mUsernameTextView.setVisibility(View.VISIBLE);
                    mNotAuthenticatedTextView.setVisibility(View.GONE);
                    mLogoutMenuItem.setVisible(true);
                    mProgressBar.setVisibility(View.GONE);
                    mFloatingActionButton.setVisibility(View.VISIBLE);
                    mHeaderView.setBackgroundResource(R.drawable.side_nav_bar_auth);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.view_content_container, new PostsFragment())
                            .commit();
                    break;
            }
        }
    };

    private DrawerLayout mDrawerLayout;
    private TextView mUsernameTextView;
    private TextView mNotAuthenticatedTextView;
    private MenuItem mLogoutMenuItem;
    private ProgressBar mProgressBar;
    private ProfileTracker mProfileTracker;
    private FloatingActionButton mFloatingActionButton;
    private View mHeaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.view_drawer_layout);
        mProgressBar = findViewById(R.id.view_progress_bar);

        final Toolbar toolbar = findViewById(R.id.view_toolbar);
        setSupportActionBar(toolbar);

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mFloatingActionButton = findViewById(R.id.view_fab);
        mFloatingActionButton.setOnClickListener(view ->
                new CreatePostDialogFragment().show(getSupportFragmentManager(),"CreatePostDialogFragment"));

        final NavigationView navigationView = findViewById(R.id.view_navigation);
        navigationView.setNavigationItemSelectedListener(this);

        mHeaderView = navigationView.getHeaderView(0);
        mUsernameTextView = mHeaderView.findViewById(R.id.view_username);
        mNotAuthenticatedTextView = mHeaderView.findViewById(R.id.view_not_authenticated);
        mLogoutMenuItem = navigationView.getMenu().findItem(R.id.nav_logout);

        mLoginManager.registerCallback(mCallbackManager, mFacebookCallback);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        mAuthModule.addListener(mAuthListener);

        mProfileTracker = new ProfileTracker() {

            @Override
            protected void onCurrentProfileChanged(final Profile oldProfile, final Profile currentProfile) {
                onProfileUpdate();
            }
        };
        onProfileUpdate();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mAuthModule.removeListener(mAuthListener);
        mProfileTracker.stopTracking();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        final int id = item.getItemId();

        if (id == R.id.nav_logout) mAuthModule.logout();

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("SetTextI18n")
    private void onProfileUpdate() {
        final Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            mUsernameTextView.setText(profile.getFirstName() + " " + profile.getLastName());
        }
    }

    @Override
    public void onCreatePost(final CreatePostDialogFragment fragment, final String text) {
        // do nothing
    }
}

//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
//        loginButton = findViewById(R.id.login_button);
//
//        callbackManager = CallbackManager.Factory.create();
//
//        LoginManager.getInstance().registerCallback(callbackManager,
//                new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        // App code
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        // App code
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception) {
//                        // App code
//                    }
//                });
//
//        loginButton.setReadPermissions("email");
//        // If using in a fragment
//        loginButton.setFragment(this);
//
//        // Callback registration
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                // App code
//            }
//
//            @Override
//            public void onCancel() {
//                // App code
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                // App code
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));  - для onClick проверка статуса входа
    //AccessToken accessToken = AccessToken.getCurrentAccessToken();
    //boolean isLoggedIn = accessToken != null && !accessToken.isExpired(); - просто проверка (в onCreateActivity?)
//}
