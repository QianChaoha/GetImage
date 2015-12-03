package com.example.image.utils;

import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.image.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public abstract class MyImgAdapterBaseAbs<E> extends BaseAdapter{
	private static DisplayImageOptions options;
	private int nRes = -1;
	protected final DisplayImageOptions getDisplayImageOptions(final int nRes){
		if (options == null||this.nRes!=nRes) {
			this.nRes = nRes;
			options = new DisplayImageOptions.Builder()
			//加载中
			.showStubImage(nRes)
			//加载为空时
			.showImageForEmptyUri(nRes)
			//加载失入时
			.showImageOnFail(nRes)
			//内存缓存
			.cacheInMemory(false)
			//硬盘中缓存
			.cacheOnDisc(true).build();
		}
		return options;
	}
	
	public MyImgAdapterBaseAbs() {
		super();
	}
	
	protected final void getAsyncBitMap(final ImageView imgView, final String picUrl)
	{
		this.getAsyncBitMap(imgView, picUrl,R.drawable.default_user_big);//,R.drawable.default_img);
	}
	
	protected final void getAsyncBitMap(final ImageView imgView, final String picUrl,
			final int nRes) {
		ImageLoader.getInstance().displayImage(picUrl, imgView, getDisplayImageOptions(nRes));
	}
}
