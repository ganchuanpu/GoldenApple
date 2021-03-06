package com.goldenapple.lottery.pattern;

import android.view.View;
import android.widget.TextView;

import com.goldenapple.lottery.R;
import com.goldenapple.lottery.view.DrawCodeView;
import com.goldenapple.lottery.view.DrawTrendView;

/**
 * Created by ACE-PC on 2016/3/11.
 */
public class CodeView{

    private static final String TAG = CodeView.class.getSimpleName();
    private DrawCodeView drawCodeView;
    public CodeView(View trendView, String title){
        ((TextView) trendView.findViewById(R.id.code_column_title)).setText(title);
        drawCodeView = trendView.findViewById(R.id.codeview);
    }

    public void setCodeData(Object trendArray){
        drawCodeView.setData(trendArray);
    }

    public void requestLayout() {
        if (drawCodeView != null && !drawCodeView.isLayoutRequested()) {
            drawCodeView.requestLayout();
        }
    }
}
