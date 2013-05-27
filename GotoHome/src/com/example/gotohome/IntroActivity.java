package com.example.gotohome;

import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;

public class IntroActivity extends Activity {

	//boolean providerCheck = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		
		//LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);	//자기 위치 알아 내는 서비스매니저 생성
	   // String provider = locationManager.getBestProvider(new Criteria(), true);	//현재 자기가 사용 할 수 있는 최고의 위치추적기 알아냄
	  /*  if(!locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER)) {
	    	providerCheck = true;
	    }
	    else if(!locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {	//GPS가 꺼져있다면 나중에 프로바이더를 GPS로 바꾸어야함
			AlertDialog.Builder gpsAlert = new AlertDialog.Builder(this);
			gpsAlert.setTitle("Location Manager");
			gpsAlert.setMessage("현재 GPS가 꺼져 있습니다.\n 설정화면으로 이동하시겠습니까?");
			gpsAlert.setPositiveButton("예", new DialogInterface.OnClickListener() {
				///예를 클릭했을경우 설정화면으로
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(i);
				}
			});
			gpsAlert.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			gpsAlert.create().show();
			
			Intent intent = new Intent(IntroActivity.this,MainActivity.class);
			startActivity(intent);
			/*
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				public void run() {
					Intent intent = new Intent(IntroActivity.this,MainActivity.class);
					while(true) {
						if(providerCheck == true) {
							startActivity(intent);
							finish();
							break;
						}
					}
				}
			}, 2000);
			
		}*/
		
	 //   if(providerCheck == true) {
	    	Handler handler = new Handler();
	    	handler.postDelayed(new Runnable() {
	    		public void run() {
	    			Intent intent = new Intent(IntroActivity.this,MainActivity.class);
	    			startActivity(intent);
	    			finish();
	    		}
	    	}, 2000);
	 //   }
	}
}
