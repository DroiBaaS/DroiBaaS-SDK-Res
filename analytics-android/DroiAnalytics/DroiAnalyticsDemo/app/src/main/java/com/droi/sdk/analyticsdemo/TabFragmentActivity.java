package com.droi.sdk.analyticsdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.droi.sdk.analyticsdemo.R;
import com.droi.sdk.analytics.DroiAnalytics;

/**
 * Created by chenpei on 16/1/5.
 */
public class TabFragmentActivity extends FragmentActivity {
    private FragmentTabHost mFragmentTabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.droi_example_tab_fragment);
        mFragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mFragmentTabHost.setup(this, getSupportFragmentManager(), R.id.content);
        mFragmentTabHost.addTab(mFragmentTabHost.newTabSpec("Fragment1")
                .setIndicator("Fragment1"), Fragment1.class, null);
        mFragmentTabHost.addTab(mFragmentTabHost.newTabSpec("Fragment2")
                .setIndicator("Fragment2"), Fragment2.class, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DroiAnalytics.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DroiAnalytics.onPause(this);
    }

    public static class Fragment1 extends Fragment {
        private final String mPageName = "Fragment1";

        static Fragment1 newInstance(int num) {
            Fragment1 f = new Fragment1();
            Bundle b = new Bundle();
            b.putInt("num", num);
            f.setArguments(b);
            return f;
        }

        @Override
        public void onResume() {
            super.onResume();
            DroiAnalytics.onFragmentStart(getActivity(), mPageName);
        }

        @Override
        public void onPause() {
            super.onPause();
            DroiAnalytics.onFragmentEnd(getActivity(), mPageName);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            TextView textView = new TextView(getActivity());
            textView.setText("Fragment1");
            return textView;
        }
    }

    public static class Fragment2 extends Fragment {
        private final String mPageName = "Fragment2";

        static Fragment1 newInstance(int num) {
            Fragment1 f = new Fragment1();
            Bundle b = new Bundle();
            b.putInt("num", num);
            f.setArguments(b);
            return f;
        }

        @Override
        public void onResume() {
            super.onResume();
            DroiAnalytics.onFragmentStart(getActivity(), mPageName);
        }

        @Override
        public void onPause() {
            super.onPause();
            DroiAnalytics.onFragmentEnd(getActivity(), mPageName);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            TextView textView = new TextView(getActivity());
            textView.setText("Fragment2");
            return textView;
        }
    }
}
