package net.bluehack.stylens;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;

import net.bluehack.stylens.camera.CameraFragment;
import net.bluehack.stylens.gallery.GalleryFragment;
import net.bluehack.stylens.home.FeedFragment;
import net.bluehack.stylens.home.StaggeredGridFragment;
import net.bluehack.stylens_android.R;

import java.util.List;

import io.swagger.client.model.GetFeedResponse;
import io.swagger.client.model.Product;

import static net.bluehack.stylens.utils.Logger.LOGD;
import static net.bluehack.stylens.utils.Logger.LOGE;
import static net.bluehack.stylens.utils.Logger.makeLogTag;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = makeLogTag(MainActivity.class);
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private TabLayout tabs;
    private Context context;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        mViewPager = (ViewPager) findViewById(R.id.pager);
        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("Tab 1"));
        tabs.addTab(tabs.newTab().setText("Tab 2"));
        tabs.addTab(tabs.newTab().setText("Tab 3"));
//        tabs.setTabGravity(TabLayout.SCROLL_INDICATOR_BOTTOM);

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }



    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // 해당하는 page의 Fragment를 생성합니다.
            Fragment fragment = null;
            switch (position) {
                case 0:
//                    fragment = FeedFragment.create(position);
                    fragment = StaggeredGridFragment.create(position);
                    break;
                case 1:
                    fragment = CameraFragment.create(position);
                    break;
                case 2:
                    fragment = GalleryFragment.create(position);
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;  // 총 3개의 page를 보여줍니다.
        }

    }
}
