package com.ourexists.era.framework.rpc.feign;

import com.ourexists.era.framework.core.user.OperatorModel;
import com.ourexists.era.framework.core.user.UserInfo;

import java.util.List;

public class DefaultEraFeignHeaderTransfer implements EraFeignHeaderTransfer {
    @Override
    public String tenantId() {
        return "";
    }

    @Override
    public UserInfo userInfo() {
        return null;
    }

    @Override
    public List<OperatorModel> tenantDataAuth() {
        return List.of();
    }
}
