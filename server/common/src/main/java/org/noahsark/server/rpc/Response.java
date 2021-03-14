package org.noahsark.server.rpc;

/**
 * Created by hadoop on 2021/3/13.
 */
public class Response<R>  {

  private int code;

  private String message;

  private R payload;

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public R getPayload() {
    return payload;
  }

  public void setPayload(R payload) {
    this.payload = payload;
  }

  @Override
  public String toString() {
    return "Response{" +
        "code=" + code +
        ", message='" + message + '\'' +
        ", payload=" + payload +
        '}';
  }
}
