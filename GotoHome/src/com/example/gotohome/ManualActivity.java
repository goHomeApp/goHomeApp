package com.example.gotohome;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class ManualActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        SlidingView sv = new SlidingView(this); 
        View v1 = View.inflate(this, R.layout.activity_manual1, null); 
        View v2 = View.inflate(this, R.layout.activity_manual2, null); 
        View v3 = View.inflate(this, R.layout.activity_manual3, null);
        sv.addView(v1); 
        sv.addView(v2); 
        sv.addView(v3); 
        setContentView(sv);
	}
}
