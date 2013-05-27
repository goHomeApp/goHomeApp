package com.example.gotohome;

import java.io.IOException;
import java.util.List;

import com.example.gotohome.SmsService;
import com.example.gotohome.SmsService.ThreadExam;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends Activity {

	Intent intent;
	EditText txtPhoneNum;
	EditText txtTimeNum;
	String strPhoneNum;
	String strTimeNum;
	LocationManager locationManager;
	String provider;
	String strAddress;
	String strViewAddress;
	List<Address> addr;
	Geocoder coder;
	Location location;
	StringBuilder address;
	StringBuilder viewAddress;
	SharedPreferences pref;
	int count;
	SharedPreferences.Editor edit;
	double latitude;
	double longitude;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_start);
	    // TODO Auto-generated method stub
	   
	    locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);	//자기 위치 알아 내는 서비스매니저 생성
	    provider = locationManager.getBestProvider(new Criteria(), true);	//현재 자기가 사용 할 수 있는 최고의 위치추적기 알아냄
	    coder = new Geocoder(this);					//위도와 경도의 숫자로 표시되어있는데 그것을 우리가 알아 볼 수 있게 한글로 바꾸어 줌
	    pref = getSharedPreferences("Activity_Start",0);	//저장을 위한 프레퍼런스 생성
	    edit = pref.edit();						//저장을 위한 프레퍼런스 에딧 생성
	    count = 0;								//수신 횟수를 알아보기 위해 카운트 생성
	    address = new StringBuilder();			//이쁘게 글을 집어 넣기위해서
	    viewAddress = new StringBuilder();		//링크주소를 집어넣기 위한 변수 생성
	}
	
	public void onResume() {
		super.onResume();
		if(!locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER)) {
			
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
    }
		locationManager.requestLocationUpdates(provider, 20000, 0, listener);	//20초 간격으로 자기 위치 알아옴
		Toast.makeText(this,"보내고자 하는 번호와 시간간격을 입력해 주세요.", Toast.LENGTH_SHORT).show();

	}
	
	public void onPause() {
		super.onPause();	

	}
	
	LocationListener listener = new LocationListener() {
		public void onLocationChanged(Location location) {
			try {		
				//address = new StringBuilder();
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				
				///////////한글 주소를 넣어줌///////////////////////
				count++;
				addr = coder.getFromLocation(latitude, longitude, 1);
				address.append("[집으로]\n지금 ");
				address.append(addr.get(0).getAdminArea()).append(" ");
				address.append(addr.get(0).getLocality()).append(" ");
				address.append(addr.get(0).getThoroughfare()).append(" ");
				address.append(addr.get(0).getFeatureName()).append(" ");
				address.append("에 있습니다.\n(위치보기)\n");
				address.append("수신횟수: " + count).append(" ");
				
				////////////링크 주소를 넣어줌/////////////////////////////
				viewAddress.append("http://maps.google.com/maps?q=");//처음에 들어감
				viewAddress.append(latitude);			//위도
				viewAddress.append("%20");				//위도와 경도사이에 들어감
				viewAddress.append(longitude);			//경도
				viewAddress.append("&z=17");			//보이는 크기를 17정도
				strAddress = address.toString();
				strViewAddress = viewAddress.toString();
				
				edit.putString("ViewAddress", strViewAddress);
				edit.putString("Address", strAddress);	
				edit.commit();					//동기화를 위해 커밋 시켜주어야 함
				address.delete(0, 80);			//계속해서 append해버리면 sms메시지에 쓸때없는 내용까지 보내게 되므로
				viewAddress.delete(0, 80);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		public void onProviderDisabled(String provider) {
			
		}
		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
		
	};
	

	public void mOnClick(View view) {
		switch(view.getId()) {
		case R.id.OkButton :
			
			SharedPreferences pref = getSharedPreferences("Activity_Start",0);
		//	SharedPreferences.Editor edit = pref.edit();
			txtPhoneNum = (EditText) this.findViewById(R.id.PhoneNumEditText);
			txtTimeNum = (EditText) this.findViewById(R.id.TimeNumEditText);
			
			strPhoneNum = txtPhoneNum.getText().toString();
			strTimeNum = txtTimeNum.getText().toString();
			
			int timer = Integer.parseInt(strTimeNum);
			timer = timer*1000*60;	// sleep함수를 사용하기 위해 1000을 곱하고 1000은 초이기 때문에 60을 곱하여 분으로 만듦
			
			strTimeNum = Integer.toString(timer);
				
			edit.putString("PhoneNum", strPhoneNum);
			edit.putString("TimeNum", strTimeNum);
			
			edit.commit();

			//Toast.makeText(this,strAddress, Toast.LENGTH_SHORT).show();
			
			Toast.makeText(this, "서비스를 시작합니다.", Toast.LENGTH_SHORT).show();
			onStartService(view);
			final Context thisActivity = (Context)this;
			intent = new Intent(thisActivity,RunningActivity.class);
			startActivity(intent);
			break;
			
		case R.id.HomeButton :
			intent = new Intent(this,MainActivity.class);
			startActivity(intent);
			break;
			
		case R.id.MapButton :
			
			break;
			
		case R.id.InformationButton :
			intent = new Intent(this,InfoActivity.class);
			startActivity(intent);
			break;
			
		case R.id.SettingButton :
			intent = new Intent(this,SettingActivity.class);
			startActivity(intent);
			break;
			
		case R.id.ExitButton :
			String alertTitle = "종료";
			new AlertDialog.Builder(this).setTitle(alertTitle)
			.setMessage("프로그램을 완전히 종료합니다.\n종료 하시겠습니까?")
			.setPositiveButton("예",		 new DialogInterface.OnClickListener() {
				@SuppressWarnings("deprecation")
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					moveTaskToBack(true);
					finish();
					ActivityManager activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
					activityManager.restartPackage(getPackageName());
				}
			})
			.setNegativeButton("아니오", null).show();
			break; 

		}
		
	}
	
	public void onStartService(View view) {		//서비스 실행
		Intent intent = new Intent(this,SmsService.class);
		startService(intent);
	}
}

