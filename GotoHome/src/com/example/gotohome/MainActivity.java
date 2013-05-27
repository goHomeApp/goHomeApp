package com.example.gotohome;

import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	//	Toast.makeText(this, "집으로 앱을 시작 하였습니다.", Toast.LENGTH_SHORT).show();
	}
	public void mOnClick(View view) {
		switch(view.getId()) {
		case R.id.StartButton :
			//Toast.makeText(this, "button clicked", Toast.LENGTH_SHORT).show();
			intent = new Intent(this,StartActivity.class);
			startActivity(intent);
			break;
			
		case R.id.ManualButton :
			intent = new Intent(this,ManualActivity.class);
			startActivity(intent);
			break;
			
		case R.id.HomeButton :
			
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
				}
			})
			.setNegativeButton("아니오", null).show();
		}
		return true;
	}	

}
