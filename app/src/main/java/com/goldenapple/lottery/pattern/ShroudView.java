package com.goldenapple.lottery.pattern;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.goldenapple.lottery.R;
import com.goldenapple.lottery.app.GoldenAppleApp;
import com.goldenapple.lottery.component.DiscreteSeekBar;
import com.goldenapple.lottery.component.FlowRadioGroup;
import com.goldenapple.lottery.component.QuantityView;
import com.goldenapple.lottery.component.QuantityView.OnQuantityChangeListener;
import com.goldenapple.lottery.data.UserInfo;
import com.goldenapple.lottery.material.ShoppingCart;

/**
 * 购物车多倍操作
 * Created by ACE-PC on 2016/2/5.
 */
public class ShroudView {

    private static final String TAG = ShroudView.class.getSimpleName();
    private QuantityView doubleText;    //倍数
    private QuantityView chaseAddText;  // 追号
    private OnModeItemChosenListener modeChosenListener;
    private UserInfo userInfo;

    public ShroudView(View view) {
        this.userInfo = GoldenAppleApp.getUserCentre().getUserInfo();
        doubleText = (QuantityView) view.findViewById(R.id.double_number_view);
        chaseAddText = (QuantityView) view.findViewById(R.id.chaseadd_number_view);
        doubleText.setMinQuantity(1);
        doubleText.setMaxQuantity(1000);
        doubleText.setQuantity(ShoppingCart.getInstance().getMultiple());
        chaseAddText.setMinQuantity(0);
        chaseAddText.setQuantity(ShoppingCart.getInstance().getTraceNumber());

        doubleText.setOnQuantityChangeListener(new OnQuantityChangeListener() {

            @Override
            public void onQuantityChanged(int newQuantity, boolean programmatically) {
                if (modeChosenListener != null)
                    modeChosenListener.onModeItemChosen(newQuantity, chaseAddText.getQuantity());
            }

            @Override
            public void onLimitReached() {

            }
        });

        chaseAddText.setOnQuantityChangeListener(new OnQuantityChangeListener() {

            @Override
            public void onQuantityChanged(int newQuantity, boolean programmatically) {
                if (modeChosenListener != null)
                    modeChosenListener.onModeItemChosen(doubleText.getQuantity(), newQuantity);
//                modeItemListener.onModeItemClick(doubleText.getQuantity(), newQuantity, setLucreMode(GoldenAppleApp.getUserCentre().getLucreMode()), appendSettings.isChecked());
            }

            @Override
            public void onLimitReached() {

            }
        });
    }

    public void setModeChosenListener(OnModeItemChosenListener modeChosenListener) {
        this.modeChosenListener = modeChosenListener;
    }

    /**
     * 设置按键最大值
     *
     * @param num
     */
    public void setMaxQuantity(int num) {
        chaseAddText.setMaxQuantity(num);
    }

    /**
     * 设置按键最大值
     *
     * @param num
     */
    public void setChaseTrace(int num) {
        chaseAddText.setQuantity(num);
    }

    /**
     * 选中监听器
     */
    public interface OnModeItemChosenListener{
        void onModeItemChosen(int multiple, int chaseadd);
    }
}
