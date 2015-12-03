package com.example.image.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.text.TextUtils;

public class AlbumHelper {
	private Context context;
	private ContentResolver cr;

	/**
	 * �Ƿ񴴽���ͼƬ��
	 */
	private boolean hasBuildImagesBucketList = false;
	/** 缩略图的id和path */
	private HashMap<String, String> thumbnailList = new HashMap<String, String>();
	// private List<HashMap<String, String>> albumList = new
	// ArrayList<HashMap<String, String>>();
	private HashMap<String, ImageBucket> bucketList = new HashMap<String, ImageBucket>();
	private static AlbumHelper instance;

	private AlbumHelper() {
	}

	public static AlbumHelper getInstance() {
		if (instance == null) {
			instance = new AlbumHelper();
		}
		return instance;
	}

	public void setNull() {
		instance = null;
	}

	/**
	 * 
	 * @param context
	 */
	public void init(Context context) {
		if (this.context == null) {
			this.context = context;
			cr = context.getContentResolver();
		}
	}

	/**
	 * 获取到手机的缩略图id和path，存于thumbnailList
	 */
	private void getThumbnail() {
		String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID, Thumbnails.DATA };
		Cursor cursor = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
		if (cursor.moveToFirst()) {
			int _id;
			int image_id;
			String image_path;
			int _idColumn = cursor.getColumnIndex(Thumbnails._ID);
			int image_idColumn = cursor.getColumnIndex(Thumbnails.IMAGE_ID);
			int dataColumn = cursor.getColumnIndex(Thumbnails.DATA);
			do {
				// Get the field values
				_id = cursor.getInt(_idColumn);
				image_id = cursor.getInt(image_idColumn);
				image_path = cursor.getString(dataColumn);
				thumbnailList.put(String.valueOf(image_id), image_path);
			} while (cursor.moveToNext());
		}
	}


	/**
	 * 给bucketList设置数据
	 */
	private void buildImagesBucketList() {
		getThumbnail();
		String columns[] = new String[] { Media._ID, Media.BUCKET_ID, Media.PICASA_ID, Media.DATA, Media.DISPLAY_NAME, Media.TITLE, Media.SIZE,
				Media.BUCKET_DISPLAY_NAME };
		Cursor cur = cr.query(Media.EXTERNAL_CONTENT_URI, columns, null, null, null);
		if (cur.moveToFirst()) {
			int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
			int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
			int photoNameIndex = cur.getColumnIndexOrThrow(Media.DISPLAY_NAME);
			int photoTitleIndex = cur.getColumnIndexOrThrow(Media.TITLE);
			int photoSizeIndex = cur.getColumnIndexOrThrow(Media.SIZE);
			int bucketDisplayNameIndex = cur.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
			int bucketIdIndex = cur.getColumnIndexOrThrow(Media.BUCKET_ID);
			int picasaIdIndex = cur.getColumnIndexOrThrow(Media.PICASA_ID);
			int totalNum = cur.getCount();

			do {
				String _id = cur.getString(photoIDIndex);
				String name = cur.getString(photoNameIndex);
				String path = cur.getString(photoPathIndex);
				String title = cur.getString(photoTitleIndex);
				String size = cur.getString(photoSizeIndex);
				String bucketName = cur.getString(bucketDisplayNameIndex);
				String bucketId = cur.getString(bucketIdIndex);
				String picasaId = cur.getString(picasaIdIndex);
				ImageBucket bucket = bucketList.get(bucketId);
				if (bucket == null) {
					bucket = new ImageBucket();
					bucketList.put(bucketId, bucket);
					bucket.imageList = new ArrayList<ImageItem>();
					bucket.bucketName = bucketName;
				}
				ImageItem imageItem = new ImageItem();
				imageItem.imageId = _id;
				imageItem.imagePath = path;
				imageItem.imageName = name;
				imageItem.thumbnailPath = thumbnailList.get(_id);
				bucket.imageList.add(imageItem);
			} while (cur.moveToNext());
		}
		hasBuildImagesBucketList = true;
	}

	/**
	 * 返回系统所有的图片文件夹集合，key图片文件夹名称，value文件夹下所有图片
	 * 
	 * @param refresh
	 * @return
	 */
	public List<ImageBucket> getImagesBucketList(boolean refresh) {
		if (refresh || (!refresh && !hasBuildImagesBucketList)) {
			buildImagesBucketList();
		}
		List<ImageBucket> tmpList = new ArrayList<ImageBucket>();
		Iterator<Entry<String, ImageBucket>> itr = bucketList.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<String, ImageBucket> entry = (Map.Entry<String, ImageBucket>) itr.next();
			tmpList.add(entry.getValue());
		}
		return tmpList;
	}

	/**
	 * Description: 包含一组图片和图片所在文件夹的ID Company: guanghua
	 * 
	 * @author qianchao
	 */
	public static class ImageBucket {
		/** 一组图片所在文件夹的名称 */
		public String bucketName;
		/** 文件夹下的图片 */
		public List<ImageItem> imageList = new Stack<ImageItem>();
	}

	// 图片对象,包括缩略图路径和图片路径
	public class ImageItem implements Serializable {
		private static final long serialVersionUID = 1L;
		public String imageId;
		public String thumbnailPath;
		public String imagePath;// ����·��
		public String imageName;
		/**是否选中*/
		public boolean isSelected = false;

		public String getThumbnailPath() {
			if (!TextUtils.isEmpty(thumbnailPath)) {
				return "file:///" + thumbnailPath;
			}
			return "file:///" + imagePath;
		}
	}
}
