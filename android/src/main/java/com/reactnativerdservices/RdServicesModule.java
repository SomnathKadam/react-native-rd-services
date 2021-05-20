package com.reactnativerdservices;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.google.gson.Gson;

@ReactModule(name = RdServicesModule.NAME)
public class RdServicesModule extends ReactContextBaseJavaModule {

  public static final String NAME = "RdServices";
  public static final int RDInfo = 1;
  public static final int RDCapture = 2;
  private String devicePackageName = "";
  private Promise promise;
  private String SUCCESS = "SUCCESS";
  private String FAILURE = "FAILURE";
  private String CaptureData = "";

  private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
    @Override
    public void onActivityResult(
      Activity activity,
      int requestCode,
      int resultCode,
      Intent data
    ) {
      if (data == null) {
        resolve(FAILURE, "No action taken");
        return;
      }

      if (requestCode == RDInfo) {
        String requiredValue = data.getStringExtra("RD_SERVICE_INFO");

        if (requiredValue == null) {
          resolve(FAILURE, "Device not ready");
          return;
        }
        if (requiredValue.length() <= 10) {
          resolve(FAILURE, "Device not ready");
          return;
        }
        if (requiredValue.toLowerCase().contains("notready")) {
          resolve(FAILURE, "Device not ready");
          return;
        }

        captureData();
        return;
      }

      if (requestCode == RDCapture && !data.equals(null)) {
        CaptureData = data.getStringExtra("PID_DATA");

        if (CaptureData == null || CaptureData.length() <= 10) {
          resolve(FAILURE, "Device not ready");
          return;
        }
        if (CaptureData.toLowerCase().contains("device not ready")) {
          resolve(FAILURE, "Device not ready");
          return;
        }
        resolve(SUCCESS, CaptureData);
      }
    }
  };

  public RdServicesModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @NonNull
  public String getName() {
    return NAME;
  }

  private void deviceInfo() {
    try {
      Intent intent = new Intent();
      intent.setAction("in.gov.uidai.rdservice.fp.INFO");
      Activity currentActivity = getCurrentActivity();
      currentActivity.startActivityForResult(intent, RDInfo);
    } catch (Exception e) {
      e.printStackTrace();
      resolve(FAILURE, "RD services not available");
    }
  }

  private void captureData() {
    String pidOption =
      "<?xml version=\"1.0\"?><PidOptions ver=\"1.0\"><Opts fCount=\"1\" fType=\"0\" iCount=\"0\" pCount=\"0\" format=\"0\" pidVer=\"2.0\" timeout=\"10000\" posh=\"UNKNOWN\" env=\"P\" /><CustOpts></CustOpts></PidOptions>";
    Intent intent = new Intent();
    intent.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
    intent.putExtra("PID_OPTIONS", pidOption);

    if (devicePackageName.equalsIgnoreCase("com.scl.rdservice")) {
      intent.setPackage("com.scl.rdservice");
    } else if (devicePackageName.equalsIgnoreCase("com.mantra.rdservice")) {
      intent.setPackage("com.mantra.rdservice");
    } else if (
      devicePackageName.equalsIgnoreCase("com.precision.pb510.rdservice")
    ) {
      intent.setPackage("com.precision.pb510.rdservice");
    } else if (devicePackageName.equalsIgnoreCase("com.secugen.rdservice")) {
      intent.setPackage("com.secugen.rdservice");
    } else if (devicePackageName.equalsIgnoreCase("com.acpl.registersdk")) {
      intent.setPackage("com.acpl.registersdk");
    } else if (
      devicePackageName.equalsIgnoreCase("co.aratek.asix_gms.rdservice")
    ) {
      intent.setPackage("co.aratek.asix_gms.rdservice");
    } else {
      resolve(FAILURE, "RD services not available");
    }

    Activity currentActivity = getCurrentActivity();

    currentActivity.startActivityForResult(intent, RDCapture);
  }


  @ReactMethod
  public void getFingerPrint(String deviceName, Promise prm) {
    promise = prm;
    devicePackageName = deviceName;
    deviceInfo();
  }

  private void resolve(String status, String message) {
    if (promise == null) {
      return;
    }
    ResponsePojo responseData = new ResponsePojo();
    try {
      responseData.setStatus(status.toUpperCase());

      responseData.setMessage(message);
    } catch (Exception e) {
      e.printStackTrace();
    }
    promise.resolve(new Gson().toJson(responseData));
    promise = null;
  }
}
