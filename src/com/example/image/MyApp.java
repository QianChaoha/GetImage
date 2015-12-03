package com.example.image;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MyApp extends Application {
@Override
public void onCreate() {
	super.onCreate();
	ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
}
}
