package net.bluehack.stylens.home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.etsy.android.grid.StaggeredGridView;
import com.google.gson.Gson;

import net.bluehack.stylens.ApiClient;
import net.bluehack.stylens_android.R;

import java.util.List;

import io.swagger.client.model.GetFeedResponse;
import io.swagger.client.model.Product;

import static net.bluehack.stylens.utils.Logger.LOGD;
import static net.bluehack.stylens.utils.Logger.makeLogTag;

public class FeedFragment extends Fragment {

    private static final String TAG = makeLogTag(FeedFragment.class);
    private Context context;
    private int mPageNumber;
    private FeedAdapter feedAdapter;
    private GridView gv_list;
//    private StaggeredGridView grid_view;
    private List<Product> products = null;

    public static FeedFragment create(int pageNumber) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putInt("page", pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();



    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mPageNumber = getArguments().getInt("page");
        context = getContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_feed, container, false);
//        ((TextView) rootView.findViewById(R.id.number)).setText(mPageNumber + "");
        gv_list = (GridView) rootView.findViewById(R.id.gv_list);
//        grid_view = (StaggeredGridView) rootView.findViewById(R.id.grid_view);
        feedAdapter = new FeedAdapter(context);
        gv_list.setAdapter(feedAdapter);
//        grid_view.setAdapter(feedAdapter);
        feedGetAPI(context);

        return rootView;
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

                                products = output.getData();
                                if (products != null || products.size() > 0) {
                                    for (Product product : products) {
                                        feedAdapter.add(product.getMainImageMobileThumb());
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

}
