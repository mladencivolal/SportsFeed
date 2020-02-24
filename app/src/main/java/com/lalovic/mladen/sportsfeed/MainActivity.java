package com.lalovic.mladen.sportsfeed;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robert.autoplayvideo.CustomRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements VideoItemAdapter.OnItemClickListener,
        AAH_FabulousFragment.Callbacks, AAH_FabulousFragment.AnimationListener {
    public static final String EXTRA_URL = "videoUrl";

    public static final String OBJECT_AUTHOR = "author";
    public static final String STRING_AUTHOR_NAME = "name";
    public static final String OBJECT_VIDEO = "video";
    public static final String STRING_VIDEO_POSTER = "poster";
    public static final String STRING_VIDEO_URL = "url";
    public static final String STRING_VIEWS = "views";
    public static final String STRING_DESCRIPTION = "description";
    public static final String OBJECT_ATHLETE = "athlete";
    public static final String OBJECT_ATHLETE_COUNTRY = "country";
    public static final String OBJECT_ATHLETE_SPORT = "sport";
    public static final String STRING_ATHLETE_COUNTRY_FLAG = "icon";
    public static final String STRING_ATHLETE_SPORT_ICON = "icon";
    public static final String STRING_ATHLETE_SPORT_NAME = "name";
    public static final String STRING_ATHLETE_COUNTRY_NAME = "name";

    private FloatingActionButton fab_filter;
    private FloatingActionButton fab_top;
    private CustomRecyclerView mRecyclerView;

    private VideoData mVideoData;
    private VideoItemAdapter mVideoItemAdapter;
    private List<VideoItem> mVideoItems;

    //volley
    private RequestQueue mRequestQueue;

    private ArrayMap<String, List<String>> applied_filters = new ArrayMap<>();
    FabFilterFragment dialogFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initFabFilterFragment();
        recyclerViewVideoConfig();

        mVideoItems = new ArrayList<>();
        mRequestQueue = VolleySingleton.getInstance(this).getRequestQueue();

        parseJSON();

        mVideoItemAdapter = new VideoItemAdapter(MainActivity.this, mVideoItems);
        mVideoItemAdapter.setOnItemClickListener(MainActivity.this);
        mRecyclerView.setAdapter(mVideoItemAdapter);
    }

    private void parseJSON() {
        String apiUrl = "https://private-anon-b3562530e2-technicaltaskapi.apiary-mock.com/feed?sport=football";

        final JsonArrayRequest request = new JsonArrayRequest(apiUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mVideoItems.clear();
                mVideoItemAdapter.notifyDataSetChanged();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        String author = response.getJSONObject(i).getJSONObject(OBJECT_AUTHOR).getString(STRING_AUTHOR_NAME);
                        String cover = response.getJSONObject(i).getJSONObject(OBJECT_VIDEO).getString(STRING_VIDEO_POSTER);
                        String viewsCount = response.getJSONObject(i).getString(STRING_VIEWS);
                        String url = response.getJSONObject(i).getJSONObject(OBJECT_VIDEO).getString(STRING_VIDEO_URL);
                        String flagIcon = response.getJSONObject(i).getJSONObject(OBJECT_ATHLETE).getJSONObject(OBJECT_ATHLETE_COUNTRY).getString(STRING_ATHLETE_COUNTRY_FLAG);
                        String sportIcon = response.getJSONObject(i).getJSONObject(OBJECT_ATHLETE).getJSONObject(OBJECT_ATHLETE_SPORT).getString(STRING_ATHLETE_SPORT_ICON);
                        String description = response.getJSONObject(i).getString(STRING_DESCRIPTION);
                        String sport = response.getJSONObject(i).getJSONObject(OBJECT_ATHLETE).getJSONObject(OBJECT_ATHLETE_SPORT).getString(STRING_ATHLETE_SPORT_NAME);
                        String country = response.getJSONObject(i).getJSONObject(OBJECT_ATHLETE).getJSONObject(OBJECT_ATHLETE_COUNTRY).getString(STRING_ATHLETE_COUNTRY_NAME);

                        mVideoItems.add(new VideoItem(cover, url, author, viewsCount, flagIcon, sportIcon, description, sport, country));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mVideoData = new VideoData(mVideoItems);
        mRequestQueue.add(request);
    }

    public void initUI() {
        fab_filter = findViewById(R.id.fab_filter);
        fab_top = findViewById(R.id.fab_top);
        mRecyclerView = findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0 && !fab_filter.isShown()) {
                    fab_filter.show();
                    fab_top.show();
                } else if (dy > 0 && fab_filter.isShown()) {
                    fab_filter.hide();
                    fab_top.hide();
                }
            }
        });
    }

    private void initFabFilterFragment() {
        dialogFrag = FabFilterFragment.newInstance();
        dialogFrag.setParentFab(fab_filter);
        fab_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFrag.show(getSupportFragmentManager(), dialogFrag.getTag());
            }
        });
        fab_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });
    }

    private void recyclerViewVideoConfig() {
        mRecyclerView.setActivity(this);
        //mRecyclerView.setPlayOnlyFirstVideo(true);
        mRecyclerView.setCheckForMp4(false);
        mRecyclerView.setDownloadPath(Environment.getExternalStorageDirectory() + "/MyVideo");
        mRecyclerView.setDownloadVideos(true);
        //mRecyclerView.setVisiblePercent(60);
        mRecyclerView.smoothScrollBy(0, 1);
        mRecyclerView.smoothScrollBy(0, -1);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra(EXTRA_URL, mVideoItems.get(position).getUrl());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecyclerView.playAvailableVideos(0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRecyclerView.stopVideos();
    }

    @Override
    public void onResult(Object result) {
        if (result.toString().equalsIgnoreCase("swiped_down")) {
            //do something or nothing
        } else {
            ArrayMap<String, List<String>> applied_filters = (ArrayMap<String, List<String>>) result;
            if (applied_filters.size() != 0) {
                List<VideoItem> filteredList = mVideoData.getAllVideos();
                //iterate over arraymap
                for (Map.Entry<String, List<String>> entry : applied_filters.entrySet()) {
                    switch (entry.getKey()) {
                        case "country":
                            filteredList = mVideoData.getCountryFilteredVideos(entry.getValue(), filteredList);
                            break;
                        case "sport":
                            filteredList = mVideoData.getSportFilteredVideos(entry.getValue(), filteredList);
                            break;
                        case "author":
                            filteredList = mVideoData.getAuthorFilteredMovies(entry.getValue(), filteredList);
                            break;
                    }
                }
                mVideoItems.clear();
                mVideoItems.addAll(filteredList);
                mVideoItemAdapter.notifyDataSetChanged();

            } else {
                parseJSON();
                mVideoItems.addAll(mVideoData.getAllVideos());
                mVideoItemAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onOpenAnimationStart() {

    }

    @Override
    public void onOpenAnimationEnd() {

    }

    @Override
    public void onCloseAnimationStart() {

    }

    @Override
    public void onCloseAnimationEnd() {

    }

    public ArrayMap<String, List<String>> getApplied_filters() {
        return applied_filters;
    }

    public VideoData getVideoData() {
        return mVideoData;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (dialogFrag.isAdded()) {
            dialogFrag.dismiss();
            dialogFrag.show(getSupportFragmentManager(), dialogFrag.getTag());
        }
    }
}