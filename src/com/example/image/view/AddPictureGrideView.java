package com.example.image.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.image.ImageGridActivity;
import com.example.image.LookPhotoActivity;
import com.example.image.PicActivity;
import com.example.image.PopupActivity;
import com.example.image.R;
import com.example.image.utils.MyImgAdapterBaseAbs;

public class AddPictureGrideView extends GridView {
	private static Bitmap bitmap;
	private Activity context = (Activity) getContext();
	private boolean hasPic = false;
	private MyAdapter myAdapter;
	private List<String> lastSelectPics = new ArrayList<String>();

	public AddPictureGrideView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public AddPictureGrideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AddPictureGrideView(Context context) {
		super(context);
		init();
	}

	/**
	 * 显示选择后的图片
	 */
	public void refresh() {
		if (lastSelectPics.equals(ImageGridActivity.selectPics)) {
			// 本次选择的图片没有变动
			return;
		}
		lastSelectPics.clear();
		lastSelectPics.addAll(ImageGridActivity.selectPics);
		hasPic = true;
		setAdapter(myAdapter);
	}

	public void init() {
		bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.upload_btn);
		myAdapter = new MyAdapter();
		setAdapter(myAdapter);
		setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = null;
				if (position == myAdapter.getCount() - 1) {
					intent = new Intent(getContext(), PopupActivity.class);
				} else {
					intent = new Intent(getContext(), LookPhotoActivity.class);
					intent.putExtra("item", position);
				}
				getContext().startActivity(intent);
			}
		});
	}

	class MyAdapter extends MyImgAdapterBaseAbs {

		@Override
		public int getCount() {
			if (hasPic) {
				return 1 + ImageGridActivity.selectPics.size();
			} else {
				return 1;
			}

		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(context, R.layout.picture_item, null);
				holder.imageView = (ImageView) convertView.findViewById(R.id.iv);
				holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (hasPic) {
				if (position == ImageGridActivity.selectPics.size()) {
					// 显示增加按钮
					holder.imageView.setImageBitmap(bitmap);
					holder.iv_delete.setVisibility(View.GONE);
				} else {
					// 将多次选择的图片显示出来
					this.getAsyncBitMap(holder.imageView, ImageGridActivity.selectPics.get(position));
					holder.iv_delete.setVisibility(View.VISIBLE);
					holder.iv_delete.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							ImageGridActivity.selectPics.remove(position);
							myAdapter.notifyDataSetChanged();

						}
					});

				}
			} else {
				holder.iv_delete.setVisibility(View.GONE);
				holder.imageView.setImageBitmap(bitmap);
			}

			return convertView;
		}
	}

	class ViewHolder {
		ImageView imageView, iv_delete;
	}

	public void onDestory() {
		ImageGridActivity.selectPics.clear();
		if (PicActivity.bitmap!=null) {
			PicActivity.bitmap.recycle();

		}
		PicActivity.dataList.clear();
	}
}
