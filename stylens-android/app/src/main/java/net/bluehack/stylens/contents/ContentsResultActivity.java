package net.bluehack.stylens.contents;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import net.bluehack.stylens.ApiClient;
import net.bluehack.stylens.home.FeedAdapter;
import net.bluehack.stylens_android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import io.swagger.client.model.BoxArray;
import io.swagger.client.model.GetObjectsResponse;
import io.swagger.client.model.GetObjectsResponseData;
import io.swagger.client.model.GetProductsResponse;
import io.swagger.client.model.Product;

import static android.graphics.Bitmap.Config.ARGB_8888;
import static net.bluehack.stylens.utils.Logger.LOGD;
import static net.bluehack.stylens.utils.Logger.LOGE;
import static net.bluehack.stylens.utils.Logger.makeLogTag;

public class ContentsResultActivity extends AppCompatActivity implements
        AbsListView.OnScrollListener, AbsListView.OnItemClickListener{

    private static final String TAG = makeLogTag(ContentsResultActivity.class);
    private Context context;
    private ImageView iv_picture;
    private ImageView iv_back_btn;
    private GridView grid_view;

    private ArrayList<Product> products = null;
//    private ArrayList<BoxArray> boxs = null;
    private BoxArray box = null;
    private FeedAdapter feedAdapter;
    private boolean mHasRequestedMore;
    private final int offset = 1;
    private final int limit = 3;
    private Bitmap imageBitmap;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents_result);
        context = this;
        intent = getIntent();

        iv_picture = (ImageView) findViewById(R.id.iv_picture);
        iv_back_btn = (ImageView) findViewById(R.id.iv_back_btn);
        grid_view = (GridView) findViewById(R.id.grid_view);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels / 2;

        File imageFile = getPickImage();

        imageBitmap = (Bitmap)intent.getExtras().get("imageBitmap");

        if(imageFile.exists()) {
            Uri uri = Uri.fromFile(imageFile);

//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(imageFile.getPath(), options);
//            int bwidth = options.outWidth;
//            int bheight = options.outHeight;
//            String type = options.outMimeType;

            Picasso.with(this)
                    .load(uri)
                    .resize(width,height)
                    .into(iv_picture);
//            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
//            iv_picture.setImageBitmap(bitmap);
        }

        if (feedAdapter == null) {
            feedAdapter = new FeedAdapter(this, R.id.txt_line1);
        }
        grid_view.setAdapter(feedAdapter);
        grid_view.setOnScrollListener(this);
        grid_view.setOnItemClickListener(this);

        objectsByImageFileAPI(context, imageFile);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private File getPickImage() {

        String imageItem = null;
        File imageFile = null;

        try {
            if (intent.getExtras().getString("imageFile") != null) {
                imageItem = intent.getExtras().getString("imageFile");
                intent.removeExtra("imageFile");
            }

            imageFile = new File(imageItem);

        }catch (Exception e) {
            LOGE(TAG, "getPickImage func error");
        }

        return imageFile;
    }


    private void objectsByImageFileAPI(final Context context, final File imageFile) {

        //TODO: TEST code
        String root = Environment.getExternalStorageDirectory().toString() + "/Pictures";
        File testImage = new File(root, "5a13a888247c1a0001704f19.jpg");

        if (imageFile.isFile()) {
            LOGD("image test", "ok : filePath => " + imageFile.getAbsolutePath());
        } else {
            LOGD("image test", "fail");
        }

        ApiClient.getInstance().objectsByImageFilePost(imageFile, new ApiClient.ApiResponseListener() {
            @Override
            public void onResponse(final Object result) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Gson gson = new Gson();
                                JSONObject output = null;
                                output = (JSONObject) result;
                                LOGE(TAG, "output=>" + output);

                                GetObjectsResponseData getObjectsResponseData = gson.fromJson(output.toString(),
                                        GetObjectsResponseData.class);

                                //TODO: check score & appear score priority
//                                float score = 0.0f;
//                                for (int i = 0; i < getObjectsResponse.getData().getBoxes().size(); i ++) {
//                                    if (getObjectsResponse.getData().getBoxes().get(i).getScore())
//                                }

                                //TODO: construct all box
//                                box = getObjectsResponseData.getBoxes().get(0).getBox();

                                products = (ArrayList<Product>) getObjectsResponseData.getBoxes().get(0).getProducts();
                                if (products != null || products.size() > 0) {
                                    for (Product product : products) {
                                        feedAdapter.add(product);
                                    }
                                }
                                feedAdapter.notifyDataSetChanged();

                            }
                        });
                    }
                }).start();
            }
        });
    }

//    private void productsByImageFileAPI(final Context context, final File imageFile) {
//
//        //TODO: TEST code
//        String root = Environment.getExternalStorageDirectory().toString() + "/Pictures";
//        File testImage = new File(root, "5a13a888247c1a0001704f19.jpg");
//
//        if (imageFile.isFile()) {
//            LOGD("image test", "ok : filePath => " + imageFile.getAbsolutePath());
//        } else {
//            LOGD("image test", "fail");
//        }
//
//        ApiClient.getInstance().objectsByImageFilePost(imageFile, new ApiClient.ApiResponseListener() {
//            @Override
//            public void onResponse(final Object result) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Gson gson = new Gson();
//                                JSONArray output = null;
//                                output = (JSONArray) result;
//                                LOGE(TAG, "output=>" + output);
//
//                                for (int i = 0; i < output.length(); i ++) {
//                                    try {
//                                        JSONObject jsonObject = output.getJSONObject(i);
//                                        Product product = gson.fromJson(jsonObject.toString(), Product.class);
////                                        products.add(product);
//                                        feedAdapter.add(product);
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                                feedAdapter.notifyDataSetChanged();
//                            }
//                        });
//                    }
//                }).start();
//            }
//        });
//    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        LOGD(TAG, "onScrollStateChanged:" + scrollState);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        LOGD(TAG, "onScroll firstVisibleItem:" + firstVisibleItem +
                " visibleItemCount:" + visibleItemCount +
                " totalItemCount:" + totalItemCount);
        // our handling
        if (!mHasRequestedMore) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen >= totalItemCount) {
                LOGD(TAG, "onScroll lastInScreen - so load more");
                mHasRequestedMore = true;
//                onLoadMoreItems();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "Item Clicked: " + position, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
