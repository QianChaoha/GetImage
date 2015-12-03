package com.example.image;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

public class LookPhotoActivity extends Activity implements OnPageChangeListener {
	/**
	 * ViewPager
	 */
	private ViewPager viewPager;

	/**
	 * 装点点的ImageView数组
	 */
	private ImageView[] tips;

	/**
	 * 装ImageView数组
	 */
	private ImageView[] mImageViews;

	/**
	 * 图片资源id
	 */
//	private List<String> imgIdArray;
	private OnClickListener clickListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView();
	}

	public class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mImageViews.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(mImageViews[position]);
		}

		/**
		 * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
		 */
		@Override
		public Object instantiateItem(View container, int position) {
			try {
				((ViewPager) container).addView(mImageViews[position], 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mImageViews[position];
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		setImageBackground(arg0);
	}

	/**
	 * 设置选中的tip的背景
	 * 
	 * @param selectItems
	 */
	private void setImageBackground(int selectItems) {
		for (int i = 0; i < tips.length; i++) {
			if (i == selectItems) {
				tips[i].setBackgroundResource(R.drawable.icon_selct_1);
			} else {
				tips[i].setBackgroundResource(R.drawable.icon_selct_2);
			}
		}
	}

	protected void setContentView() {
		setContentView(R.layout.look_photo);
		ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);
		viewPager = (ViewPager) findViewById(R.id.viewPager);

		// 载入图片资源ID
//		imgIdArray = new ArrayList<String>();
//		Intent intent = getIntent();
//		if (intent != null) {
//			Bundle bundle = intent.getExtras();
//			if (bundle != null) {
//				imgIdArray = bundle.getStringArrayList("pictures");
//			}
//		}
		int item = getIntent().getIntExtra("item", 0);
		clickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				LookPhotoActivity.this.finish();
			}
		};
		// 将图片装载到数组中
		mImageViews = new ImageView[ImageGridActivity.selectPics.size()];
		for (int i = 0; i < mImageViews.length; i++) {
			ImageView imageView = new ImageView(this);
			mImageViews[i] = imageView;
			imageView.setOnClickListener(clickListener);
			ImageLoader.getInstance().displayImage(ImageGridActivity.selectPics.get(i),imageView);

		}
		// 将点点加入到ViewGroup中
		tips = new ImageView[ImageGridActivity.selectPics.size()];
		for (int i = 0; i < tips.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setLayoutParams(new LayoutParams(10, 10));
			tips[i] = imageView;
			if (i == 0) {
				tips[i].setBackgroundResource(R.drawable.icon_selct_1);
			} else {
				tips[i].setBackgroundResource(R.drawable.icon_selct_2);
			}

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			layoutParams.leftMargin = 5;
			layoutParams.rightMargin = 5;
			group.addView(imageView, layoutParams);
		}
		// 设置Adapter
		viewPager.setAdapter(new MyAdapter());
		// 设置监听，主要是设置点点的背景
		viewPager.setOnPageChangeListener(this);
		// 设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
		// 如果只有一张图片，禁止滑动
		if (ImageGridActivity.selectPics.size() < 2) {
			viewPager.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					return true;
				}
			});
		}
		viewPager.setCurrentItem(item);

	}

}