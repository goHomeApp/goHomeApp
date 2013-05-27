package com.example.gotohome;

import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class SettingActivity extends Activity {

	Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
	}

	public void mOnClick(View view) {
		switch(view.getId()) {
		case R.id.OkButton :
			intent = new Intent(this,MainActivity.class);
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
			finish();			//탭끼리는 더이상 뒤로갔을경우 필요없으므로
			break;
			
		case R.id.SettingButton :

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
}
