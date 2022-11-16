package com.example.mapsstreetview.levelMeterPackage;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	public DeviceRotationManager deviceRotationManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LevelViewMeter levelView = new LevelViewMeter(this);
		setContentView(levelView);

		View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(decorView.getSystemUiVisibility()
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

		deviceRotationManager = new DeviceRotationManager(this, levelView::setData);
	}

	@Override
	public void onStart() {
		super.onStart();
		deviceRotationManager.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
		deviceRotationManager.onStop();
	}
}