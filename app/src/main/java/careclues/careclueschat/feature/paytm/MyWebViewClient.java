package careclues.careclueschat.feature.paytm;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by SukamalD on 7/28/2018.
 */

public class MyWebViewClient extends WebViewClient {

    public void onPageFinished(WebView view, String url) {
        view.loadUrl("javascript:alert('hi')");
    }
}
