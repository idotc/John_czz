package com.example.devstore_demo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.ItemizedOverlay;
import com.tianditu.android.maps.MapActivity;
import com.tianditu.android.maps.MapController;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.MapView.LayoutParams;
import com.tianditu.android.maps.MyLocationOverlay;
import com.tianditu.android.maps.Overlay;
import com.tianditu.android.maps.OverlayItem;

public class Itemizeddemo extends MapActivity {
	public static View mPopView = null;
	public static TextView mText = null;
	public static MapView mapView = null;
	protected static Context mCon = null;
	protected MapController mController = null;
	private OverItemT mOverlay = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itemized);
		mapView = (MapView) findViewById(R.id.itemized_mapview);
		mapView.setBuiltInZoomControls(true);
		// 在缩放时显示Overlay，可以会影响程序显示效率
		// mMapView.setDrawOverlayWhenZooming(true);
		mController = mapView.getController();
		mCon = this;

		List<Overlay> list = mapView.getOverlays();
		MyLocationOverlay myLocation = new MyLocationOverlay(this, mapView);
		myLocation.enableCompass();
		myLocation.enableMyLocation();
		list.add(myLocation);
		// 标注时用的资源
		Resources res = getResources();
		Drawable marker = res.getDrawable(R.drawable.poiresult_sel);
		mOverlay = new OverItemT(marker, this);
		list.add(mOverlay);

		mController.setCenter(new GeoPoint((int) (39.90923 * 1000000),
				(int) (116.397428 * 1000000)));
		mController.setZoom(12);
		// 创建弹出框view
		mPopView = super.getLayoutInflater().inflate(R.layout.popview, null);
		mText = (TextView) mPopView.findViewById(R.id.text);
		mapView.addView(mPopView, new MapView.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, null,
				MapView.LayoutParams.TOP_LEFT));
		mPopView.setVisibility(View.GONE);

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK)
			System.exit(0);
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	static class OverItemT extends ItemizedOverlay<OverlayItem> implements
			Overlay.Snappable {
		private List<OverlayItem> GeoList = new ArrayList<OverlayItem>();
		private Context mContext;
		private static Drawable mMaker = null;
		private double mLat1 = 39.90456;
		private double mLon1 = 116.397568;
		private double mLat2 = 39.9422;
		private double mLon2 = 116.4922;
		private double mLat3 = 39.967723;
		private double mLon3 = 116.4722;

		public OverItemT(Drawable marker, Context context) {
			super((mMaker = boundCenterBottom(marker)));
			mContext = context;

			// 用给定的经纬度构造GeoPoint，单位是微度 (度 * 1E6)
			GeoPoint p1 = new GeoPoint((int) (mLat1 * 1E6), (int) (mLon1 * 1E6));
			GeoPoint p2 = new GeoPoint((int) (mLat2 * 1E6), (int) (mLon2 * 1E6));
			GeoPoint p3 = new GeoPoint((int) (mLat3 * 1E6), (int) (mLon3 * 1E6));

			GeoList.add(new OverlayItem(p1, "P1", "hello,devstore,我是p1"));
			GeoList.add(new OverlayItem(p2, "P2", "hello,devstore,我是p2"));
			GeoList.add(new OverlayItem(p3, "P3", "hello,devstore,我是p3"));
			populate(); // createItem(int)方法构造item。一旦有了数据，在调用其它方法前，首先调用这个方法

		}

		@Override
		protected OverlayItem createItem(int i) {
			return GeoList.get(i);
		}

		@Override
		public int size() {
			return GeoList.size();
		}

		public void addItem(OverlayItem item) {
			item.setMarker(mMaker);
			GeoList.add(item);
		}

		@Override
		// 处理当点击事件
		protected boolean onTap(int i) {
			GeoPoint pt = GeoList.get(i).getPoint();
			Itemizeddemo.mapView.updateViewLayout(Itemizeddemo.mPopView,
					new MapView.LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT, pt,
							MapView.LayoutParams.BOTTOM_CENTER));
			Itemizeddemo.mPopView.setVisibility(View.VISIBLE);
			Itemizeddemo.mText.setText(GeoList.get(i).getTitle());

			Toast.makeText(mContext, GeoList.get(i).getSnippet(),
					Toast.LENGTH_SHORT).show();

			return true;
		}

		@Override
		public boolean onTouchEvent(MotionEvent event, MapView mapView) {
			// TODO Auto-generated method stub
			return super.onTouchEvent(event, mapView);
		}

		@Override
		public boolean onKeyUp(int keyCode, KeyEvent event, MapView mapView) {
			// TODO Auto-generated method stub
			return super.onKeyUp(keyCode, event, mapView);
		}

		@Override
		public boolean onTrackballEvent(MotionEvent event, MapView mapView) {
			// TODO Auto-generated method stub
			return super.onTrackballEvent(event, mapView);
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			// TODO Auto-generated method stub
			super.draw(canvas, mapView, shadow);
		}

		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
				long when) {
			// TODO Auto-generated method stub
			return super.draw(canvas, mapView, shadow, when);
		}

		@Override
		public boolean onTap(GeoPoint p, MapView mapView) {
			// TODO Auto-generated method stub
			Itemizeddemo.mPopView.setVisibility(View.GONE);
			return super.onTap(p, mapView);
		}

	}
}
