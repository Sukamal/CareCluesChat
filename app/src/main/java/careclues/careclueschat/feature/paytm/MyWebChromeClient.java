package careclues.careclueschat.feature.paytm;

import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by SukamalD on 7/28/2018.
 */

public class MyWebChromeClient extends WebChromeClient {
    @Override
    public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result)
    {
        Log.d("alert", message);
        result.confirm();
        return true;
    };
}
