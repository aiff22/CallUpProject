package com.aiff.callup_001;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PageFragment extends Fragment{
    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    int pageNumber;
    ViewPager mViewPager;

    static PageFragment newInstance(int page) {
        PageFragment pageFragment = new PageFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;

        if (pageNumber == 0) {
            view = inflater.inflate(R.layout.fragment, null);
        }

        if (pageNumber == 1) {
            view = inflater.inflate(R.layout.fragment2, null);
        }

        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        return view;
    }
}
