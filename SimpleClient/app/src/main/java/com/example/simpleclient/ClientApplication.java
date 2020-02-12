package com.example.simpleclient;

import android.app.Application;

import com.example.simplefacebookclient.core.AuthModule;
import com.example.simplefacebookclient.core.db.DatabaseModule;
import com.example.simplefacebookclient.core.posts.PostsModule;
import com.facebook.FacebookSdk;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ClientApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        PreferencesModule.createInstance(this);
        AuthModule.createInstance();
        FacebookSdk.sdkInitialize(getApplicationContext(), () -> AuthModule.getInstance().init());
        DatabaseModule.createInstance(this);
        PostsModule.createInstance();
    }
}
