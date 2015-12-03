package com.example.image;

import android.app.Activity;
import android.os.Bundle;

import com.example.image.view.AddPictureGrideView;

public class MainActivity extends Activity {
	AddPictureGrideView gv_add_file;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		gv_add_file = (AddPictureGrideView) findViewById(R.id.gv_add_file);

	}

	@Override
	protected void onResume() {
		super.onResume();
		gv_add_file.refresh();
	}

	@Override
	protected void onDestroy() {
		gv_add_file.onDestory();
		super.onDestroy();
	}
}
