package com.goldenapple.lottery.data;

import com.goldenapple.lottery.base.net.RequestConfig;

@RequestConfig(api = "service?packet=Game&action=GetThirdGabby")
public class GetThirdGabbyCommand extends CommonAttribute  {
    private String identify;

    public void setIdentify(String identify) {
        this.identify = identify;
    }
}
