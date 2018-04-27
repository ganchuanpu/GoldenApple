package com.goldenapple.lottery.data;

import com.android.volley.Request;
import com.goldenapple.lottery.app.GoldenAppleApp;
import com.goldenapple.lottery.base.net.RequestConfig;

/**
 * 获取自己绑定卡的信息
 * Created by Alashi on 2016/3/17.
 */
@RequestConfig(api = "service?packet=User&action=getBankCardList")
public class CardDetailCommand extends CommonAttribute{}
