package careclues.careclueschat.feature.paytm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

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

    @BindView(R.id.wvLoadPage)
    WebView wvLoadPage;
    String path;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_load_webpage, container, false);
        ButterKnife.bind(this, view);
        path = "file:///"+ path;
        path = (String) getArguments().getString("path");
        initView();
        return view;
    }

    private void initView(){
        wvLoadPage.loadUrl(path);
    }

}
