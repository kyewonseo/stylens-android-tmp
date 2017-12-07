package net.bluehack.stylens.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.*;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import net.bluehack.stylens.ApiClient;
import net.bluehack.stylens.contents.ContentsDetailActivity;
import net.bluehack.stylens.utils.UiUtil;
import net.bluehack.stylens_android.R;

import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import io.swagger.client.model.GetFeedResponse;
import io.swagger.client.model.Product;

import static net.bluehack.stylens.utils.Logger.LOGD;
import static net.bluehack.stylens.utils.Logger.makeLogTag;

public class StaggeredGridFragment extends Fragment implements
        AbsListView.OnScrollListener, AbsListView.OnItemClickListener {

    private static final String TAG = makeLogTag(StaggeredGridFragment.class);
    private StaggeredGridView staggeredGridView;
    private boolean mHasRequestedMore;
    private FeedAdapter feedAdapter;

    private ArrayList<String> mData;
    private static ArrayList<Product> products = null;
    private static Boolean isMoreItem = false;

    public StaggeredGridFragment() {}

    public static StaggeredGridFragment create(int pageNumber) {
        StaggeredGridFragment fragment = new StaggeredGridFragment();
        Bundle args = new Bundle();
        args.putInt("page", pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sgv_feed, container, false);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        staggeredGridView = (StaggeredGridView) getView().findViewById(R.id.grid_view);

        if (savedInstanceState == null) {
            final LayoutInflater layoutInflater = getActivity().getLayoutInflater();

//                View header = layoutInflater.inflate(R.layout.list_item_header_footer, null);
//                View footer = layoutInflater.inflate(R.layout.list_item_header_footer, null);
//                TextView txtHeaderTitle = (TextView) header.findViewById(R.id.txt_title);
//                TextView txtFooterTitle = (TextView) footer.findViewById(R.id.txt_title);
//                txtHeaderTitle.setText("THE HEADER!");
//                txtFooterTitle.setText("THE FOOTER!");

//                staggeredGridView.addHeaderView(header);
//                staggeredGridView.addFooterView(footer);
        }

        if (feedAdapter == null) {
            feedAdapter = new FeedAdapter(getActivity(), R.id.txt_line1);
        }

        if (products == null) {
            products = new ArrayList<>();
            feedGetAPI(getContext());
        } else {
            products.clear();
            feedGetAPI(getContext());
        }

        staggeredGridView.setAdapter(feedAdapter);
        staggeredGridView.setOnScrollListener(this);
        staggeredGridView.setOnItemClickListener(this);


    }

    @SuppressLint("LongLogTag")
    @Override
    public void onScrollStateChanged(final AbsListView view, final int scrollState) {
        Log.d(TAG, "onScrollStateChanged:" + scrollState);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
        Log.d(TAG, "onScroll firstVisibleItem:" + firstVisibleItem +
                " visibleItemCount:" + visibleItemCount +
                " totalItemCount:" + totalItemCount);
        // our handling
        if (!mHasRequestedMore) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen >= totalItemCount) {
                Log.d(TAG, "onScroll lastInScreen - so load more");
                mHasRequestedMore = true;
//                onLoadMoreItems();
            }
        }
    }

    public void onLoadMoreItems(ArrayList<Product> moreProducts, int index) {
        if (moreProducts.size() > 0) {
//            products.addAll(index, moreProducts);
            for (Product moreProduct: moreProducts) {
                products.add(index, moreProduct);
            }
            isMoreItem = true;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        if (isMoreItem) {
            feedAdapter.notifyDataSetChanged();
            isMoreItem = false;
        }

        Toast.makeText(getActivity(), "Item Clicked: " + position, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(), "Item Clicked: " + adapterView.getSelectedItemPosition(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this.getActivity(), ContentsDetailActivity.class);
        intent.putExtra("product", UiUtil.toStringGson(products.get(position)));
        startActivity(intent);
    }

    private void feedGetAPI(final Context context) {
        ApiClient.getInstance().feedGet(context, new ApiClient.ApiResponseListener() {
            @Override
            public void onResponse(final Object result) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Gson gson = new Gson();
                                GetFeedResponse output = null;
                                output = (GetFeedResponse) result;
//                                LOGE(TAG, "output=>" + output.toString());

                                products.addAll(output.getData());
                                if (products != null || products.size() > 0) {
                                    for (Product product : products) {
//                                        feedAdapter.addItem(product);
                                        feedAdapter.add(product);
//                                    LOGD(TAG, "product.getMainImageMobileThumb()=> " + product.getMainImageMobileThumb());
                                    }

                                    feedAdapter.notifyDataSetChanged();
                                }

//                                LOGD(TAG, "products.get(0) =>" + products.get(0).toString());
                            }
                        });
                    }
                }).start();
            }
        });
    }

//    private void test1API(final Context context) {
//        ApiClient.getInstance().productsGet(context,"", new ApiClient.ApiResponseListener() {
//            @Override
//            public void onResponse(final Object result) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                            }
//                        });
//                    }
//                }).start();
//            }
//        });
//    }

    private void test2API(final Context context) {
        ApiClient.getInstance().productsByIdGet(context,"", new ApiClient.ApiResponseListener() {
            @Override
            public void onResponse(final Object result) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }
                }).start();
            }
        });
    }

    private void test3API(final Context context) {
        ApiClient.getInstance().productByHostcodeAndProductNoGet(context,"","", new ApiClient.ApiResponseListener() {
            @Override
            public void onResponse(final Object result) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

//                                LOGD(TAG, "products.get(0) =>" + products.get(0).toString());
                            }
                        });
                    }
                }).start();
            }
        });
    }

    private void test4API(final Context context) {
        ApiClient.getInstance().objectsByProductIdGet(context, "", new ApiClient.ApiResponseListener() {
            @Override
            public void onResponse(final Object result) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Gson gson = new Gson();
//                                GetFeedResponse output = null;
//                                output = (GetFeedResponse) result;
//                                LOGE(TAG, "output=>" + output.toString());

//                                LOGD(TAG, "products.get(0) =>" + products.get(0).toString());
                            }
                        });
                    }
                }).start();
            }
        });
    }

    private void test5API(final Context context) {
        String root = Environment.getExternalStorageDirectory().toString() + "/Pictures";
        File myDir = new File(root);
        myDir.mkdirs();
        File image = new File(root, "5a13a888247c1a0001704f19.jpg");

        if (image.isFile()) {
            LOGD("image test", "ok");
        } else {
            LOGD("image test", "fail");
        }

        ApiClient.getInstance().productsByImageFilePost(image, new ApiClient.ApiResponseListener() {
            @Override
            public void onResponse(final Object result) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                    }
                }).start();
            }
        });
    }

    private void test6API(final Context context) {

        String root = Environment.getExternalStorageDirectory().toString() + "/Pictures";
        File myDir = new File(root);
        myDir.mkdirs();
        File image = new File(root, "5a13a888247c1a0001704f19.jpg");

        if (image.isFile()) {
            LOGD("image test", "ok");
        } else {
            LOGD("image test", "fail");
        }

        ApiClient.getInstance().objectsByImageFilePost(image, new ApiClient.ApiResponseListener() {
            @Override
            public void onResponse(final Object result) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                    }
                }).start();
            }
        });
    }



}
