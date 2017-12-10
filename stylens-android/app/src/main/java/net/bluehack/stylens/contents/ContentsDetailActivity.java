package net.bluehack.stylens.contents;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
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
import com.soundcloud.android.crop.MonitoredActivity;
import com.squareup.picasso.Picasso;

import net.bluehack.stylens.ApiClient;
import net.bluehack.stylens.MainActivity;
import net.bluehack.stylens.home.FeedAdapter;
import net.bluehack.stylens.utils.Logger;
import net.bluehack.stylens.utils.UiUtil;
import net.bluehack.stylens_android.R;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import io.swagger.client.model.Box;
import io.swagger.client.model.GetFeedResponse;
import io.swagger.client.model.GetObjectsByProductIdResponse;
import io.swagger.client.model.GetObjectsByProductIdResponseData;
import io.swagger.client.model.GetObjectsResponse;
import io.swagger.client.model.GetObjectsResponseData;
import io.swagger.client.model.GetProductsResponse;
import io.swagger.client.model.Product;

import static net.bluehack.stylens.utils.Logger.LOGD;
import static net.bluehack.stylens.utils.Logger.LOGE;
import static net.bluehack.stylens.utils.Logger.makeLogTag;

public class ContentsDetailActivity extends MonitoredActivity implements
        AbsListView.OnScrollListener, AbsListView.OnItemClickListener{

    private static final String TAG = makeLogTag(ContentsDetailActivity.class);
    private Context context;
//    private ImageView iv_picture;
    private CropImageView iv_picture;
    private ImageView iv_back_btn;
    private ImageView iv_crop_btn;
    private FrameLayout iv_purchase;
    private GridView grid_view;
    private TextView tv_purchase_01;
    private TextView tv_purchase_02;

    private ArrayList<Product> products = null;
    private FeedAdapter feedAdapter;
    private boolean mHasRequestedMore;
    private final int offset = 1;
    private final int limit = 3;

    private Bitmap imageBitmap;
    private HighlightView cropView;
    private final Handler handler = new Handler();
    private int aspectX;
    private int aspectY;

    // Output image
    private int maxX;
    private int maxY;

    private Box box = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents_detail);
        context = this;

//        iv_picture = (ImageView) findViewById(R.id.iv_picture);
        iv_picture = (CropImageView) findViewById(R.id.iv_picture);
        iv_back_btn = (ImageView) findViewById(R.id.iv_back_btn);
        iv_crop_btn = (ImageView) findViewById(R.id.iv_crop_btn);
        iv_purchase = (FrameLayout) findViewById(R.id.iv_purchase);
        tv_purchase_01 = (TextView) findViewById(R.id.tv_purchase_01);
        tv_purchase_02 = (TextView) findViewById(R.id.tv_purchase_02);
        grid_view = (GridView) findViewById(R.id.grid_view);

        final Product product = getProductItem();


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels /2;

        Picasso.with(this)
                .load(product.getMainImage())
                .resize(width,height)
                .into(iv_picture);

        tv_purchase_01.setText(product.getName());
        tv_purchase_02.setText(product.getPrice() + context.getResources().getString(R.string.price_unit_won));

        if (feedAdapter == null) {
            feedAdapter = new FeedAdapter(this, R.id.txt_line1);
        }
        grid_view.setAdapter(feedAdapter);
        grid_view.setOnScrollListener(this);
        grid_view.setOnItemClickListener(this);

        productsAPI(context, product.getId(), offset, limit);

        iv_crop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                iv_picture.buildDrawingCache();
                imageBitmap = iv_picture.getDrawingCache();
                LOGE(TAG, "iv_picture bitmap W, H=>" + imageBitmap.getWidth() + ","
                    + imageBitmap.getHeight());

                objectsByProductIdAPI(context, product.getId());
//                startCrop();

            }
        });

        iv_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ContentsDetailActivity.this, ProductWebView.class);
                intent.putExtra("productUrl", product.getProductUrl());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private Product getProductItem() {
        Intent intent = getIntent();
        Product product = null;

        try {
            if (intent.getSerializableExtra("product") != null) {
                String productItem = intent.getExtras().getString("product");
                Gson gson = new Gson();
                product = gson.fromJson(productItem, Product.class);
                intent.removeExtra("product");
            }
        }catch (Exception e) {
            LOGE(TAG, "getProductItem func error");
        }

        return product;
    }

    private void productsAPI(final Context context, String productId, int offset, int limit) {
        ApiClient.getInstance().productsGet(context, productId, offset, limit, new ApiClient.ApiResponseListener() {
            @Override
            public void onResponse(final Object result) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Gson gson = new Gson();
                                GetProductsResponse output = null;
                                output = (GetProductsResponse) result;
//                                LOGE(TAG, "output=>" + output.toString());

                                products = (ArrayList<Product>) output.getData();
                                if (products != null || products.size() > 0) {
                                    for (Product product : products) {
//                                        feedAdapter.addItem(product);
                                        feedAdapter.add(product);
//                                    LOGD(TAG, "product.getMainImageMobileThumb()=> " + product.getMainImageMobileThumb());
                                    }

                                    feedAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                }).start();
            }
        });
    }

    private void objectsByProductIdAPI(final Context context, String productId) {
        ApiClient.getInstance().objectsByProductIdGet(context, productId, new ApiClient.ApiResponseListener() {
            @Override
            public void onResponse(final Object result) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Gson gson = new Gson();
                                GetObjectsResponse output = null;
                                output = (GetObjectsResponse) result;
//                                LOGE(TAG, "output=>" + output.toString());
                                GetObjectsResponseData responseData = output.getData();
                                box = responseData.getBoxes().get(0).getBox();
                                markBoxsCrop(box);
                            }
                        });
                    }
                }).start();
            }
        });
    }

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

//    private void startCrop() {
//        if (isFinishing()) {
//            return;
//        }
//        iv_picture.setImageBitmapResetBase(imageBitmap, true);
//        CropUtil.startBackgroundJob(this, null, getResources().getString(R.string.crop__wait),
//                new Runnable() {
//                    public void run() {
//                        final CountDownLatch latch = new CountDownLatch(1);
//                        handler.post(new Runnable() {
//                            public void run() {
//                                latch.countDown();
//                            }
//                        });
//                        try {
//                            latch.await();
//                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }
//                        new Cropper().crop();
//                    }
//                }, handler
//        );
//    }
//
//    private class Cropper {
//
//        private void makeDefault() {
//            if (imageBitmap == null) {
//                return;
//            }
//
//            HighlightView hv = new HighlightView(iv_picture);
//            final int width = imageBitmap.getWidth();
//            final int height = imageBitmap.getHeight();
//
//            Rect imageRect = new Rect(0, 0, width, height);
//
//            // Make the default size about 4/5 of the width or height
//            int cropWidth = Math.min(width, height) * 4 / 5;
//            @SuppressWarnings("SuspiciousNameCombination")
//            int cropHeight = cropWidth;
//
//            if (aspectX != 0 && aspectY != 0) {
//                if (aspectX > aspectY) {
//                    cropHeight = cropWidth * aspectY / aspectX;
//                } else {
//                    cropWidth = cropHeight * aspectX / aspectY;
//                }
//            }
//
//            int x = (width - cropWidth) / 2;
//            int y = (height - cropHeight) / 2;
//
//            RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
//            hv.setup(iv_picture.getUnrotatedMatrix(), imageRect, cropRect, aspectX != 0 && aspectY != 0);
//            iv_picture.add(hv);
//        }
//
//        public void crop() {
//            handler.post(new Runnable() {
//                public void run() {
//                    makeDefault();
//                    iv_picture.invalidate();
//                    cropView = iv_picture.highlightViews.get(0);
//                    cropView.setFocus(true);
////                    if (imageView.highlightViews.size() == 1) {
////                        cropView = imageView.highlightViews.get(0);
////                        cropView.setFocus(true);
////                    }
//                }
//            });
//        }
//    }
}
