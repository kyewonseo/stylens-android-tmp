package net.bluehack.stylens.home;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.util.DynamicHeightTextView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import net.bluehack.stylens.ApiClient;
import net.bluehack.stylens.utils.UiUtil;
import net.bluehack.stylens_android.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import io.swagger.client.model.GetProductsResponse;
import io.swagger.client.model.Product;

public class FeedAdapter extends ArrayAdapter<Product> {

    private static final String TAG = "FeedAdapter";
    private Context context;

    static class ViewHolder {
        TextView txtLineOne;
        TextView txt_line2;
        ImageView iv_more_item;
        ImageView iv_item;

    }

    private final LayoutInflater mLayoutInflater;
    private final Random mRandom;

    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    public FeedAdapter(final Context context, final int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        mRandom = new Random();
    }

    @Override
    public void insert(@Nullable Product object, int index) {
        super.insert(object, index);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder vh;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.card_item, parent, false);
            vh = new ViewHolder();
            vh.txtLineOne = (TextView) convertView.findViewById(R.id.txt_line1);
            vh.txt_line2 = (TextView) convertView.findViewById(R.id.txt_line2);
            vh.iv_more_item = (ImageView) convertView.findViewById(R.id.iv_more_item);
            vh.iv_item = (ImageView) convertView.findViewById(R.id.iv_item);

            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }

        double positionHeight = getPositionRatio(position);

        Picasso.with(context).load(getItem(position).getMainImageMobileThumb()).into(vh.iv_item);

        Log.d(TAG, "getView position:" + position  + " h:" + positionHeight);

//        vh.txtLineOne.setHeightRatio(positionHeight);
        vh.txtLineOne.setText(getItem(position).getName().substring(0,7));
        vh.txt_line2.setText(String.valueOf(getItem(position).getPrice()));

        vh.iv_more_item.setBackground(UiUtil.getDrawable(context, R.drawable.btn_same_product_nor));
        vh.iv_more_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Toast.makeText(getContext(), "Button Clicked Position " +
                        position, Toast.LENGTH_SHORT).show();

                int offset = 1;
                int limit = 10;
                productsAPI(context, getItem(position).getId(), offset, limit, position);
            }
        });

        return convertView;
    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
            Log.d(TAG, "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5 the width

        //TODO: image height로 배치하기

    }

    private void productsAPI(final Context context, String productId, int offset, int limit, final int position) {
        ApiClient.getInstance().productsGet(context, productId, offset, limit, new ApiClient.ApiResponseListener() {
            @Override
            public void onResponse(final Object result) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                GetProductsResponse output = null;
                                output = (GetProductsResponse) result;

                                ArrayList<Product> products = (ArrayList<Product>) output.getData();

                                StaggeredGridFragment staggeredGridFragment= new StaggeredGridFragment();
                                staggeredGridFragment.onLoadMoreItems(products, position+1);
                                if (products != null || products.size() > 0) {
                                    for (Product product : products) {
                                        insert(product, position+1);
                                    }
                                }
                            }
                        });
                    }
                }).start();
            }
        });
    }

}
