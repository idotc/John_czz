package com.example.devstore_demo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.tianditu.android.maps.ItemizedOverlay;
import com.tianditu.android.maps.MapActivity;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.Overlay;
import com.tianditu.android.maps.OverlayItem;
import com.tianditu.android.maps.TBusLineInfo;
import com.tianditu.android.maps.TBusLineSearch;
import com.tianditu.android.maps.TBusLineSearch.OnBusLineResultListener;
import com.tianditu.android.maps.TBusStationInfo;
import com.tianditu.android.maps.TBusStationSearch;
import com.tianditu.android.maps.TBusStationSearch.OnStationResultListener;

/**
 * 主要测试天地图公交查询
 * 
 * */
public class Searchbus extends MapActivity implements OnBusLineResultListener,
		OnStationResultListener {
	public static Context mCon = null;
	public static TBusLineSearch busSearch = null;
	public static TBusStationSearch station = null;
	private ArrayList<TBusLineInfo> mLines = null;
	private ListView mList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mCon = this;
		setContentView(R.layout.bussearch);
		Button search = (Button) findViewById(R.id.search_byName);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				EditText et = (EditText) findViewById(R.id.main_search_poi_edit_name);
				if (et == null) {
					return;
				}
				if (et.getText() == null || et.getText().toString().equals("")) {
					Toast.makeText(mCon, "请输入您要查公交线路名称", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				String searchcondition = et.getText().toString();
				busSearch = new TBusLineSearch(Searchbus.this);
				busSearch.search(searchcondition);
			}
		});

		EditText et = (EditText) findViewById(R.id.main_search_poi_edit_id);
		et.setInputType(InputType.TYPE_CLASS_NUMBER);
		// 按id搜索按钮
		Button searchById = (Button) findViewById(R.id.search_byId);
		searchById.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				EditText et = (EditText) findViewById(R.id.main_search_poi_edit_id);
				if (et == null) {
					return;
				}
				if (et.getText() == null || et.getText().toString().equals("")) {
					Toast.makeText(mCon, "请输入您要查的公交线路id", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				String searchcondition = et.getText().toString();
				busSearch = new TBusLineSearch(Searchbus.this);
				int id = Integer.parseInt(searchcondition);
				busSearch.search(id);
			}
		});

		EditText stationet = (EditText) findViewById(R.id.main_search_station_edit_id);
		stationet.setInputType(InputType.TYPE_CLASS_NUMBER);
		// 按id搜索按钮
		Button searchstationById = (Button) findViewById(R.id.search_station_byId);
		searchstationById.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				EditText et = (EditText) findViewById(R.id.main_search_station_edit_id);
				if (et == null) {
					return;
				}
				if (et.getText() == null || et.getText().toString().equals("")) {
					Toast.makeText(mCon, "请输入您要查的公交线路id", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				String searchcondition = et.getText().toString();
				station = new TBusStationSearch(Searchbus.this);
				station.search(searchcondition);
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
			return super.onTap(i);
		}

		public void Populate() {
			populate();
		}

	}

	@Override
	public void onBusLineResult(ArrayList<TBusLineInfo> lines, int error) {
		// TODO Auto-generated method stub
		if (lines == null || lines.size() == 0) {
			Toast.makeText(mCon, "未找到结果", Toast.LENGTH_LONG).show();
			return;
		}
		mLines = lines;
		List<String> data = new ArrayList<String>();
		for (int i = 0; i < lines.size(); i++) {
			TBusLineInfo line = lines.get(i);
			if (mLines.size() != 1) {
				data.add(line.getName());
			} else {
				for (int j = 0; j < line.getStations().size(); j++) {
					data.add(line.getStations().get(j).getName());
				}
				data.add("公司：" + line.getCompany());
				data.add("里程：" + line.getLength() + "米");
				data.add("结束时间：" + line.getFinalTime());
				data.add("id：" + line.getId());
				data.add("线路类型：" + line.getType());
				data.add("线路名称：" + line.getName());
				data.add("首发车时间：" + line.getFirstTime());
			}
		}
		mList = (ListView) findViewById(R.id.list);
		mList.setAdapter((ListAdapter) new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, data));
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (mLines.size() == 1) {
					if (arg2 < mLines.get(0).getStations().size()) {
						String id = mLines.get(0).getStations().get(arg2)
								.getId();
						EditText et = (EditText) findViewById(R.id.main_search_station_edit_id);
						et.setText(id);
					}
				} else {
					String id = mLines.get(arg2).getId();
					// TODO Auto-generated method stub
					EditText et = (EditText) findViewById(R.id.main_search_poi_edit_id);
					et.setText(id);
				}
			}
		});
	}

	@Override
	public void onStationResult(TBusStationInfo info, int error) {
		// TODO Auto-generated method stub
		if (info == null) {
			String str = "没有找到结果";
			Toast.makeText(mCon, str, Toast.LENGTH_SHORT).show();
			return;
		}
		int size = info.getBusLines().size();
		ArrayList<String> data = new ArrayList<String>();
		data.add("经过该站公交：");
		for (int j = 0; j < size; j++) {
			data.add(info.getBusLines().get(j).getName());
		}
		data.add("站点id：" + info.getId());
		data.add("站点名称：" + info.getName());
		data.add("站点坐标：" + info.getPoint());
		mList = (ListView) findViewById(R.id.list);
		mList.setAdapter((ListAdapter) new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, data));
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				return;
			}

		});
	}

}
