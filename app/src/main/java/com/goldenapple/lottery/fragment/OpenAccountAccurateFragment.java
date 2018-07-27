package com.goldenapple.lottery.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.goldenapple.lottery.R;
import com.goldenapple.lottery.app.LazyBaseFragment;
import com.goldenapple.lottery.base.net.GsonHelper;
import com.goldenapple.lottery.base.net.RestCallback;
import com.goldenapple.lottery.base.net.RestRequest;
import com.goldenapple.lottery.base.net.RestRequestManager;
import com.goldenapple.lottery.base.net.RestResponse;
import com.goldenapple.lottery.component.CustomDialog;
import com.goldenapple.lottery.component.DialogLayout;
import com.goldenapple.lottery.data.AccurateUserCommand;
import com.goldenapple.lottery.data.GetUserAccurateInfoCommand;
import com.goldenapple.lottery.data.PrizeGroupChild;
import com.goldenapple.lottery.data.QuotaBean;
import com.goldenapple.lottery.data.UserAccurateInfo;
import com.goldenapple.lottery.view.adapter.QuotaAdapter;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * 精准开户
 */

public class OpenAccountAccurateFragment extends LazyBaseFragment
{
    private static final String TAG = OpenAccountAccurateFragment.class.getSimpleName();
    @BindView(R.id.proxy)
    RadioButton proxy;
    @BindView(R.id.user)
    RadioButton user;
    @BindView(R.id.user_type)
    RadioGroup userType;
    @BindView(R.id.user_name)
    EditText userName;
    @BindView(R.id.user_password)
    EditText userPassword;
    @BindView(R.id.nick_name)
    EditText nickName;
    @BindView(R.id.bonus_counts)
    EditText bonusCounts;
    @BindView(R.id.bonus_rebate)
    TextView bonusRebate;
    @BindView(R.id.bonus_danguan)
    EditText bonusDanguan;
    @BindView(R.id.bonus_hunhe)
    EditText bonusHunhe;
    @BindView(R.id.bonus_ag)
    EditText bonusAg;
    @BindView(R.id.bonus_game)
    EditText bonusGame;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.rebates_setting_btn)
    Button rebatesSettingBtn;
    Unbinder unbinder;
    
    private static final int GET_USER_INFO_COMMAND = 1;
    private static final int SUBMIT_COMMAND = 2;
    
    private QuotaAdapter quotaAdapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflateView(inflater, container, true, "精准开户", R.layout.fragment_open_account_accurate, true, true);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }
    
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        init();
    }
    
    private void init()
    {
        user.setVisibility(View.INVISIBLE);
        quotaAdapter = new QuotaAdapter();
        
        GetUserAccurateInfoCommand command = new GetUserAccurateInfoCommand();
        TypeToken typeToken = new TypeToken<RestResponse<UserAccurateInfo>>() {};
        RestRequest restRequest = RestRequestManager.createRequest(getActivity(), command, typeToken, restCallback,
                GET_USER_INFO_COMMAND, this);
        restRequest.execute();
    }
    
    @OnClick(R.id.rebates_setting_btn)
    public void onViewClicked()
    {
        if (verifyInput())
        {
            String type;
            if (proxy.isChecked())
                type = "代理";
            else if (user.isChecked())
                type = "普通用户";
            else
                type = "";
            /*stringBuilder.append("用户类型：" + type + "   ");
            stringBuilder.append("登录账号:" + userName.getText().toString() + "\n");
            stringBuilder.append("登录密码:" + userPassword.getText().toString() + "   ");
            stringBuilder.append("用户昵称:" + nickName.getText().toString() + "\n");
            stringBuilder.append("数字彩奖金组:" + bonusCounts.getText().toString() + "\n");
            stringBuilder.append("竞彩单关：" + bonusDanguan.getText().toString() + "%   ");
            stringBuilder.append("竞彩混关:" + bonusHunhe.getText().toString() + "%\n");
            stringBuilder.append("AG游戏：" + bonusAg.getText().toString() + "%   ");
            stringBuilder.append("GA游戏：" + bonusAg.getText().toString() + "%\n");*/
            
            CustomDialog.Builder builder = new CustomDialog.Builder(getContext());
            View displayView = LayoutInflater.from(getContext()).inflate(R.layout.alert_dialog_reg_confirm_layout,
                    null);
            builder.setDisplayLayout(displayView);
            TextView userType = (TextView) displayView.findViewById(R.id.user_type);
            TextView account = (TextView) displayView.findViewById(R.id.account);
            TextView pwd = (TextView) displayView.findViewById(R.id.pwd);
            TextView nick = (TextView) displayView.findViewById(R.id.nick);
            TextView numPrizeGroup = (TextView) displayView.findViewById(R.id.num_prize_group);
            TextView jcdg = (TextView) displayView.findViewById(R.id.jcdg);
            TextView jchg = (TextView) displayView.findViewById(R.id.jchg);
            TextView ag = (TextView) displayView.findViewById(R.id.ag);
            TextView ga = (TextView) displayView.findViewById(R.id.ga);
            userType.setText("用户类型：" + type);
            account.setText("登录账号:" + userName.getText().toString());
            pwd.setText("登录密码:" + userPassword.getText().toString());
            nick.setText("登录账号:" + nickName.getText().toString());
            numPrizeGroup.setText("数字彩奖金组:" + bonusCounts.getText().toString());
            jcdg.setText("竞彩单关:" + bonusDanguan.getText().toString());
            jchg.setText("竞彩混关:" + bonusHunhe.getText().toString());
            ag.setText("AG游戏:" + bonusAg.getText().toString());
            ga.setText("GA游戏:" + bonusGame.getText().toString());
            //builder.setMessage(stringBuilder.toString());
            builder.setLayoutSet(DialogLayout.SINGLE);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    AccurateUserCommand accurateUserCommand = new AccurateUserCommand();
                    accurateUserCommand.setUsername(userName.getText().toString());
                    accurateUserCommand.setNickname(nickName.getText().toString());
                    accurateUserCommand.setPassword(userPassword.getText().toString());
                    accurateUserCommand.setIs_agent(proxy.isChecked() ? 1 : 0);
                    HashMap<String, String> map = new HashMap<>();
                    map.put("1", bonusCounts.getText().toString());
                    accurateUserCommand.setSeries_prize_group_json(GsonHelper.toJson(map));
                    accurateUserCommand.setAgent_prize_set_quota(Integer.parseInt(bonusCounts.getText().toString()) <
                            1950 ? null : GsonHelper.toJson(quotaAdapter.getResultMap()));
                    accurateUserCommand.setFb_single(Float.parseFloat(bonusDanguan.getText().toString()));
                    accurateUserCommand.setFb_all(Float.parseFloat(bonusHunhe.getText().toString()));
                    accurateUserCommand.setAg_percent(Float.parseFloat(bonusAg.getText().toString()));
                    accurateUserCommand.setGa_percent(Float.parseFloat(bonusGame.getText().toString()));
                    
                    executeCommand(accurateUserCommand, restCallback, SUBMIT_COMMAND);
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }
    }
    
    private boolean verifyInput()
    {
        if (TextUtils.isEmpty(userName.getText().toString()))
        {
            Toast.makeText(getContext(), "请输入用户名", Toast.LENGTH_LONG).show();
            return false;
        }
        
        if (!(userName.getText().toString().charAt(0) <= 'Z' && userName.getText().toString().charAt(0) >= 'A' ||
                userName.getText().toString().charAt(0) <= 'z' && userName.getText().toString().charAt(0) >= 'a'))
        {
            Toast.makeText(getContext(), "用户名必须以字母开头", Toast.LENGTH_LONG).show();
            return false;
        }
        
        String userNameReg = "^[A-Za-z0-9_]+$";//英文和数字
        Pattern pAll = Pattern.compile(userNameReg);
        Matcher mAll = pAll.matcher(userName.getText().toString());
        if (!mAll.matches())
        {
            Toast.makeText(getContext(), "用户名只能是字母或者数字或者下划线", Toast.LENGTH_LONG).show();
            return false;
        }
        
        if (userName.getText().toString().length() > 16 || userName.getText().toString().length() < 5)
        {
            Toast.makeText(getContext(), "用户名只能是长度为5-16位", Toast.LENGTH_LONG).show();
            return false;
        }
        
        //// 清除掉所有特殊字符
        //        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        //        Pattern p = Pattern.compile(regEx);
        //        Matcher matcher = p.matcher(nickname.getText().toString());
        //        if(matcher .find()){
        //            showToast("昵称不能包含特殊字符", Toast.LENGTH_SHORT);
        //            return false;
        //        }
        
        if (hasEmoji(nickName.getText().toString()))
        {
            showToast("昵称不能包含Emoji表情", Toast.LENGTH_SHORT);
            return false;
        }
        
        if (TextUtils.isEmpty(userPassword.getText().toString()))
        {
            Toast.makeText(getContext(), "请输入密码", Toast.LENGTH_LONG).show();
            return false;
        }
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";
        if (!userPassword.getText().toString().matches(regex))
        {
            Toast.makeText(getActivity(), "密码格式不正确", Toast.LENGTH_LONG).show();
            return false;
        }
        
        if (TextUtils.isEmpty(nickName.getText().toString()))
        {
            Toast.makeText(getContext(), "请输入昵称", Toast.LENGTH_LONG).show();
            return false;
        }
        
        if (TextUtils.isEmpty(bonusCounts.getText().toString()))
        {
            Toast.makeText(getContext(), "请输入奖金组", Toast.LENGTH_LONG).show();
            return false;
        }
        
        if (TextUtils.isEmpty(bonusDanguan.getText().toString()) || TextUtils.isEmpty(bonusHunhe.getText().toString()
        ) || TextUtils.isEmpty(bonusAg.getText().toString()) || TextUtils.isEmpty(bonusGame.getText().toString()))
        {
            Toast.makeText(getContext(), "请输入返点", Toast.LENGTH_LONG).show();
            return false;
        }
        
        return true;
    }
    
    //判断字符串中是否含Emoji表情正则表达式
    public boolean hasEmoji(String content)
    {
        //过滤Emoji表情
        //        Pattern p = Pattern.compile("[^\\u0000-\\uFFFF]");
        //过滤Emoji表情和颜文字
        //        Pattern p = Pattern.compile
        // ("[\\ud83c\\udc00-\\ud83c\\udfff]|[\\ud83d\\udc00-\\ud83d\\udfff]|[\\u2600-\\u27ff]|[\\ud83e\\udd00
        // -\\ud83e\\uddff]|[\\u2300-\\u23ff]|[\\u2500-\\u25ff]|[\\u2100-\\u21ff]|[\\u0000-\\u00ff]|[\\u2b00-\\u2bff
        // ]|[\\u2d06]|[\\u3030]");
        //禁用emoji表情和颜文字
        Pattern p = Pattern.compile("[^a-zA-Z0-9\\u4E00-\\u9FA5_,.?!:;…~_\\-\"\"/@*+'()<>{}/[/]()" +
                "<>{}\\[\\]=%&$|\\/♀♂#¥£¢€\"^` " +
                "，。？！：；……～“”、“（）”、（——）‘’＠‘·’＆＊＃《》￥《〈〉》〈＄〉［］￡［］｛｝｛｝￠【】【】％〖〗〖〗／〔〕〔〕＼『』『』＾「」「」｜﹁﹂｀．]");
        //判断字符串中是否含Emoji表情正则表达式
        //        Pattern p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff
        // ]");
        Matcher matcher = p.matcher(content);
        if (matcher.find())
        {
            return true;
        }
        return false;
    }
    
    private RestCallback restCallback = new RestCallback()
    {
        @Override
        public boolean onRestComplete(RestRequest request, RestResponse response)
        {
            switch (request.getId())
            {
                case GET_USER_INFO_COMMAND:
                    if (response.getData() != null && response.getData() instanceof UserAccurateInfo)
                    {
                        UserAccurateInfo userAccurateInfo = (UserAccurateInfo) response.getData();
                        if (userAccurateInfo.isIs_top_agent() == 0)
                            user.setVisibility(View.VISIBLE);
                        bonusCounts.addTextChangedListener(new TextWatcher()
                        {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
                            {
                            
                            }
                            
                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
                            {
                                if (!TextUtils.isEmpty(charSequence))
                                {
                                    if (Integer.parseInt(charSequence.toString()) < 1950)
                                    {
                                        if (proxy.isChecked())
                                        {
                                            ArrayList<PrizeGroupChild> allList = userAccurateInfo
                                                    .getAllPossibleAgentPrizeGroups();
                                            if (allList != null && allList.size() != 0)
                                                for (PrizeGroupChild prizeGroupChild : allList)
                                                {
                                                    if (prizeGroupChild.getName().equals(charSequence.toString()))
                                                    {
                                                        float returnPoint = ((float) userAccurateInfo
                                                                .getPossibleAgentPrizeGroup() - Float.parseFloat
                                                                (charSequence.toString())) / 2000l;
                                                        //returnPoint = (float) (Math.round((returnPoint * 1000 /
                                                        // 1000)));
                                                        bonusRebate.setText("对应返点" + returnPoint + "%");
                                                    }
                                                }
                                        } else
                                        {
                                            ArrayList<PrizeGroupChild> allList = userAccurateInfo
                                                    .getAllPossiblePrizeGroups();
                                            if (allList != null && allList.size() != 0)
                                                for (PrizeGroupChild prizeGroupChild : allList)
                                                {
                                                    if (prizeGroupChild.getName().equals(charSequence.toString()))
                                                    {
                                                        float returnPoint = ((float) userAccurateInfo
                                                                .getPossiblePlayerPrizeGroup() - Float.parseFloat
                                                                (charSequence.toString())) / 2000l;
                                                        //returnPoint = (float) (Math.round((returnPoint * 1000 /
                                                        // 1000)));
                                                        bonusRebate.setText("对应返点" + returnPoint + "%");
                                                    }
                                                }
                                        }
                                    } else
                                    {
                                        LinkedHashMap<Integer, Integer> quotaMap = userAccurateInfo
                                                .getUserAllPrizeSetQuota();
                                        if (quotaMap == null || quotaMap.size() == 0)
                                        {
                                            return;
                                        }
                                        ArrayList<QuotaBean> quotaList = new ArrayList<>();
                                        for (Map.Entry<Integer, Integer> entry : quotaMap.entrySet())
                                        {
                                            QuotaBean quotaBean = new QuotaBean();
                                            quotaBean.setQuota(entry.getKey());
                                            quotaBean.setMax(entry.getValue());
                                            quotaList.add(quotaBean);
                                        }
                                        CustomDialog.Builder builder = new CustomDialog.Builder(getContext());
                                        View displayView = LayoutInflater.from(getContext()).inflate(R.layout
                                                .alert_dialog_prize_groups_layout, null);
                                        ListView listView = displayView.findViewById(R.id.list_view);
                                        listView.setAdapter(quotaAdapter);
                                        quotaAdapter.setData(quotaList);
                                        builder.setDisplayLayout(displayView);
                                        builder.setTitle("设置开户配额");
                                        builder.setLayoutSet(DialogLayout.SINGLE);
                                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i)
                                            {
                                                dialogInterface.dismiss();
                                            }
                                        });
                                        builder.create().show();
                                    }
                                }
                            }
                            
                            @Override
                            public void afterTextChanged(Editable editable)
                            {
                            
                            }
                        });
                    }
                    break;
                case SUBMIT_COMMAND:
                    showToast("开户成功", Toast.LENGTH_LONG);
                    break;
            }
            return true;
        }
        
        @Override
        public boolean onRestError(RestRequest request, int errCode, String errDesc)
        {
            if (errCode == 3004 || errCode == 2016)
            {
                signOutDialog(getActivity(), errCode);
                return true;
            } else
            {
                showToast(errDesc);
            }
            return false;
        }
        
        @Override
        public void onRestStateChanged(RestRequest request, @RestRequest.RestState int state)
        {
        }
    };
    
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        unbinder.unbind();
    }
}