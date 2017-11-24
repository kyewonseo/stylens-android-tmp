package net.bluehack.stylens;

import android.content.Context;
import android.os.AsyncTask;

import net.bluehack.stylens.utils.UiUtil;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import io.swagger.client.ApiException;
import io.swagger.client.api.FeedApi;
import io.swagger.client.model.GetFeedResponse;

import static net.bluehack.stylens.utils.Logger.LOGD;
import static net.bluehack.stylens.utils.Logger.makeLogTag;

public class ApiClient {
    private static final String TAG = makeLogTag(ApiClient.class);
    private static ApiClient ourInstance = new ApiClient();
    private final String header = "application/json";

    public static ApiClient getInstance() {
        return ourInstance;
    }

    private ApiClient() {
    }

    public interface ApiResponseListener {
        void onResponse(Object result);
    }

    public void feedGet(final Context context, final ApiResponseListener listener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                GetFeedResponse output = null;
                FeedApi api = new FeedApi();
                try {
                    output = api.getFeeds(0,10);
                    LOGD(TAG, "output =>" + UiUtil.toStringGson(output));
                    listener.onResponse(output);
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ApiException e) {
                    e.printStackTrace();
                }
                return null;
            }

        }.execute();
    }
}
