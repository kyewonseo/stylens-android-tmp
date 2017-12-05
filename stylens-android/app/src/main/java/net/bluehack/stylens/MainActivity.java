package net.bluehack.stylens;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraCharacteristics;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import net.bluehack.stylens.camera.CameraFragment;
import net.bluehack.stylens.camera.NativeCameraFragment;
import net.bluehack.stylens.gallery.GalleryFragment;
import net.bluehack.stylens.home.StaggeredGridFragment;
import net.bluehack.stylens.utils.UiUtil;
import net.bluehack.stylens_android.R;

import static net.bluehack.stylens.utils.Logger.LOGE;
import static net.bluehack.stylens.utils.Logger.makeLogTag;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = makeLogTag(MainActivity.class);
    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private static final String PERMISSION_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final int PERMISSIONS_REQUEST = 1;

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private TabLayout tabs;
    private Context context;
    private static boolean useCamera2API;

    @Override
    protected void onStart() {
        super.onStart();
        requestPermission();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        context = this;
        mViewPager = (ViewPager) findViewById(R.id.pager);
        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setIcon(UiUtil.getDrawable(context, R.drawable.btn_home_nor)));
        tabs.addTab(tabs.newTab().setIcon(UiUtil.getDrawable(context, R.drawable.btn_camera_nor)));
        tabs.addTab(tabs.newTab().setIcon(UiUtil.getDrawable(context, R.drawable.btn_gallery_nor)));
//        tabs.setTabGravity(TabLayout.SCROLL_INDICATOR_BOTTOM);


        if (hasPermission()) {
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
//                    fragment = CameraFragment.create(position);
                    fragment = NativeCameraFragment.create(position);

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

    private static boolean isHardwareLevelSupported(
            CameraCharacteristics characteristics, int requiredLevel) {
        int deviceLevel = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
        if (deviceLevel == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
            return requiredLevel == deviceLevel;
        }
        // deviceLevel is not LEGACY, can use numerical sort
        return requiredLevel <= deviceLevel;
    }

    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(PERMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(PERMISSION_STORAGE) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(PERMISSION_CAMERA) ||
                    shouldShowRequestPermissionRationale(PERMISSION_STORAGE)) {
                Toast.makeText(MainActivity.this,
                        "Camera AND storage permission are required for this demo", Toast.LENGTH_LONG).show();
            }
            requestPermissions(new String[] {PERMISSION_CAMERA, PERMISSION_STORAGE}, PERMISSIONS_REQUEST);
        }
    }
}
