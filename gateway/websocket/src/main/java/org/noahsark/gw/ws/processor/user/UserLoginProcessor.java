package org.noahsark.gw.ws.processor.user;

import java.util.UUID;

import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.rpc.Response;
import org.noahsark.server.rpc.Result;
import org.noahsark.server.rpc.RpcContext;
import org.springframework.stereotype.Component;

/**
 * Created by hadoop on 2021/3/13.
 */
@Component
public class UserLoginProcessor extends AbstractProcessor<UserLoginInfo> {

  @Override
  protected void execute(UserLoginInfo request, RpcContext context) {

    UserLoginResult result = new UserLoginResult();
    result.setToken(UUID.randomUUID().toString());

    Result<UserLoginResult> response = new Result<>();
    response.setCode(0);
    response.setMessage("success");
    response.setData(result);

    context.sendResponse(response);
  }

  @Override
  protected Class<UserLoginInfo> getParamsClass() {
    return UserLoginInfo.class;
  }

  @Override
  protected int getBiz() {
    return 1;
  }

  @Override
  protected int getCmd() {
    return 1000;
  }

}
