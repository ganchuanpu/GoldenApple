package com.goldenapple.lottery.data;

import com.goldenapple.lottery.base.net.RequestConfig;

/**
 * Created by Gan on 2017/11/17.
 * 4.5.1	收件箱
 */
@RequestConfig(api = "service?packet=Message&action=GetLettersList")
public class ReceiveBoxCommand extends CommonAttribute{

    private  int page=1;//当前页码，默认=1
    private  int pagesize=20;//每页记录数，默认=10

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }
}
