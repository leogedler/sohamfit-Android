package com.sohamfit.sohamfitapp;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by leonardogedler on 4/21/17.
 */

// Services class
public class HttpUtils {
    private static AsyncHttpClient client = new AsyncHttpClient();
    private static final String BASE_URL = Constants.SERVER_URL;

    public static void getVideos(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("X-Parse-Application-Id", Constants.APP_ID);
        client.addHeader("Content-Type", "application/json");
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
