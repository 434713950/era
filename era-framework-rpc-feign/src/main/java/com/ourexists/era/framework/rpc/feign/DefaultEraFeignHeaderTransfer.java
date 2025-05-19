package com.ourexists.era.framework.rpc.feign;

import com.ourexists.era.framework.core.user.*;

import java.util.List;
import java.util.Map;

public class DefaultEraFeignHeaderTransfer implements EraFeignHeaderTransfer {

    @Override
    public String tenantId() {
        return UserContext.getTenant().getTenantId();
    }

    @Override
    public Map<String, String> requestHeaders() {
        return UserContext.getRequestHeader();
    }

    @Override
    public UserInfo userInfo() {
        return UserContext.getUser();
    }

    @Override
    public List<OperatorModel> tenantDataAuth() {
        TenantInfo tenantInfo = UserContext.getTenant();
        if (tenantInfo == null) {
            return null;
        }
        TenantDataAuth tenantDataAuth = tenantInfo.getTenantDataAuth();
        if (tenantDataAuth == null) {
            return null;
        }
        return tenantDataAuth.getLowerControlPower();
    }
}
