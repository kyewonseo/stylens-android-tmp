package net.bluehack.stylens.contents;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
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
import com.soundcloud.android.crop.CropImageView;
import com.soundcloud.android.crop.CropUtil;
import com.soundcloud.android.crop.HighlightView;
import com.soundcloud.android.crop.ImageViewTouchBase;
import com.soundcloud.android.crop.MonitoredActivity;
import com.soundcloud.android.crop.RotateBitmap;
import com.squareup.picasso.Picasso;

import net.bluehack.stylens.ApiClient;
import net.bluehack.stylens.home.FeedAdapter;
import net.bluehack.stylens_android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import io.swagger.client.model.Box;
import io.swagger.client.model.BoxesArray;
import io.swagger.client.model.GetObjectsResponse;
import io.swagger.client.model.GetObjectsResponseData;
import io.swagger.client.model.GetProductsResponse;
import io.swagger.client.model.Product;

import static android.graphics.Bitmap.Config.ARGB_8888;
import static net.bluehack.stylens.utils.Logger.LOGD;
import static net.bluehack.stylens.utils.Logger.LOGE;
import static net.bluehack.stylens.utils.Logger.makeLogTag;

public class ContentsResultActivity extends MonitoredActivity implements
        AbsListView.OnScrollListener, AbsListView.OnItemClickListener{

    private static final String TAG = makeLogTag(ContentsResultActivity.class);
    private Context context;
//    private ImageView iv_picture;
    private CropImageView iv_picture;
    private ImageView iv_back_btn;
    private GridView grid_view;

    private ArrayList<Product> products = null;
//    private ArrayList<BoxArray> boxs = null;
    private Box box = null;
    private FeedAdapter feedAdapter;
    private boolean mHasRequestedMore;
    private final int offset = 1;
    private final int limit = 3;
    private Bitmap imageBitmap;
    private Intent intent;

    private HighlightView cropView;
    private final Handler handler = new Handler();
    private int aspectX;
    private int aspectY;

    // Output image
    private int maxX;
    private int maxY;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents_result);
        context = this;
        intent = getIntent();

//        iv_picture = (ImageView) findViewById(R.id.iv_picture);
        iv_picture = (CropImageView) findViewById(R.id.iv_picture);
        iv_picture.context = this;
        iv_picture.setRecycler(new ImageViewTouchBase.Recycler() {
            @Override
            public void recycle(Bitmap b) {
                b.recycle();
                System.gc();
            }
        });

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
            try {

                Bitmap mutableBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                LOGE(TAG, "imageBitmap W, H =>" + mutableBitmap.getWidth() + mutableBitmap.getHeight());
//                imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                imageBitmap = Bitmap.createScaledBitmap(mutableBitmap, width, height, true);
//                imageBitmap = mutableBitmap.copy(Bitmap.Config.ARGB_8888, true);
//                startCrop();

            } catch (IOException e) {
                e.printStackTrace();
            }

//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(imageFile.getPath(), options);
//            int bwidth = options.outWidth;
//            int bheight = options.outHeight;
//            String type = options.outMimeType;

//            Picasso.with(this)
//                    .load(uri)
//                    .resize(width,height)
//                    .into(iv_picture);

//            startCrop();

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
                                box = getObjectsResponseData.getBoxes().get(0).getBox();
                                markBoxsCrop(box);

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

    private void markBoxsCrop(final Box box) {
        if (isFinishing()) {
            return;
        }
        iv_picture.setImageBitmapResetBase(imageBitmap, true);
        CropUtil.startBackgroundJob(this, null, getResources().getString(R.string.crop__wait),
                new Runnable() {
                    public void run() {
                        final CountDownLatch latch = new CountDownLatch(1);
                        handler.post(new Runnable() {
                            public void run() {
//                                iv_picture.center();
//                                if (iv_picture.getScale() == 1F) {
//                                    iv_picture.center();
//                                }
                                latch.countDown();
                            }
                        });
                        try {
                            latch.await();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        new Cropper().crop(box);
                    }
                }, handler
        );
    }

    private class Cropper {

        private void makeDefault(Box box) {
            if (imageBitmap == null) {
                return;
            }

            HighlightView hv = new HighlightView(iv_picture);
            final int width = imageBitmap.getWidth();
            final int height = imageBitmap.getHeight();

            Rect imageRect = new Rect(0, 0, width, height);

            // Make the default size about 4/5 of the width or height
            int cropWidth = Math.min(width, height) * 4 / 5;
            @SuppressWarnings("SuspiciousNameCombination")
            int cropHeight = cropWidth;

            if (aspectX != 0 && aspectY != 0) {
                if (aspectX > aspectY) {
                    cropHeight = cropWidth * aspectY / aspectX;
                } else {
                    cropWidth = cropHeight * aspectX / aspectY;
                }
            }

            int x = (width - cropWidth) / 2;
            int y = (height - cropHeight) / 2;

//            RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);

            RectF boxRect = new RectF(box.getLeft(), box.getTop(), box.getRight(), box.getBottom());
            float boxWidthRatio = 300.f / imageRect.width();
            float boxHeightRatio = 300.f / imageRect.height();

            RectF cropRect = new RectF(box.getLeft() / boxWidthRatio, box.getTop() / boxHeightRatio,
                    box.getRight() / boxWidthRatio, box.getBottom() / boxHeightRatio);

            LOGE(TAG, "display L,T,R,B=>" + iv_picture.getLeft() + ","
                    + iv_picture.getTop() + ","
                    + iv_picture.getRight() + ","
                    + iv_picture.getBottom()
            );

            LOGE(TAG, "imageBitmap W, H=>" + imageBitmap.getWidth() + ","
                    + imageBitmap.getHeight()
            );

            LOGE(TAG, "imageRect W, H=>" + imageRect.width() + ","
                    + imageRect.height()
            );

            LOGE(TAG, "cropRect W, H=>" + cropRect.width() + ","
                    + cropRect.height()
            );

            LOGE(TAG, "cropRect L,T,R,B=>" + box.getLeft() / boxWidthRatio + ","
                    + box.getTop() / boxHeightRatio + ","
                    + box.getRight() / boxWidthRatio + ","
                    + box.getBottom() / boxHeightRatio
            );

            hv.setup(iv_picture.getUnrotatedMatrix(), imageRect, cropRect, aspectX != 0 && aspectY != 0);
            iv_picture.add(hv);
        }

        public void crop(final Box box) {
            handler.post(new Runnable() {
                public void run() {
                    makeDefault(box);
//                    makeDefault();
                    iv_picture.invalidate();
                    cropView = iv_picture.highlightViews.get(0);
                    cropView.setFocus(true);
//                    if (imageView.highlightViews.size() == 1) {
//                        cropView = imageView.highlightViews.get(0);
//                        cropView.setFocus(true);
//                    }
                }
            });
        }
    }
}
