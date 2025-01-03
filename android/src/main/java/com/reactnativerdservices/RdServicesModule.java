package com.reactnativerdservices;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;

@ReactModule(name = RdServicesModule.NAME)
public class RdServicesModule extends ReactContextBaseJavaModule {

  public static final String NAME = "RdServices";
  public static final int RDINFO_CODE = 1;
  public static final int RDCAPTURE_CODE = 2;
  public static final int FACE_CAPTURE_CODE = 9;
  private final String SUCCESS = "SUCCESS";
  private final String FAILURE = "FAILURE";
  private String PckName = "";
  private String PIDOption = "";
  private Promise promise;

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

      if (requestCode == RDINFO_CODE) {
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

      if (requestCode == RDCAPTURE_CODE) {
        if (data == null) {
          resolve(FAILURE, "Device not ready");
          return;
        }

        String captureXML = data.getStringExtra("PID_DATA");

        if (captureXML == null || captureXML.length() <= 10) {
          resolve(FAILURE, "Device not ready");
          return;
        }
        if (captureXML.toLowerCase().contains("device not ready")) {
          resolve(FAILURE, "Device not ready");
          return;
        }
        resolve(SUCCESS, captureXML);
      }

      if (requestCode == FACE_CAPTURE_CODE) {
        if (data == null) {
          resolve(FAILURE, "Device not ready");
          return;
        }

        String captureXML = data.getStringExtra("response");

        if (captureXML == null || captureXML.length() <= 10) {
          resolve(FAILURE, "Device not ready");
          return;
        }
        if (captureXML.toLowerCase().contains("device not ready")) {
          resolve(FAILURE, "Device not ready");
          return;
        }
        resolve(SUCCESS, captureXML);
      }

    }
  };

  public RdServicesModule(ReactApplicationContext reactContext) {
    super(reactContext);
    reactContext.addActivityEventListener(mActivityEventListener);
  }

  @NonNull
  public String getName() {
    return NAME;
  }



  private void captureData() {
    String pidOption =
      "<?xml version=\"1.0\"?><PidOptions ver=\"1.0\"><Opts fCount=\"1\" fType=\"2\" iCount=\"0\" pCount=\"0\" format=\"0\" pidVer=\"2.0\" timeout=\"10000\" posh=\"UNKNOWN\" env=\"P\" /><CustOpts></CustOpts></PidOptions>";

    if (PIDOption.length() >= 10) {
      pidOption = PIDOption;
    }

    Intent intent = new Intent();
    intent.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
    intent.putExtra("PID_OPTIONS", pidOption);
    intent.setPackage(PckName);

    Activity currentActivity = getCurrentActivity();
    try {
      currentActivity.startActivityForResult(intent, RDCAPTURE_CODE);
    } catch (Exception e) {
      e.printStackTrace();
      resolve(FAILURE, "Selected device not found");
    }
  }

  private void captureFaceData() {
    String pidOption =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <PidOptions ver=\"1.0\" env=\"P\"><Opts fCount=\"\" fType=\"\" iCount=\"\" iType=\"\" pCount=\"\" pType=\"\" format=\"\" pidVer=\"2.0\" timeout=\"\" otp=\"\" wadh=\"sgydIC09zzy6f8Lb3xaAqzKquKe9lFcNR9uTvYxFp+A=\" posh=\"\" /> <CustOpts><Param name=\"txnId\" value=\"76435891\"/><Param name=\"purpose\" value=\"auth\"/><Param name=\"language\" value=\"en\"/></CustOpts></PidOptions>";
    if (PIDOption.length() >= 10) {
      pidOption = PIDOption;
    }

    Intent intent = new Intent();
    intent.setAction("in.gov.uidai.rdservice.face.CAPTURE");
    //intent.setPackage("in.gov.uidai.facerd");
    intent.putExtra("request", pidOption);
    intent.setPackage(PckName);

    Activity currentActivity = getCurrentActivity();
    try {
      currentActivity.startActivityForResult(intent, FACE_CAPTURE_CODE);
    } catch (Exception e) {
      e.printStackTrace();
      resolve(FAILURE, "Selected device not found");
    }
  }

  @ReactMethod
  public void getFingerPrint(String deviceName, String pidOption, Promise prm) {
    try {
      promise = prm;
      PckName = deviceName;
      PIDOption = pidOption;
      captureData();
    } catch (Exception e) {
      e.printStackTrace();
      resolve(FAILURE, "RD services not available");
    }
  }

  @ReactMethod
  public void getFaceCapture(String deviceName, String pidOption, Promise prm) {
    try {
      promise = prm;
      PckName = deviceName;
      PIDOption = pidOption;
      captureFaceData();
    } catch (Exception e) {
      e.printStackTrace();
      resolve(FAILURE, "Face RD services not available");
    }
  }

  private String ParseBioMetricData(String bioxml) {
    bioxml = bioxml.replaceAll("\"", "'");
    bioxml = bioxml.replaceAll("\\n   ", " ");
    bioxml = bioxml.replaceAll("\\n ", " ");

    return bioxml;
  }

  private void resolve(String status, String message) {
    if (promise == null) {
      return;
    }

    WritableMap map = Arguments.createMap();
    map.putString("status", status.toUpperCase());
    map.putString("message", ParseBioMetricData(message));

    promise.resolve(map);
    promise = null;
  }
}
