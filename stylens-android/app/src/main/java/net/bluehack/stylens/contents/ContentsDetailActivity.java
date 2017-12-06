package net.bluehack.stylens.contents;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import net.bluehack.stylens.MainActivity;
import net.bluehack.stylens.home.FeedAdapter;
import net.bluehack.stylens.utils.Logger;
import net.bluehack.stylens.utils.UiUtil;
import net.bluehack.stylens_android.R;

import java.util.ArrayList;

import io.swagger.client.model.GetFeedResponse;
import io.swagger.client.model.GetProductsResponse;
import io.swagger.client.model.Product;

import static net.bluehack.stylens.utils.Logger.LOGD;
import static net.bluehack.stylens.utils.Logger.LOGE;
import static net.bluehack.stylens.utils.Logger.makeLogTag;

public class ContentsDetailActivity extends AppCompatActivity implements
        AbsListView.OnScrollListener, AbsListView.OnItemClickListener{

    private static final String TAG = makeLogTag(ContentsDetailActivity.class);
    private Context context;
    private ImageView iv_picture;
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



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents_detail);
        context = this;

        iv_picture = (ImageView) findViewById(R.id.iv_picture);
        iv_back_btn = (ImageView) findViewById(R.id.iv_back_btn);
        iv_crop_btn = (ImageView) findViewById(R.id.iv_crop_btn);
        iv_purchase = (FrameLayout) findViewById(R.id.iv_purchase);
        tv_purchase_01 = (TextView) findViewById(R.id.tv_purchase_01);
        tv_purchase_02 = (TextView) findViewById(R.id.tv_purchase_02);
        grid_view = (GridView) findViewById(R.id.grid_view);

        Product product = getProductItem();


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;

        Picasso.with(this)
                .load(product.getMainImage())
                .resize(width,height/2)
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

}
