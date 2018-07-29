package careclues.careclueschat.feature.paytm;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import careclues.careclueschat.R;
import careclues.careclueschat.application.CareCluesChatApplication;
import careclues.careclueschat.feature.common.BaseFragment;

/**
 * Created by SukamalD on 7/28/2018.
 */

public class LoadWebPage extends BaseFragment{

    private View view;

//    @BindView(R.id.wvLoadPage)
    WebView wvLoadPage;
    String path;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_load_webpage, container, false);
        ButterKnife.bind(this, view);
        path = (String) getArguments().getString("path");
//        path = "file://"+ path;
        wvLoadPage = (WebView) view.findViewById(R.id.wvLoadPage);
        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            wvLoadPage.getSettings().setSafeBrowsingEnabled(false);
        }
        wvLoadPage.getSettings().setJavaScriptEnabled(true);
        wvLoadPage.getSettings().setLoadWithOverviewMode(true);
        wvLoadPage.getSettings().setUseWideViewPort(true);
        wvLoadPage.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
            }
        });

        wvLoadPage.post(new Runnable() {
            @Override
            public void run() {
                wvLoadPage.loadUrl("https://www.google.com");
            }
        });
    }

}
