package com.colin.permissions.dispatcher;

import android.support.v4.app.ActivityCompat;

import com.inthecheesefactory.lab.intent_fileprovider.MainActivity;

import java.lang.ref.WeakReference;


/**
 * Created by zhilian on 2018/1/18.
 */

public class MainActivityPermissionsDispatcher {
	private static final int REQUEST_STARTCAMERA = 0;

	private static final String[] PERMISSION_STARTCAMERA = new String[] {"android.permission.WRITE_EXTERNAL_STORAGE"};

	private MainActivityPermissionsDispatcher() {
	}

	public static void startCameraWithCheck(MainActivity target) {
		if (PermissionUtils.hasSelfPermissions(target, PERMISSION_STARTCAMERA)) {
			target.startCamera();
		} else {
			if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_STARTCAMERA)) {
				target.showRationaleForCamera(new StartCameraPermissionRequest(target));
			} else {
				ActivityCompat.requestPermissions(target, PERMISSION_STARTCAMERA, REQUEST_STARTCAMERA);
			}
		}
	}

	public static void onRequestPermissionsResult(MainActivity target, int requestCode, int[] grantResults) {
		switch (requestCode) {
			case REQUEST_STARTCAMERA:
				if (PermissionUtils.getTargetSdkVersion(target) < 23 && !PermissionUtils.hasSelfPermissions(target, PERMISSION_STARTCAMERA)) {
					return;
				}
				if (PermissionUtils.verifyPermissions(grantResults)) {
					target.startCamera();
				}
				break;
			default:
				break;
		}
	}

	private static final class StartCameraPermissionRequest implements PermissionRequest {
		private final WeakReference<MainActivity> weakTarget;

		private StartCameraPermissionRequest(MainActivity target) {
			this.weakTarget = new WeakReference<>(target);
		}

		@Override
		public void proceed() {
			MainActivity target = weakTarget.get();
			if (target == null) return;
			ActivityCompat.requestPermissions(target, PERMISSION_STARTCAMERA, REQUEST_STARTCAMERA);
		}

		@Override
		public void cancel() {
		}
	}
}
