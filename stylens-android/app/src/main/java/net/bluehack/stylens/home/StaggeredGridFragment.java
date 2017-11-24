package net.bluehack.stylens.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
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

import net.bluehack.stylens_android.R;

import java.util.ArrayList;

import static net.bluehack.stylens.utils.Logger.makeLogTag;

public class StaggeredGridFragment extends Fragment implements
        AbsListView.OnScrollListener, AbsListView.OnItemClickListener {

    private static final String TAG = makeLogTag(FeedFragment.class);
    private StaggeredGridView mGridView;
    private boolean mHasRequestedMore;
    private SampleAdapter mAdapter;

    private ArrayList<String> mData;

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

        mGridView = (StaggeredGridView) getView().findViewById(R.id.grid_view);

        if (savedInstanceState == null) {
            final LayoutInflater layoutInflater = getActivity().getLayoutInflater();

//                View header = layoutInflater.inflate(R.layout.list_item_header_footer, null);
//                View footer = layoutInflater.inflate(R.layout.list_item_header_footer, null);
//                TextView txtHeaderTitle = (TextView) header.findViewById(R.id.txt_title);
//                TextView txtFooterTitle = (TextView) footer.findViewById(R.id.txt_title);
//                txtHeaderTitle.setText("THE HEADER!");
//                txtFooterTitle.setText("THE FOOTER!");

//                mGridView.addHeaderView(header);
//                mGridView.addFooterView(footer);
        }

        if (mAdapter == null) {
            mAdapter = new SampleAdapter(getActivity(), R.id.txt_line1);
        }

        if (mData == null) {
            mData = SampleData.generateSampleData();
        }

        for (String data : mData) {
            mAdapter.add(data);
        }

        mGridView.setAdapter(mAdapter);
        mGridView.setOnScrollListener(this);
        mGridView.setOnItemClickListener(this);
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
                onLoadMoreItems();
            }
        }
    }

    private void onLoadMoreItems() {
        final ArrayList<String> sampleData = SampleData.generateSampleData();
        for (String data : sampleData) {
            mAdapter.add(data);
        }
        // stash all the data in our backing store
        mData.addAll(sampleData);
        // notify the adapter that we can update now
        mAdapter.notifyDataSetChanged();
        mHasRequestedMore = false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Toast.makeText(getActivity(), "Item Clicked: " + position, Toast.LENGTH_SHORT).show();
    }
}
