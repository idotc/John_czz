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
		// ָ����
		mLocation.enableCompass();
		// �ҵ�λ��
		mLocation.enableMyLocation();
		list.add(mLocation);
		// ע��λ��
		LocationManager m_locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (m_locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			m_locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 1000, 0, mLocation);
		}
		decode = new TGeoDecode(this);
		// ��λ�¼�
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

		// "�ҵ�λ��"�ϵĵ���¼�

		protected boolean dispatchTap() {
			Toast.makeText(mCon, "��������", Toast.LENGTH_SHORT).show();
			return true;
		}

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			super.onLocationChanged(location);
			if (location != null) {
				String strLog = String.format("����ǰ��λ��:\r\n" + "γ��:%f\r\n"
						+ "����:%f", location.getLongitude(),
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
			tv.setText("��ȡ��ַʧ�ܣ�");
			return;
		}
		String str = "";
		str += "�����poi����:" + addr.getPoiName() + "\n";
		str += "���poi�ķ�λ:" + addr.getPoiDirection() + "\n";
		str += "���poi�ľ���:" + addr.getPoiDistance() + "\n";
		str += "��������:" + addr.getCity() + "\n";
		str += "ȫ��:" + addr.getFullName() + "\n";
		str += "����ĵ�ַ:" + addr.getAddress() + "\n";
		str += "�����ַ�ķ�λ:" + addr.getAddrDirection() + "\n";
		str += "�����ַ�ľ���:" + addr.getAddrDistance() + "\n";
		str += "����ĵ�·����:" + addr.getRoadName() + "\n";
		str += "�����·�ľ���:" + addr.getRoadDistance();
		tv.setText(str);
	}
}
