package com.example.devstore_demo;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.Overlay;
import com.tianditu.android.maps.Projection;
import com.tianditu.android.maps.TBusRoute;
import com.tianditu.android.maps.TTransitLine;
import com.tianditu.android.maps.TTransitResult;
import com.tianditu.android.maps.TTransitSegmentInfo;

public class BusOverlay extends Overlay{
	private Paint		  mPaint       = null;
	private Drawable 	  mDrawableStart        = null;    //��ʼ��ͼ��
	private Drawable      mDrawableEnd          = null;    //�յ�ͼ��
	private Drawable      mDrawableBus          = null;    //��������ͼ��
	private Drawable      mDrawableSub          = null;    //����ͼ��
	private Drawable      mDrawableWalk         = null;    //����ͼ��
	private Drawable      mDrawableStation      = null;    //վ��ͼ��
	private ArrayList<Point> m_fBusResultPoints = null;
	private TTransitResult mResult = null;
	private int			  mSelected;
	
	public BusOverlay(Context context)
	{
		mPaint = new Paint();
		m_fBusResultPoints = new ArrayList<Point>();
	}

	/**
	 * ��ʼ����ͼ��ʾ��Ҫ����Դ
	 * selected : ��ѡ�еķ���
	 * */
	public void initBus(Context con,TTransitResult result,int selected)
	{
		mResult = result;
		if(mDrawableStart == null)
			mDrawableStart = (Drawable)con.getResources().getDrawable(R.drawable.icon_route_start);
		
		if(mDrawableEnd == null)
			mDrawableEnd = (Drawable)con.getResources().getDrawable(R.drawable.icon_route_end);
		
		if(mDrawableBus == null)
			mDrawableBus = (Drawable)con.getResources().getDrawable(R.drawable.icon_busroute_change_bus);

		if(mDrawableSub == null)
			mDrawableSub = (Drawable)con.getResources().getDrawable(R.drawable.icon_busroute_change_sub);
		
		if(mDrawableWalk == null)
			mDrawableWalk = (Drawable)con.getResources().getDrawable(R.drawable.icon_busroute_change_walk);
		
		if(mDrawableStation == null)
			mDrawableStation = (Drawable)con.getResources().getDrawable(R.drawable.icon_bus_station);
	}
		
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		// TODO Auto-generated method stub
		if(shadow)
			return;
		mPaint.setAntiAlias(true);
		//�����߿�
		mPaint.setStrokeWidth(5);
		mPaint.setStyle(Style.STROKE);	
		drawBusLine(canvas,mapView);
	}
	
	//���滮������·
    public void drawBusLine(Canvas canvas, MapView mapView){
    	m_fBusResultPoints.clear();
		Path busPath = new Path(); 
		ArrayList<TTransitLine> lines = mResult.getTransitLines();
		ArrayList<TTransitSegmentInfo> segs = lines.get(mSelected).getSegmentInfo(); //�õ�һ����·�ĸ���
		for(int i = 0;i < segs.size();i++){  //ÿһ�ο����ж�����·��ȡ��һ��������
			ArrayList<GeoPoint> points = segs.get(i).getSegmentLine().get(0).getShapePoints();
			m_fBusResultPoints = LatlonsToCoordinate(mapView.getProjection(),points);
			int size = m_fBusResultPoints.size();
			busPath.moveTo(m_fBusResultPoints.get(0).x,m_fBusResultPoints.get(0).y);
			for(int j = 0;j < size;j++){
				Point p = m_fBusResultPoints.get(j);
				busPath.lineTo(p.x,p.y);
			}
			busPath.setLastPoint(m_fBusResultPoints.get(size - 1).x, m_fBusResultPoints.get(size - 1).y);
			if(segs.get(i).getType() == TBusRoute.BUS_SEGMENT_TYPE_WALK)
				mPaint.setARGB(178, 120, 5, 3);
			else{
				mPaint.setARGB(204, 0, 174, 255);
			}
			canvas.drawPath(busPath, mPaint);
			busPath.reset();
			
			GeoPoint pos = segs.get(i).getEnd().getPoint();
			pos = segs.get(i).getStart().getPoint();
			Point startPoint = mapView.getProjection().toPixels(pos,null);
			int wid = mDrawableBus.getIntrinsicWidth();
			int height = mDrawableBus.getIntrinsicHeight();
			if(i == 0 ){  //��������ڵ�һ�Σ����������ͼ���ص�
				continue;
			}
			//����
			if(segs.get(i).getType() == TBusRoute.BUS_SEGMENT_TYPE_BUS)
			{
				mDrawableBus.setBounds(startPoint.x - wid/2, startPoint.y - height/2,
						startPoint.x + wid/2, startPoint.y + height/2);
				
				mDrawableBus.draw(canvas);
			}
			//����
			else if(segs.get(i).getType() == TBusRoute.BUS_SEGMENT_TYPE_SUBWAY)
			{
				mDrawableSub.setBounds(startPoint.x - wid/2, startPoint.y - height/2,
						startPoint.x + wid/2, startPoint.y + height/2);
				
				mDrawableSub.draw(canvas);
			}
			//���� 
			else if(segs.get(i).getType() == TBusRoute.BUS_SEGMENT_TYPE_WALK || segs.get(i).getType() == TBusRoute.BUS_SEGMENT_TYPE_SUBWAY_WALK)
			{
				mDrawableWalk.setBounds(startPoint.x - wid/2, startPoint.y - height/2,
						startPoint.x + wid/2, startPoint.y + height/2);
				
				mDrawableWalk.draw(canvas);
			}
		}
		int offsetY = 8;  //ͼ����Ӱƫ��
		int size = lines.get(0).getSegmentInfo().size();
		//�������յ�ͼ��
		GeoPoint start = lines.get(0).getSegmentInfo().get(0).getStart().getPoint();
		GeoPoint end   = lines.get(0).getSegmentInfo().get(size - 1).getEnd().getPoint();
		
		Point startPoint = mapView.getProjection().toPixels(start, null);
		int wid = mDrawableStart.getIntrinsicWidth();
		int height = mDrawableStart.getIntrinsicHeight();
		mDrawableStart.setBounds(startPoint.x - wid/2, startPoint.y - height + offsetY, 
				startPoint.x + wid/2, startPoint.y + offsetY);
		mDrawableStart.draw(canvas);
		
		Point endPoint = mapView.getProjection().toPixels(end, null);
		wid = mDrawableEnd.getIntrinsicWidth();
		height = mDrawableEnd.getIntrinsicHeight();
		mDrawableEnd.setBounds(endPoint.x - wid/2, endPoint.y - height + offsetY, 
				endPoint.x + wid/2, endPoint.y + offsetY);
		mDrawableEnd.draw(canvas);
    }
    
	/*
	 * ��ֱ�ӱ������£��Ѿ�γ��ת������Ļ����ֵ
	 * */
	private ArrayList<Point> LatlonsToCoordinate(Projection proj,ArrayList<GeoPoint> latlons)
	{
		ArrayList<Point> fl = new ArrayList<Point>();
		GeoPoint gPoint = null;
		for(int i=0; i< latlons.size();i++)
		{
			gPoint   = latlons.get(i);
			Point   point = proj.toPixels( gPoint, null);
			gPoint = null;
			fl.add(point);
		}
		return fl;
	}
}
