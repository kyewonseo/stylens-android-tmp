package net.bluehack.stylens.gallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.bluehack.stylens_android.R;

public class GalleryFragment extends Fragment {
    private int mPageNumber;

    public static GalleryFragment create(int pageNumber) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putInt("page", pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt("page");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_gallery, container, false);
        ((TextView) rootView.findViewById(R.id.number)).setText(mPageNumber + "");
        return rootView;
    }
}
