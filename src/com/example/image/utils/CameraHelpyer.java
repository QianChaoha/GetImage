package com.example.image.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

public class CameraHelpyer {
	// data
	public static final int TAKE_BIG_PICTURE = 1;
	public static final int TAKE_SMALL_PICTURE = 2;
	public static final int CROP_BIG_PICTURE = 3;
	public static final int CROP_SMALL_PICTURE = 4;
	public static final int A = 100;
	public static final int B = 101;
	public static final int C = 102;
	public static final int D = 103;
	public static final int CHOOSE_BIG_PICTURE = 5;
	public static final int CHOOSE_SMALL_PICTURE = 6;
	public static final String TEMP_IMAGE = "temp1.jpg";
	public static final String AVATAR_IMAGE = "avatar1.jpg";
	public static final String AVATAR_Image = "test1.jpg";
	public static final String Image_NAME = "test2.jpg";
	public static final String DIY_NAME = "telecom/temps";
	public static String DIY_PATH = "file:///sdcard/";
	public static String temp = "file:///sdcard/temp.jpg";
	public static File tempImageFile = new File(Environment.getExternalStorageDirectory() + "/" + DIY_NAME + "/" + TEMP_IMAGE);
	public static File avatarImageFile = new File(Environment.getExternalStorageDirectory() + "/" + DIY_NAME + "/" + AVATAR_IMAGE);
	public static File avatartestImageFile = new File(Environment.getExternalStorageDirectory() + "/" + DIY_NAME + "/" + AVATAR_Image);
	public static File imageDIR = new File(Environment.getExternalStorageDirectory() + "/" + DIY_NAME);
	// 剪切和压缩参数
	public static int aspectX = 1;
	public static int aspectY = 1;
	public static int outputX = 300;
	public static int outputY = 300;
	// 获取临时照片
	public static Uri tempImageUri = Uri.parse(DIY_PATH + DIY_NAME + "/" + TEMP_IMAGE);
	// 获取头像图片
	public static Uri avatarImageUri = Uri.parse(DIY_PATH + DIY_NAME + "/" + AVATAR_IMAGE);
	public static ImageView imageView;

	// 拍照
	public static Intent Q1() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// action is
		intent.putExtra(MediaStore.EXTRA_OUTPUT, tempImageUri);
		return intent;
	}

	/**
	 * 调取相机功能拍照
	 * 
	 * @return
	 */
	public static Intent selectFromCroup() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		return intent;
	}

	// 拍照
	public static Intent Q4() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// action is
		// capture
		// tempImageUri = Uri.parse(temp);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, tempImageUri);
		intent.setPackage("vStudio.Android.Camera360");
		return intent;
	}

	// 保存临时文件->裁剪
	public static Intent Q2() {
		if (tempImageUri != null) {
			// Bitmap bitmap = decodeUriAsBitmap(tempImageUri.toString());
			// bitmap.recycle();
			// 调用裁剪
			Intent intent = Crop(tempImageUri);
			return intent;
		}
		return null;
	}

	public static Intent Q3() {
		if (tempImageUri != null) {
			// Bitmap bitmap = decodeUriAsBitmap(tempImageUri.toString());
			// bitma.precycle();
			// 调用裁剪
			Intent intent = Crops(tempImageUri);
			return intent;
		}
		return null;
	}

	// 直接裁剪临时照片
	public static Intent Crops(Uri uri) {
		Intent intent2 = new Intent("com.android.camera.action.CROP");
		intent2.setDataAndType(uri, "image/*");
		intent2.putExtra("crop", "true");
		intent2.putExtra("aspectX", aspectX);
		intent2.putExtra("aspectY", aspectY);
		intent2.putExtra("outputX", outputX);
		intent2.putExtra("outputY", outputY);
		intent2.putExtra("scale", true);
		intent2.putExtra("return-data", true);
		intent2.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent2.putExtra("noFaceDetection", true);
		return intent2;
	}

	// 选择图片库中德文件裁减
	public static Intent Z1() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", aspectX);
		intent.putExtra("aspectY", aspectY);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", true);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		return intent;
	}

	// 选择图片库中德文件裁减
	public static Intent Z2() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		// intent.putExtra("crop", "true");
		return intent;
	}

	// 选择图片库中德文件裁减
	public static Intent Z3() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 2);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 220);
		intent.putExtra("outputY", 110);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", true);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		return intent;
	}

	// 直接裁剪临时照片
	public static Intent Crop(Uri uri) {
		Intent intent2 = new Intent("com.android.camera.action.CROP");
		intent2.setDataAndType(uri, "image/*");
		intent2.putExtra("crop", "true");
		intent2.putExtra("aspectX", 1);
		intent2.putExtra("aspectY", 1);
		intent2.putExtra("outputX", 100);
		intent2.putExtra("outputY", 100);
		intent2.putExtra("scale", true);
		// intent2.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent2.putExtra("return-data", true);
		intent2.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		return intent2;
	}

	// 保存图片
	public static void saveMyBitmap(Activity obj, String fileName, Bitmap bitmap) throws IOException {
		FileOutputStream outStream = obj.openFileOutput(fileName, Context.MODE_WORLD_READABLE);
		bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outStream);
		try {
			outStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 保存图片
	public static void saveMyBitmap(String bitName, Bitmap bitmap) throws IOException {
		File f = new File(Environment.getExternalStorageDirectory() + "/" + DIY_NAME);
		File file = new File(Environment.getExternalStorageDirectory() + "/" + DIY_NAME, bitName);
		try {
			if (!f.exists()) {
				f.mkdir();
			}
			boolean r = file.createNewFile();
		} catch (IOException e) {
		}
		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outStream);
		try {
			outStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 获取图片对象
	private static Bitmap decodeUriAsBitmap(String uri) {
		Bitmap bitmap = null;
		try {
			File f = new File(uri);
			FileInputStream s = new FileInputStream(f);
			bitmap = BitmapFactory.decodeStream(s);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	/**
	 * 根据图片路径对图片进行压缩返回
	 * 
	 * @param srcPath
	 * @return
	 */
	public static Bitmap getImage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800;// 这里设置高度为800f
		float ww = 600;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return bitmap;// 压缩好比例大小后再进行质量压缩
	}
}
