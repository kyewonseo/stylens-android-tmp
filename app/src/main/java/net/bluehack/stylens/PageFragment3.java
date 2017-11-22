package net.bluehack.stylens;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PageFragment3 extends Fragment {
    private int mPageNumber;

    public static PageFragment3 create(int pageNumber) {
        PageFragment3 fragment = new PageFragment3();
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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment3_page, container, false);
        ((TextView) rootView.findViewById(R.id.number)).setText(mPageNumber + "");
        return rootView;
    }
}
