package org.noahsark.server.rpc;

import java.io.Serializable;

/**
 * Created by hadoop on 2021/3/13.
 */
public class Request implements Serializable {

  private int requestId;

  private String className;

  private String method;

  private String version;

  private Object payload;

  public int getRequestId() {
    return requestId;
  }

  public void setRequestId(int requestId) {
    this.requestId = requestId;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public Object getPayload() {
    return payload;
  }

  public void setPayload(Object payload) {
    this.payload = payload;
  }

  @Override
  public String toString() {
    return "Request{" +
        "requestId=" + requestId +
        ", className='" + className + '\'' +
        ", method='" + method + '\'' +
        ", version='" + version + '\'' +
        ", payload=" + payload +
        '}';
  }
}
