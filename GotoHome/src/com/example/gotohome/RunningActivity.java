package com.example.gotohome;

import java.io.IOException;
import java.util.List;

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
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RunningActivity extends Activity {

	MediaPlayer player;
	Button alarmButton;
	Button ExitButton;
	boolean runCheck;
	String strAddress;
	String strViewAddress;
	String strPhoneNum;
	String urgencyMessage;
	KakaoLink kakaoLink;
	String encoding;
	SharedPreferences pref;
	SmsManager sms;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_running);
	    // TODO Auto-generated method stub

	    alarmButton = (Button) findViewById(R.id.AlarmButton);   
	    ExitButton =  (Button) findViewById(R.id.ExitButton);
	    runCheck = false;
	    pref = getSharedPreferences("Activity_Start",0);
		sms = SmsManager.getDefault();
		
		kakaoLink = KakaoLink.getLink(getApplicationContext());
		
		encoding = "UTF-8";
		// check, intent is available.
		if (!kakaoLink.isAvailableIntent()) {
			alert("Not installed KakaoTalk.");			
			return;
		}
		
		strAddress = pref.getString("Address","위치 알수 없음");
		strViewAddress = pref.getString("ViewAddress", "잘못된 링크 주소");
		strPhoneNum = pref.getString("PhoneNum","0000");
		urgencyMessage = "[집으로]\n긴급 상황입니다.\n" + strViewAddress;
		
	    try {
			kakaoLink.openKakaoLink(this, 
					strViewAddress, 	//링크
					strAddress, //메시지
					getPackageName(), //이것도 역시나
					getPackageManager().getPackageInfo(getPackageName(), 0).versionName, //고대로 하면 됨
					"안전 귀가 알리미", //제목
					encoding);//이것도
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}
	
	
	public void mOnClick(View view) {
		switch(view.getId()) {
		case R.id.AlarmButton :
			if(runCheck == false) {
				player = MediaPlayer.create(this, R.raw.alarm);
				player.start();
				player.setLooping(true);
				sms.sendTextMessage(strPhoneNum, null, urgencyMessage, null, null);
				runCheck = true;
			}else if(runCheck == true) {
				player.setLooping(false);
				player.release();
				runCheck = false;
			}
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
					if(runCheck == true) {
						player.setLooping(false);
						player.release();
						runCheck = false;
					}
				}
			})
			.setNegativeButton("아니오", null).show();
		/*	if(runCheck == true) {
				player.setLooping(false);
				player.release();
				runCheck = false;
			}*/
		//	Intent intent = new Intent(this,SmsService.class);
		//	stopService(intent);
		//	intent = new Intent(this,MainActivity.class);
		//	startActivity(intent);
			break;
		}
	}	
	public boolean onKeyDown(int keyCode,KeyEvent event) {
		switch(keyCode) {
		case KeyEvent.KEYCODE_BACK :
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
					if(runCheck == true) {
						player.setLooping(false);
						player.release();
						runCheck = false;
					}
				}
			})
			.setNegativeButton("아니오", null).show();
		}
		return true;
	}	
	private void alert(String message) {
		new AlertDialog.Builder(this)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle(R.string.app_name)
			.setMessage(message)
			.setPositiveButton(android.R.string.ok, null)
			.create().show();
	}
}
	

