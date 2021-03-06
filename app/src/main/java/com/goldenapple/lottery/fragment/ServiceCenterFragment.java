package com.goldenapple.lottery.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.goldenapple.lottery.R;
import com.goldenapple.lottery.app.BaseFragment;
import com.goldenapple.lottery.app.GoldenAppleApp;
import com.goldenapple.lottery.base.net.GsonHelper;
import com.goldenapple.lottery.base.net.RestCallback;
import com.goldenapple.lottery.base.net.RestRequest;
import com.goldenapple.lottery.base.net.RestRequestManager;
import com.goldenapple.lottery.base.net.RestResponse;
import com.goldenapple.lottery.data.Customers;
import com.goldenapple.lottery.data.CustomersCommand;
import com.goldenapple.lottery.data.ServiceParam;
import com.goldenapple.lottery.data.UserInfo;
import com.goldenapple.lottery.material.ImageFilePath;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * 客服页面
 * Created by Ace on 2016/10/6.
 */
public class ServiceCenterFragment extends BaseFragment {

    private static final String TAG = ServiceCenterFragment.class.getSimpleName();

    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.net_prompt)
    TextView netPrompt;

    private UserInfo userInfo;

    public static final int INPUT_FILE_REQUEST_CODE = 1;
    private static int FILECHOOSER_RESULTCODE = 2;
    private static final int CUSTOMERS_COMMAND = 1;
    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflateView(inflater, container, "客服", R.layout.fragment_servicecenter, true, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userInfo = GoldenAppleApp.getUserCentre().getUserInfo();
        if (GoldenAppleApp.getNetStateHelper().isConnected()) {
            webView.setVisibility(View.VISIBLE);
            netPrompt.setVisibility(View.GONE);
            initView();
//            loadCustomers();
        } else {
            webView.setVisibility(View.GONE);
            netPrompt.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onPause() {
        if (webView != null) {
            webView.onPause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
        }
    }

    @SuppressLint("JavascriptInterface")
    private void initView() {

        WebSettings mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setSupportZoom(false);
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setAllowContentAccess(true);
        ServiceParam serviceParam = new ServiceParam();
        serviceParam.setClientName(userInfo.getUsername().isEmpty() ? "临时访客" : userInfo.getUsername());
        serviceParam.setUserid(String.valueOf(userInfo.getId()));
        serviceParam.setBalance(String.format("%.4f", userInfo.getAbalance()));
        serviceParam.setRegTime(String.valueOf(userInfo.getSigninAt()));
        String userjson = Base64.encodeToString(GsonHelper.toJson(serviceParam).getBytes(), Base64.DEFAULT);
        //53客服
        webView.loadUrl("http://s3.myapple888.com/index/app?companyId=70722524&style=default&mode=4&token=" + userjson);

//        webView.loadUrl("http://yafuweb.4385nt.com/mobile.php?userid="+userInfo.getId()+"&name="+userInfo.getName()+"&platform=2");
        webView.setWebChromeClient(mWebChromeClient);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    //在sdcard卡创建缩略图
    @SuppressLint("SdCardPath")
    private File createImageFile() {
        //mCameraPhotoPath="/mnt/sdcard/tmp.png";
        File file = new File(Environment.getExternalStorageDirectory() + "/", "tmp.png");
        mCameraPhotoPath = file.getAbsolutePath();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        // android 5.0 这里需要使用android5.0 sdk
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
            }
            mFilePathCallback = filePathCallback;
            imageFileChooser();
            return true;
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            uploadMessage = uploadMsg;
            imageFileChooser();
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            uploadMessage = uploadMsg;
            imageFileChooser();
        }

        //For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            uploadMessage = uploadMsg;
            imageFileChooser();
        }
    };

    /**
     * TODO：重写选择图片方式与相机方式
     * 选择图片方式
     */
    private void imageFileChooser() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                //设置MediaStore.EXTRA_OUTPUT路径,相机拍照写入的全路径
                photoFile = createImageFile();
                takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
            } catch (Exception ex) {
                Log.e(TAG, "无法创建图像文件", ex);
            }
            if (photoFile != null) {
                mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                System.out.println(mCameraPhotoPath);
            } else {
                takePictureIntent = null;
            }
        }

        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");
        Intent[] intentArray;
        if (takePictureIntent != null) {
            intentArray = new Intent[]{takePictureIntent};
            System.out.println(takePictureIntent);
        } else {
            intentArray = new Intent[0];
        }
        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "图片选择");
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
        startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == uploadMessage) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (result != null) {
                String imagePath = ImageFilePath.getPath(getActivity(), result);
                if (!TextUtils.isEmpty(imagePath)) {
                    result = Uri.parse("file:///" + imagePath);
                }
            }
            uploadMessage.onReceiveValue(result);
            uploadMessage = null;
        } else if (requestCode == INPUT_FILE_REQUEST_CODE && mFilePathCallback != null) {
            // 5.0的回调
            Uri[] results = null;
            // 检查响应是否良好
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    // 如果没有数据，那么我们可能已经拍了照片
                    if (mCameraPhotoPath != null) {
                        Log.d("Camera_Photo_Path", mCameraPhotoPath);
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    Log.d("Camera_DataString", dataString);
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }
            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }

    private void loadCustomers() {
        CustomersCommand command = new CustomersCommand();
        TypeToken typeToken = new TypeToken<RestResponse<ArrayList<Customers>>>() {
        };
        RestRequest restRequest = RestRequestManager.createRequest(getActivity(), command, typeToken, restCallback, CUSTOMERS_COMMAND, this);
        restRequest.execute();
    }

    private RestCallback restCallback = new RestCallback() {
        @Override
        public boolean onRestComplete(RestRequest request, RestResponse response) {
            List<Customers> data = (ArrayList<Customers>) response.getData();
            if (data.size() > 0) {
                webView.loadUrl(data.get(0).getUrl());
            }
            return true;
        }

        @Override
        public boolean onRestError(RestRequest request, int errCode, String errDesc) {
            if (errCode == 3004 || errCode == 2016) {
                signOutDialog(getActivity(), errCode);
                return true;
            }
            return false;
        }

        @Override
        public void onRestStateChanged(RestRequest request, @RestRequest.RestState int state) {
            if (request.getId() == CUSTOMERS_COMMAND) {
                if (state == RestRequest.RUNNING) {
                    dialogShow("加载中...");
                } else {
                    dialogHide();
                }
            }
        }
    };
}
