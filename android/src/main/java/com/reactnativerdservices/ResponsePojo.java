package com.reactnativerdservices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponsePojo {

  public ResponsePojo() {}

  @SerializedName("message")
  @Expose
  private String message;

  @SerializedName("status")
  @Expose
  private String status;

  public String getMessage() {
    return message == null ? "" : message.trim();
  }

  public void setMessage(String message) {
    this.message = message == null ? "" : message.trim();
  }

  public String getStatus() {
    return status == null ? "" : status.trim();
  }

  public void setStatus(String status) {
    this.status = status == null ? "" : status.trim();
  }
}
