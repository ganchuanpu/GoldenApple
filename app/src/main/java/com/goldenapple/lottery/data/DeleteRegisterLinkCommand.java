package com.goldenapple.lottery.data;

import com.android.volley.Request;
import com.goldenapple.lottery.base.net.RequestConfig;

@RequestConfig(api = "service?packet=User&action=DeleteRegisterLink", method = Request.Method.POST)
public class DeleteRegisterLinkCommand extends CommonAttribute
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
