package com.example.image;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.image.ImageGridActivity.ImageGridAdapter.TextCallback;
import com.example.image.bean.FileItem;
import com.example.image.utils.MyImgAdapterBaseAbs;
import com.example.image.utils.AlbumHelper.ImageItem;

public class ImageGridActivity extends Activity {
	/** 指定文件夹下的图片集合 */
	private List<ImageItem> dataList;
	public static final String LOCAL_PATH = "file:///";
	/**
	 * 选择的图片的本地地址(去掉file:/// 即为绝对地址)
	 */
	public static List<String> selectPics = new ArrayList<String>();
	private GridView gridView;
	private ImageGridAdapter adapter;
	private TextView bt;
	private ArrayList<Integer> integers = new ArrayList<Integer>();
	int index;
	public static final int MAX_SIZE=9;
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				Toast.makeText(ImageGridActivity.this, "最多选择"+MAX_SIZE+"张图片", Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		index = getIntent().getIntExtra("index", 0);
		setContentView(R.layout.activity_image_grid);
		int position = this.getIntent().getExtras().getInt(PicActivity.EXTRA_IMAGE_LIST);
		dataList = PicActivity.dataList.get(position).imageList;
		for (int i = 0; i < dataList.size(); i++) {
			integers.add(0);
		}
		initView();
		initListener();
	}

	private void initListener() {
		adapter.setTextCallback(new TextCallback() {
			public void onListen(int count) {
				bt.setText("完成" + "(" + count + ")");
			}
		});
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				adapter.notifyDataSetChanged();
			}
		});
		bt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}

		});
	}

	private void initView() {
		bt = (TextView) findViewById(R.id.bt);
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new ImageGridAdapter(ImageGridActivity.this, dataList, mHandler);
		gridView.setAdapter(adapter);
		if (selectPics.size() > 0) {
			bt.setText("完成" + "(" + selectPics.size() + ")");
		}
	}

	public static class ImageGridAdapter extends MyImgAdapterBaseAbs<ImageItem> {

		private TextCallback textcallback = null;
		private Activity act;
		private List<ImageItem> dataList;

		private Handler mHandler;

		public static interface TextCallback {
			public void onListen(int count);
		}

		public void setTextCallback(TextCallback listener) {
			textcallback = listener;
		}

		public ImageGridAdapter(Activity act, List<ImageItem> list, Handler mHandler) {
			this.mHandler = mHandler;
			this.act = act;
			this.dataList = list;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final Holder holder;
			if (convertView == null) {
				holder = new Holder();
				convertView = View.inflate(act, R.layout.item_image_grid, null);
				holder.iv = (ImageView) convertView.findViewById(R.id.image);
				holder.selected = (ImageView) convertView.findViewById(R.id.isselected);
				holder.text = (TextView) convertView.findViewById(R.id.item_image_grid_text);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			final ImageItem item = (ImageItem) this.getItem(position);
			holder.iv.setTag(item.imagePath);
			this.getAsyncBitMap(holder.iv, item.getThumbnailPath());
			if (item.isSelected&&selectPics.contains(LOCAL_PATH + item.imagePath)) {
				holder.selected.setImageResource(R.drawable.zsht_success);
				holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
			} else {
				item.isSelected=false;
				holder.selected.setImageResource(-1);
				holder.text.setBackgroundColor(0x00000000);
			}
			holder.iv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (item.isSelected) {
						selectPics.remove(LOCAL_PATH + item.imagePath);
						// mList.remove(item.imagePath);
						holder.selected.setImageResource(-1);
						holder.text.setBackgroundColor(0x00000000);
						item.isSelected = !item.isSelected;
						if (textcallback != null) {
							textcallback.onListen(selectPics.size());
						}
					} else {
						if (selectPics.size() < MAX_SIZE) {
							item.isSelected = !item.isSelected;
							FileItem it = new FileItem();
							it.setFileName(item.imageName);
							it.setFileLocalPath(item.imagePath);
							selectPics.add(LOCAL_PATH + item.imagePath);

							holder.selected.setImageResource(R.drawable.zsht_success);
							holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
							if (textcallback != null) {
								textcallback.onListen(selectPics.size());
							}
						} else {
							Message message = Message.obtain(mHandler, 0);
							message.sendToTarget();
						}
					}
				}
			});

			return convertView;
		}

		private class Holder {
			private ImageView iv;
			private ImageView selected;
			private TextView text;
		}

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}

}
