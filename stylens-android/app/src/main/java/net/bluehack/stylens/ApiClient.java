package net.bluehack.stylens;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import net.bluehack.stylens.utils.UiUtil;

import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.swagger.client.ApiException;
import io.swagger.client.api.FeedApi;
import io.swagger.client.api.ObjectApi;
import io.swagger.client.api.ProductApi;
import io.swagger.client.model.GetFeedResponse;
import io.swagger.client.model.GetObjectsResponse;
import io.swagger.client.model.GetProductResponse;
import io.swagger.client.model.GetProductsResponse;

import static net.bluehack.stylens.utils.Logger.LOGD;
import static net.bluehack.stylens.utils.Logger.LOGE;
import static net.bluehack.stylens.utils.Logger.makeLogTag;

public class ApiClient {
    private static final String TAG = makeLogTag(ApiClient.class);
    private static ApiClient ourInstance = new ApiClient();
    private final String header = "application/json";
    private final String baseUrl = "http://api.stylelens.io";

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
                    output = api.getFeeds(1,10);
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

    public void productsGet(final Context context, final String productId,
                            final int offset, final int limit, final ApiResponseListener listener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                GetProductsResponse output = null;
                ProductApi api = new ProductApi();
                try {
                    output = api.getProducts(productId, offset,limit);
//                    output = api.getProducts("5a13a92a247c1a00017051c2", 0,3);
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

    public void productsByIdGet(final Context context, final String projectId, final ApiResponseListener listener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                GetProductResponse output = null;
                ProductApi api = new ProductApi();
                try {
                    output = api.getProductById(projectId);
//                    output = api.getProductById("5a13a92a247c1a00017051c2");
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

//    public void productsByImageFilePost(final Context context, final File image, final ApiResponseListener listener) {
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... params) {
//
//                GetProductsResponse output = null;
//                ProductApi api = new ProductApi();
//
//                try {
//                    output = api.getProductsByImageFile(image);
//                    LOGD(TAG, "output =>" + UiUtil.toStringGson(output));
//                    listener.onResponse(output);
//                } catch (TimeoutException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ApiException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//        }.execute();
//    }

    public void productByHostcodeAndProductNoGet(final Context context, final String hostCode, final String productNo ,final ApiResponseListener listener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                GetProductResponse output = null;
                ProductApi api = new ProductApi();
                try {
//                    output = api.getProductByHostcodeAndProductNo(hostCode, productNo);
                    output = api.getProductByHostcodeAndProductNo("HC0002", "791");
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

    public void productsByImageIdAndObjectIdGet(final Context context, final String imageId, final int objectId ,final ApiResponseListener listener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                GetProductsResponse output = null;
                ProductApi api = new ProductApi();
                try {
                    output = api.getProductsByImageIdAndObjectId(imageId, objectId);
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

//    public void objectsByImageFilePost(final Context context, final File file, final ApiResponseListener listener) {
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... params) {
//
//                GetObjectsResponse output = null;
//                ObjectApi api = new ObjectApi();
//
//                try {
//                    output = api.getObjectsByImageFile(file);
//                    LOGD(TAG, "output =>" + UiUtil.toStringGson(output));
//                    listener.onResponse(output);
//                } catch (TimeoutException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ApiException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//        }.execute();
//    }

    public void objectsByProductIdGet(final Context context, final String productId, final ApiResponseListener listener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                GetObjectsResponse output = null;
                ObjectApi api = new ObjectApi();
                try {
//                    output = api.getObjectsByProductId(productId);
                    output = api.getObjectsByProductId("5a13a92a247c1a00017051c2");
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

    public void productsByImageFilePost(final File file, final ApiClient.ApiResponseListener listener) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    final String urlResource = "/products/images";
                    final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");

                    RequestBody req = new MultipartBuilder().type(MultipartBuilder.FORM)
                            .addFormDataPart("file", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file)).build();

                    Request request = new Request.Builder()
                            .url(baseUrl+ urlResource)
                            .post(req)
                            .build();

                    OkHttpClient client = new OkHttpClient();
                    Response response = client.newCall(request).execute();

                    Gson gson = new Gson();
                    String resData = response.body().string();
                    LOGD("response", resData);
//                    GetProductsResponse getProductsResponse = gson.fromJson(resData, GetProductsResponse.class);
//                    listener.onResponse(getProductsResponse);
                    listener.onResponse(new JSONObject(resData).getJSONArray("data"));


                } catch (UnknownHostException | UnsupportedEncodingException e) {
                    LOGE(TAG, "Error: " + e.getLocalizedMessage());
                } catch (Exception e) {
                    LOGE(TAG, "Other Error: " + e.getLocalizedMessage());
                }
                return null;
            }
        }.execute();
    }

    public void objectsByImageFilePost(final File file, final ApiClient.ApiResponseListener listener) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    final String urlResource = "/objects";
                    final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");

                    RequestBody req = new MultipartBuilder().type(MultipartBuilder.FORM)
                            .addFormDataPart("file", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file)).build();

                    Request request = new Request.Builder()
                            .url(baseUrl+ urlResource)
                            .post(req)
                            .build();

                    OkHttpClient client = new OkHttpClient();
                    client.setConnectTimeout(60, TimeUnit.SECONDS); // connect timeout
                    client.setReadTimeout(60, TimeUnit.SECONDS);
                    Response response = client.newCall(request).execute();

                    Gson gson = new Gson();
                    String resData = response.body().string();
                    LOGD("response", resData);
//                    GetProductsResponse getProductsResponse = gson.fromJson(resData, GetProductsResponse.class);
//                    listener.onResponse(getProductsResponse);
                    listener.onResponse(new JSONObject(resData).getJSONObject("data"));


                } catch (UnknownHostException | UnsupportedEncodingException e) {
                    LOGE(TAG, "Error: " + e.getLocalizedMessage());
                } catch (Exception e) {
                    LOGE(TAG, "Other Error: " + e.getLocalizedMessage());
                }
                return null;
            }
        }.execute();
    }
}
