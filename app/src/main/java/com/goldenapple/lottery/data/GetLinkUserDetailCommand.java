package com.goldenapple.lottery.data;

import com.goldenapple.lottery.base.net.RequestConfig;

@RequestConfig(api = "service?packet=User&action=GetLinkUserDetail")
public class GetLinkUserDetailCommand extends CommonAttribute
{
    private int id;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
}
