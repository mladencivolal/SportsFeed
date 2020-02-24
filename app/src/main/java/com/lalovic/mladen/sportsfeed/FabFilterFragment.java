package com.lalovic.mladen.sportsfeed;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FabFilterFragment extends AAH_FabulousFragment {

    private ArrayMap<String, List<String>> mAppliedFilters = new ArrayMap<>();
    private List<TextView> mTextViews = new ArrayList<>();

    static FabFilterFragment newInstance() {
        return new FabFilterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppliedFilters = ((MainActivity) Objects.requireNonNull(getActivity())).getApplied_filters();
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.fab_filter_fragment, null);

        RelativeLayout rlContent = contentView.findViewById(R.id.rl_content);
        LinearLayout llButtons = contentView.findViewById(R.id.ll_buttons);
        ImageButton imgbtnRefresh = contentView.findViewById(R.id.imgbtn_refresh);
        ImageButton imgbtnApply = contentView.findViewById(R.id.imgbtn_apply);
        ViewPager vpTypes = contentView.findViewById(R.id.vp_types);
        TabLayout tabsTypes = contentView.findViewById(R.id.tabs_types);

        imgbtnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFilter(mAppliedFilters);
            }
        });
        imgbtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (TextView tv : mTextViews) {
                    tv.setTag("unselected");
                    tv.setBackgroundResource(R.drawable.chip_unselected);
                    tv.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.filters_chips));
                }
                mAppliedFilters.clear();
            }
        });

        SectionsPagerAdapter adapter = new SectionsPagerAdapter();
        vpTypes.setOffscreenPageLimit(4);
        vpTypes.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        tabsTypes.setupWithViewPager(vpTypes);

        //params
        setAnimationDuration(200); //optional; default 500ms
        setPeekHeight(300); // optional; default 400dp
        setCallbacks((Callbacks) getActivity()); //optional; to get back result
        setAnimationListener((AnimationListener) getActivity()); //optional; to get animation callbacks
        setViewgroupStatic(llButtons); // optional; layout to stick at bottom on slide
        setViewPager(vpTypes); //optional; if you use viewpager that has scrollview
        setViewMain(rlContent); //necessary; main bottomsheet view
        setMainContentView(contentView); // necessary; call at end before super
        super.setupDialog(dialog, style); //call super at last
    }

    public class SectionsPagerAdapter extends PagerAdapter {

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup collection, int position) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.view_filters_sorters, collection, false);
            FlexboxLayout fbl = layout.findViewById(R.id.fbl);

            switch (position) {
                case 0:
                    inflateLayoutWithFilters("country", fbl);
                    break;
                case 1:
                    inflateLayoutWithFilters("sport", fbl);
                    break;
                case 2:
                    inflateLayoutWithFilters("author", fbl);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + position);
            }
            collection.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, @NonNull Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "COUNTRY";
                case 1:
                    return "SPORT";
                case 2:
                    return "AUTHOR";
            }
            return "";
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

    }

    private void inflateLayoutWithFilters(final String filter_category, FlexboxLayout fbl) {
        List<String> keys = new ArrayList<>();
        switch (filter_category) {
            case "country":
                keys = ((MainActivity) Objects.requireNonNull(getActivity())).getVideoData().getUniqueCountryKeys();
                break;
            case "sport":
                keys = ((MainActivity) Objects.requireNonNull(getActivity())).getVideoData().getUniqueSportKeys();
                break;
            case "author":
                keys = ((MainActivity) Objects.requireNonNull(getActivity())).getVideoData().getUniqueAuthorKeys();
                break;
        }

        for (int i = 0; i < keys.size(); i++) {
            View subchild = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.single_chip, null);
            final TextView tv = (subchild.findViewById(R.id.txt_title));
            tv.setText(keys.get(i));
            final int finalI = i;
            final List<String> finalKeys = keys;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tv.getTag() != null && tv.getTag().equals("selected")) {
                        tv.setTag("unselected");
                        tv.setBackgroundResource(R.drawable.chip_unselected);
                        tv.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.filters_chips));
                        removeFromSelectedMap(filter_category, finalKeys.get(finalI));
                    } else {
                        tv.setTag("selected");
                        tv.setBackgroundResource(R.drawable.chip_selected);
                        tv.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.filters_chips_text_selected));
                        addToSelectedMap(filter_category, finalKeys.get(finalI));
                    }
                }
            });

            if (mAppliedFilters != null && mAppliedFilters.get(filter_category) != null && Objects.requireNonNull(mAppliedFilters.get(filter_category)).contains(keys.get(finalI))) {
                tv.setTag("selected");
                tv.setBackgroundResource(R.drawable.chip_selected);
                tv.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.filters_chips_text_selected));
            } else {
                tv.setBackgroundResource(R.drawable.chip_unselected);
                tv.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.filters_chips));
            }
            mTextViews.add(tv);
            fbl.addView(subchild);
        }
    }

    private void addToSelectedMap(String key, String value) {
        if (mAppliedFilters.get(key) != null && !Objects.requireNonNull(mAppliedFilters.get(key)).contains(value)) {
            Objects.requireNonNull(mAppliedFilters.get(key)).add(value);
        } else {
            List<String> temp = new ArrayList<>();
            temp.add(value);
            mAppliedFilters.put(key, temp);
        }
    }

    private void removeFromSelectedMap(String key, String value) {
        if (Objects.requireNonNull(mAppliedFilters.get(key)).size() == 1) {
            mAppliedFilters.remove(key);
        } else {
            Objects.requireNonNull(mAppliedFilters.get(key)).remove(value);
        }
    }
}
