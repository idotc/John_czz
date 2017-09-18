package com.example.devstore_demo;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapActivity;
import com.tianditu.android.maps.MapController;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.TMapLayerManager;
import com.tianditu.android.maps.MapView.TMapType;

public class MapviewShow extends MapActivity {
	public Button test, vector, image, pan;
	public MapView mapView;
	protected MapController mController = null;
	private TMapLayerManager mLayerMnger = null;

	private ListView mList = null;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.mapview);
		mapView = (MapView) findViewById(R.id.mapview);
		// 显示地名
		mapView.setPlaceName(true);
		// 设置天地图logo的位置
		mapView.setLogoPos(MapView.LOGO_LEFT_TOP);
		// 设置显示影像地图
		// mapView.setSatellite(true);
		// 启动内置缩放控件
		mapView.setBuiltInZoomControls(true);
		mController = mapView.getController();
		mController.setZoom(10);
		mController.setCenter(new GeoPoint((int) (39.90923 * 1000000),
				(int) (116.397428 * 1000000)));
		// 隐藏地名
		test = (Button) findViewById(R.id.test);
		test.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mapView.setSatellite(true);
				mapView.setPlaceName(false);
			}
		});
		// 显示矢量地图
		vector = (Button) findViewById(R.id.vector);
		vector.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mapView.setSatellite(false);
				int zoomLevel = mapView.getZoomLevel();
				mapView.getController().setZoom(zoomLevel);
			}
		});
		// 显示影像地图
		image = (Button) findViewById(R.id.image);
		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mapView.setSatellite(true);
				int zoomLevel = mapView.getZoomLevel();
				mapView.getController().setZoom(zoomLevel);
			}
		});
		pan = (Button) findViewById(R.id.image1);
		pan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mController.animateTo(new GeoPoint(39945124, 116245124));
			}
		});
		Button btn = (Button) findViewById(R.id.maplayer_btn);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				mapView.setMapType(TMapType.MAP_TYPE_IMG);
				mapView.setDrawOverlayWhenZooming(false);
				if (mList.getVisibility() == View.VISIBLE) {
					mList.setVisibility(View.GONE);
					return;
				}
				mList.setVisibility(View.VISIBLE);

				String[] layers = mLayerMnger.getLayers(mapView.getMapType());
				mList.setAdapter(new ArrayAdapter<String>(MapviewShow.this,
						android.R.layout.simple_list_item_1, layers));
			}

		});
		mList = (ListView) findViewById(R.id.maplayer_list);
		mList.setVisibility(View.GONE);
		mLayerMnger = new TMapLayerManager(mapView);
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				@SuppressWarnings("unchecked")
				ArrayAdapter<String> adapter = (ArrayAdapter<String>) arg0
						.getAdapter();
				String layer = (String) adapter.getItem(arg2);
				String[] layers = new String[] { layer };
				mLayerMnger.setLayersShow(layers);
				mList.setVisibility(View.GONE);
				String show[] = mLayerMnger.getLayersShow();
				if (show == null) {
					return;
				}
				String strshow = "";
				for (int i = 0; i < show.length; i++) {
					strshow += show[i];
				}
				Toast.makeText(MapviewShow.this, strshow, Toast.LENGTH_SHORT)
						.show();
				int type = mLayerMnger.getMapType("影像");
				Toast.makeText(MapviewShow.this, String.valueOf(type),
						Toast.LENGTH_SHORT).show();
				String maps[] = mLayerMnger.getMaps();
				String str = "";
				for (int i = 0; i < maps.length; i++) {
					str += maps[i];
				}
				Toast.makeText(MapviewShow.this, str, Toast.LENGTH_SHORT)
						.show();
				String layerss[] = mLayerMnger.getLayers(type);
				String strlayer = "";
				for (int i = 0; i < layerss.length; i++) {
					strlayer += layerss[i];
				}
				Toast.makeText(MapviewShow.this, strlayer, Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
