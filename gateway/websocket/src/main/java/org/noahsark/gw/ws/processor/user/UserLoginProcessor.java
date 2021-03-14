package org.noahsark.gw.ws.processor.user;

import java.util.UUID;

import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.rpc.Response;
import org.springframework.stereotype.Component;

/**
 * Created by hadoop on 2021/3/13.
 */
@Component
public class UserLoginProcessor extends AbstractProcessor<UserLoginInfo,UserLoginResult> {

  @Override
  protected Response<UserLoginResult> execute(UserLoginInfo request) {

    UserLoginResult result = new UserLoginResult();
    result.setToken(UUID.randomUUID().toString());

    Response<UserLoginResult> response = new Response<>();
    response.setCode(0);
    response.setMessage("success");
    response.setPayload(result);

    return response;
  }

  @Override
  protected Class<UserLoginInfo> getParamsClass() {
    return UserLoginInfo.class;
  }

  @Override
  protected String getClassName() {
    return "user";
  }

  @Override
  protected String getMethod() {
    return "login";
  }
}
