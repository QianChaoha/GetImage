package com.example.image;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.image.utils.AlbumHelper;
import com.example.image.utils.MyImgAdapterBaseAbs;
import com.example.image.utils.AlbumHelper.ImageBucket;

public class PicActivity extends Activity {
	public static List<ImageBucket> dataList;
	private GridView gridView;
	private ImageBucketAdapter adapter;
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public static Bitmap bitmap;
	int index;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_bucket);
		AlbumHelper.getInstance().init(getApplicationContext());
		initData();
		initView();
	}
	private void initData() {
		dataList = AlbumHelper.getInstance().getImagesBucketList(false);	
	}

	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		adapter = new ImageBucketAdapter();
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(PicActivity.this,
						ImageGridActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt("index", index);
				intent.putExtras(bundle);
				intent.putExtra(PicActivity.EXTRA_IMAGE_LIST,position);
				startActivity(intent);
				finish();
			}

		});
	}
	public class ImageBucketAdapter extends MyImgAdapterBaseAbs {

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			Holder holder;
			if (arg1 == null) {
				holder = new Holder();
				arg1 = View.inflate(PicActivity.this, R.layout.item_image_bucket, null);
				holder.iv = (ImageView) arg1.findViewById(R.id.image);
				holder.selected = (ImageView) arg1.findViewById(R.id.isselected);
				holder.name = (TextView) arg1.findViewById(R.id.name);
				holder.count = (TextView) arg1.findViewById(R.id.count);
				arg1.setTag(holder);
			} else {
				holder = (Holder) arg1.getTag();
			}
			ImageBucket item = dataList.get(arg0);
			holder.count.setText(Html.fromHtml(String.format("总数<font color=blue>%s</font>张",String.valueOf(item.imageList.size())+"")));
			holder.name.setText(item.bucketName);
			holder.selected.setVisibility(View.GONE);
			if (item.imageList != null && item.imageList.size() > 0) {
				this.getAsyncBitMap(holder.iv, item.imageList.get(0).getThumbnailPath());
			} else {
				holder.iv.setImageBitmap(null);
			}
			return arg1;
		}
	
		@Override
		public int getCount() {
			return dataList.size();
		}
		@Override
		public Object getItem(int position) {
			return null;
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}
		
	}
	public static class Holder {
		private ImageView iv;
		private ImageView selected;
		private TextView name;
		private TextView count;
	}
}
