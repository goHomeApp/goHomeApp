package com.example.gotohome;

import java.io.IOException;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.os.Message;
import android.telephony.SmsManager;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;


public class SmsService extends Service{

	int timer;
	SharedPreferences pref;
	String strPhoneNum;
	String strTimeNum;
	String strAddress;
	String strViewAddress;

	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	    
		pref = getSharedPreferences("Activity_Start",0);
		strPhoneNum = pref.getString("PhoneNum","0000");
		strTimeNum = pref.getString("TimeNum","5");
		timer = Integer.parseInt(strTimeNum);

	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Toast.makeText(SmsService.this, "Exit", Toast.LENGTH_SHORT).show();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		super.onStartCommand(intent, flags, startId);
		ThreadExam thread = new ThreadExam(this,mHandler);
		thread.setDaemon(true);
		thread.start();
		
		return START_NOT_STICKY;			//프로세스를 종료하면 서비스도 종료
	}

	class ThreadExam extends Thread {		//서비스를 백그라운드로 돌리기 위해 쓰레드 생성
		SmsService mParent;
		Handler mHandler;
		
		public ThreadExam(SmsService mParent,Handler mHandler) {
			this.mParent = mParent;
			this.mHandler = mHandler;
		}
		public void run() {							
			int i;

			try { 									//처음부터 바로 보내는 것이 아니기 때문에
				Thread.sleep(timer); 
			} catch (Exception e) {;}
			
			for(i=0;i<2;i++) {
				strAddress = pref.getString("Address","위치 알수 없음");
				strViewAddress = pref.getString("ViewAddress", "잘못된 링크 주소");
				sendSMS(strPhoneNum,strAddress);	//일정한 시간간격으로 문자를 보냄
				sendSMS(strPhoneNum,strViewAddress);
				try { 
					Thread.sleep(timer); 
				} catch (Exception e) {;}
			}
		}
		
	}
	
	
	private void sendSMS(String phoneNumber, String message) {

		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
				SENT), 0);

		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
				new Intent(DELIVERED), 0);

		// ---when the SMS has been sent---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS sent",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(getBaseContext(), "Generic failure",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(getBaseContext(), "No service",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(getBaseContext(), "Null PDU",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(getBaseContext(), "Radio off",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(SENT));

		// ---when the SMS has been delivered---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS delivered",
							Toast.LENGTH_SHORT).show();
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(getBaseContext(), "SMS not delivered",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(DELIVERED));

		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
	}

	Handler mHandler = new Handler() {			//쓰레드 동기화 문제로 인해 쓰레드의 값을 바로 출력하기 위해서는 핸들러를 이용하여 뷰에다 출력
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				String temp = (String)msg.obj;
			//	Toast.makeText(SmsService.this, address, Toast.LENGTH_SHORT).show();
			}
		}
	};
}