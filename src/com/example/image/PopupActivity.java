package com.example.image;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class PopupActivity extends Activity implements OnClickListener {
	private String name, path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_popupwindow_more);

		Button but = (Button) findViewById(R.id.item_popupwindows_camera);
		but.setOnClickListener(this);
		Button but1 = (Button) findViewById(R.id.item_popupwindows_Photo);
		but1.setOnClickListener(this);
		findViewById(R.id.item_popupwindows_cancel).setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		Intent intent;
		switch (arg0.getId()) {
		case R.id.item_popupwindows_Photo:
			intent = new Intent(PopupActivity.this, PicActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("index", 0);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
			break;
		case R.id.item_popupwindows_camera:
			if (ImageGridActivity.selectPics.size() <= ImageGridActivity.MAX_SIZE) {
				takePhotos();
			} else {
				Toast.makeText(PopupActivity.this, "最多选择" + ImageGridActivity.MAX_SIZE + "张图片", Toast.LENGTH_SHORT).show();
			}
			break;

		case R.id.item_popupwindows_cancel:
			finish();
			break;
		}

	}

	private void takePhotos() {

		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = null;
		if (isSDAvailable()) {
			file = new File(Environment.getExternalStorageDirectory() + "/telecom/myimage/");
		} else {
			file = new File(getCacheDir() + "/telecom/myimage/");
		}
		if (!file.exists()) {
			file.mkdirs();
		}
		name = String.valueOf(System.currentTimeMillis()) + ".jpg";
		file = new File(file.getAbsolutePath(), name);
		path = file.getPath();
		Uri imageUri = Uri.fromFile(file);

		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(openCameraIntent, 102);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode==RESULT_OK) {
			switch (requestCode) {
			case 102:
				ImageGridActivity.selectPics.add(ImageGridActivity.LOCAL_PATH + path);
				break;
			}
		}
		finish();
	}

	private static boolean isSDAvailable() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
}
