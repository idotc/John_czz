package com.example.devstore_demo;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapActivity;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.MyLocationOverlay;
import com.tianditu.android.maps.Overlay;
import com.tianditu.android.maps.TGeoAddress;
import com.tianditu.android.maps.TGeoDecode;
import com.tianditu.android.maps.TGeoDecode.OnGeoResultListener;

public class tdtLocation extends MapActivity implements OnGeoResultListener {
	private MapView mapView = null;
	public TextView tv;
	protected Context mCon = null;
	int mCount = 0;
	MyLocationOverlay mLocation = null;
	TGeoDecode decode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mylocation);
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.displayZoomControls(true);

		mCon = this;
		List<Overlay> list = mapView.getOverlays();
		mLocation = new MyOverlay(this, mapView);
		// 指南针
		mLocation.enableCompass();
		// 我的位置
		mLocation.enableMyLocation();
		list.add(mLocation);
		// 注册位置
		LocationManager m_locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (m_locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			m_locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 1000, 0, mLocation);
		}
		decode = new TGeoDecode(this);
		// 定位事件
		ImageButton location = (ImageButton) findViewById(R.id.location);
		location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GeoPoint point = mLocation.getMyLocation();
				if (point != null)
					mapView.getController().animateTo(point);
			}

		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		mapView.getController().stopAnimation(false);
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK)
			System.exit(0);
		return super.onKeyUp(keyCode, event);
	}

	class MyOverlay extends MyLocationOverlay {
		public MyOverlay(Context context, MapView mapView) {
			super(context, mapView);
			// TODO Auto-generated constructor stub
		}

		// "我的位置"上的点击事件

		protected boolean dispatchTap() {
			Toast.makeText(mCon, "我在这里", Toast.LENGTH_SHORT).show();
			return true;
		}

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			super.onLocationChanged(location);
			if (location != null) {
				String strLog = String.format("您当前的位置:\r\n" + "纬度:%f\r\n"
						+ "经度:%f", location.getLongitude(),
						location.getLatitude());
				Toast.makeText(mCon, strLog, Toast.LENGTH_SHORT).show();
				GeoPoint point = new GeoPoint((int) (location.getLatitude()*1e6),
						(int) (location.getLongitude()*1e6));
				decode.search(point);
			}
			GeoPoint point = mLocation.getMyLocation();
			if (point != null)
				mapView.getController().animateTo(point);
		}
	}

	@Override
	public void onGeoDecodeResult(TGeoAddress addr, int errCode) {
		// TODO Auto-generated method stub
		tv = (TextView) findViewById(R.id.geodecode_tv);// TODO Auto-generated
														// method stub
		if (errCode != 0) {
			tv.setText("获取地址失败！");
			return;
		}
		String str = "";
		str += "最近的poi名称:" + addr.getPoiName() + "\n";
		str += "最近poi的方位:" + addr.getPoiDirection() + "\n";
		str += "最近poi的距离:" + addr.getPoiDistance() + "\n";
		str += "城市名称:" + addr.getCity() + "\n";
		str += "全称:" + addr.getFullName() + "\n";
		str += "最近的地址:" + addr.getAddress() + "\n";
		str += "最近地址的方位:" + addr.getAddrDirection() + "\n";
		str += "最近地址的距离:" + addr.getAddrDistance() + "\n";
		str += "最近的道路名称:" + addr.getRoadName() + "\n";
		str += "最近道路的距离:" + addr.getRoadDistance();
		tv.setText(str);
	}
}
