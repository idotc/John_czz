package com.example.devstore_demo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.ItemizedOverlay;
import com.tianditu.android.maps.MapActivity;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.OnGetPoiResultListener;
import com.tianditu.android.maps.Overlay;
import com.tianditu.android.maps.OverlayItem;
import com.tianditu.android.maps.PoiInfo;
import com.tianditu.android.maps.PoiSearch;
import com.tianditu.android.maps.PoiSearch.CityInfo;
import com.tianditu.android.maps.PoiSearch.ProvinceInfo;

/*
 * 测试天地图的兴趣点查询功能和自定义标注功能
 * */
public class PoiSearchd extends MapActivity implements OnGetPoiResultListener {
	private MapView mapView = null;
	public static Context mCon = null;
	public static PoiSearch poi = null;
	private TextView mTv = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mCon = this;
		setContentView(R.layout.poisearch);
		mapView = (MapView) findViewById(R.id.mapview);
		mTv = (TextView) findViewById(R.id.show_text);
		mapView.getOverlays().add(new MyOverlay(this));
		Button search = (Button) findViewById(R.id.search_byName);
		Button searchInView = (Button) findViewById(R.id.search_inView);
		Button searchById = (Button) findViewById(R.id.search_byId);
		Button searchByCode = (Button) findViewById(R.id.search_byGbcode);
		// 普通查询
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				EditText et = (EditText) findViewById(R.id.main_search_poi_edit_name);
				if (et == null) {
					return;
				}
				if (et.getText() == null || et.getText().toString().equals("")) {
					Toast.makeText(mCon, "请输入您要查的地点", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				String searchcondition = et.getText().toString();
				poi = new PoiSearch(mCon, PoiSearchd.this);
				poi.search(searchcondition, null, null);
			}
		});
		// 视野内搜索
		searchById.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText et = (EditText) findViewById(R.id.main_search_poi_edit_name);
				if (et == null) {
					return;
				}
				if (et.getText() == null || et.getText().toString().equals("")) {
					Toast.makeText(mCon, "请输入您要查的地点", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				String searchcondition = et.getText().toString();
				poi = new PoiSearch(mCon, PoiSearchd.this);
				GeoPoint center = mapView.getMapCenter();
				poi.search(searchcondition, center, 20000);
			}
		});
		// 周边搜索
		searchByCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText et = (EditText) findViewById(R.id.main_search_poi_edit_name);
				if (et == null) {
					return;
				}
				if (et.getText() == null || et.getText().toString().equals("")) {
					Toast.makeText(mCon, "请输入您要查的地点", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				String searchcondition = et.getText().toString();
				poi = new PoiSearch(mCon, PoiSearchd.this);
				poi.search("156110000", searchcondition);
			}
		});
		// 在北京搜索
		searchInView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText et = (EditText) findViewById(R.id.main_search_poi_edit_name);
				if (et == null) {
					return;
				}
				if (et.getText() == null || et.getText().toString().equals("")) {
					Toast.makeText(mCon, "请输入您要查的地点", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				String searchcondition = et.getText().toString();
				GeoPoint p1 = mapView.getProjection().fromPixels(0, 0);
				GeoPoint p2 = mapView.getProjection().fromPixels(
						mapView.getWidth(), mapView.getHeight());
				poi = new PoiSearch(mCon, PoiSearchd.this);
				poi.search(searchcondition, p1, p2);
			}
		});
		mCon = this;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * 自定义覆盖物
	 */
	static class OverItemT extends ItemizedOverlay<OverlayItem> implements
			Overlay.Snappable {
		private List<OverlayItem> GeoList = new ArrayList<OverlayItem>();
		private static Drawable mMaker = null;

		public OverItemT(Drawable marker, Context context) {
			super((mMaker = boundCenterBottom(marker)));
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
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			super.draw(canvas, mapView, shadow);
		}

		@Override
		// 处理当点击事件
		protected boolean onTap(int i) {
			Toast.makeText(mCon, GeoList.get(i).getTitle(), Toast.LENGTH_SHORT)
					.show();
			return super.onTap(i);
		}

		public void Populate() {
			populate();
		}

	}

	@Override
	public void OnGetPoiResult(ArrayList<PoiInfo> poiInfo,
			ArrayList<ProvinceInfo> cityInfo, String keyword, int error) {
		// TODO Auto-generated method stub@Override
		// TODO Auto-generated method stub
		if (poiInfo == null || poiInfo.size() == 0) {
			String str = null;
			if (cityInfo == null || cityInfo.size() == 0) {
				str = "没有找到结果";

			} else {
				str = "在";
				for (int i = 0; i < cityInfo.size(); i++) {
					CityInfo city = cityInfo.get(i);
					str += city.mStrName + ",";
				}
				str += "找到结果";

			}
			Log.v("sdk", str);
			Toast.makeText(mCon, str, Toast.LENGTH_SHORT).show();
			return;
		}
		List<Overlay> list = mapView.getOverlays();
		list.clear();
		Resources res = getResources();
		Drawable marker = res.getDrawable(R.drawable.poi_xml);
		OverItemT overlay = new OverItemT(marker, mCon);
		for (int i = 0; i < poiInfo.size(); i++) {
			PoiInfo info = poiInfo.get(i);
			int size = info.mBusLine.size();
			String str = "";
			if (size > 0) {
				str = ",经过该站的公交:";
			}
			for (int j = 0; j < size; j++) {
				str += info.mBusLine.get(j).getName() + ",";
			}
			OverlayItem item = new OverlayItem(info.mPoint,
					info.mStrName + str, Integer.toString(i));
			overlay.addItem(item);
			item.getMarker(OverlayItem.ITEM_STATE_FOCUSED_MASK);
		}
		GeoPoint point = poiInfo.get(0).mPoint;
		mapView.getController().animateTo(point);
		overlay.Populate();
		list.add(overlay);
		mapView.postInvalidate();
	}

	public class MyOverlay extends Overlay {
		private Context mCon = null;
		private OverlayItem mItem = null;
		private Paint mPaint = null;

		public MyOverlay(Context con) {
			mCon = con;
			mPaint = new Paint();
		}

		@Override
		public boolean onTap(GeoPoint point, MapView mapView) {
			// TODO Auto-generated method stub
			mItem = new OverlayItem(point, "Tap", point.toString());
			mapView.postInvalidate();
			return true;
		}

		@Override
		public boolean onKeyUp(int keyCode, KeyEvent event, MapView mapView) {
			// TODO Auto-generated method stub
			Toast.makeText(mCon, "onKeyUp:" + keyCode, Toast.LENGTH_LONG)
					.show();
			return super.onKeyUp(keyCode, event, mapView);
		}

		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event, MapView mapView) {
			// TODO Auto-generated method stub
			Toast.makeText(mCon, "onKeyDown:" + keyCode, Toast.LENGTH_LONG)
					.show();
			return super.onKeyDown(keyCode, event, mapView);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event, MapView mapView) {
			// TODO Auto-generated method stub
			mTv.setText("onTouchEvent:" + event.getX() + "," + event.getY());
			return super.onTouchEvent(event, mapView);
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			// TODO Auto-generated method stub
			super.draw(canvas, mapView, shadow);
			if (mItem == null)
				return;
			mPaint.setColor(Color.RED);
			Drawable d = mCon.getResources().getDrawable(R.drawable.unchecked);

			Point point = mapView.getProjection().toPixels(mItem.getPoint(),
					null);
			d.setBounds(point.x - d.getIntrinsicWidth() / 2,
					point.y - d.getIntrinsicHeight(),
					point.x + d.getIntrinsicWidth() / 2, point.y);
			d.draw(canvas);
			Rect bounds = new Rect();
			mPaint.getTextBounds(mItem.getSnippet(), 0, mItem.getSnippet()
					.length() - 1, bounds);
			canvas.drawText(mItem.getSnippet(), point.x - bounds.width() / 2,
					point.y - d.getIntrinsicHeight(), mPaint);
		}

	}

}
