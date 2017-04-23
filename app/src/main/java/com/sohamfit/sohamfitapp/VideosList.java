package com.sohamfit.sohamfitapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.sohamfit.sohamfitapp.R.id.fab;

/**
 * Created by leonardogedler on 4/21/17.
 */

public class VideosList extends AppCompatActivity {

    public static final String TAG = VideosList.class.getSimpleName();

    private List<Video> mVideos = new ArrayList<>();
    protected boolean mViewStop = false;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private VideosAdapter mAdapter;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    private int skip = 0;
    private int videoPerLoad = 10;


    // Views
    private ProgressBar mTopProgressBar;
    private ProgressBar mBottomProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FloatingActionButton mFab;
    private Toolbar mToolbar;
    private TextView mNoResults;

    // Filters
    private boolean mFilters = false;
    private boolean mFilterByVideoType = false;
    private String mVideoType;
    private boolean mFilterByVideoLevel = false;
    private String mVideoLevel;
    private boolean mSortBy = false;
    private String mSortByString;
    private boolean mSearch = false;
    private String mSearchText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);


        // Receiving Intents
        if (getIntent().getExtras() != null){
            mFilters = getIntent().getExtras().getBoolean("filters");
            mFilterByVideoType = getIntent().getExtras().getBoolean("filterByVideoType");
            mVideoType = getIntent().getExtras().getString("videoType");
            mFilterByVideoLevel = getIntent().getExtras().getBoolean("filterByVideoLevel");
            mVideoLevel = getIntent().getExtras().getString("videoLevel");
            mSortBy = getIntent().getExtras().getBoolean("sortBy");
            mSortByString = getIntent().getExtras().getString("sortString");
            mSearch = getIntent().getExtras().getBoolean("search");
            mSearchText = getIntent().getExtras().getString("searchText");
        }

        // Views
        mTopProgressBar = (ProgressBar) findViewById(R.id.top_progress_bar);
        mBottomProgressBar = (ProgressBar) findViewById(R.id.bottom_progress_bar);
        mNoResults = (TextView) findViewById(R.id.noResultsText);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_video_list_refresh_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mFab = (FloatingActionButton) findViewById(fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(VideosList.this, Filters.class);
                if(mFilters){
                    intent.putExtra("filters", true);
                    if(mSearch){
                        intent.putExtra("search", true);
                        intent.putExtra("searchText", mSearchText);
                    }
                    if(mFilterByVideoType){
                        intent.putExtra("filterByVideoType", true);
                        intent.putExtra("videoType", mVideoType);
                    }
                    if(mFilterByVideoLevel){
                        intent.putExtra("filterByVideoLevel", true);
                        intent.putExtra("videoLevel", mVideoLevel);
                    }
                    if(mSortBy){
                        intent.putExtra("sortBy", true);
                        intent.putExtra("sortString", mSortByString);
                    }
                }
                startActivity(intent);
            }
        });


        // Initialize recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new VideosAdapter(VideosList.this, mVideos);
        mRecyclerView.setAdapter(mAdapter);

        // Set infinite scroll to load new data
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
            // Check for scroll down
            if(dy > 0) {
                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = false;
                        mBottomProgressBar.setVisibility(View.VISIBLE);
                        skip = skip + videoPerLoad;
                        getVideos(skip);
                    }
                }
            }
            }
        });


        // Set on swipeRefresh
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            mVideos.clear();
            skip = 0;
            getVideos(skip);
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(
            R.color.colorPrimary,
            R.color.colorPrimaryDark);

        // Get Videos
        getVideos(skip);

    }

    public void getVideos(final int skip){

        RequestParams params = new RequestParams();

        // Pagination
        params.add("limit", String.valueOf(videoPerLoad));
        params.add("skip", String.valueOf(skip));

        //Filters
        JSONObject objectWhere = new JSONObject();
        // Search or Filter by
        if (mSearch || mFilterByVideoType || mFilterByVideoLevel) {
            // Search
            if(mSearch){
                String[] searchArray = mSearchText.toLowerCase().split("\\s+");
                JSONArray wordsArray = new JSONArray(Arrays.asList(searchArray));
                try {
                    objectWhere.put("words", new JSONObject().put("$all", wordsArray));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(mFilterByVideoType){
                try {
                    objectWhere.put("subcategory", mVideoType.toLowerCase());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(mFilterByVideoLevel){
                try {
                    objectWhere.put("level", mVideoLevel.toLowerCase());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            params.add("where", objectWhere.toString());

        }
        // Order by
        if(mSortBy){
            params.add("order", mSortByString);
        }

        // Add instructor and user
        params.add("include", "instructor.userPointer");


        HttpUtils.getVideos("/classes/Videos", params,  new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // Hide no results text
                mNoResults.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                loading = true;
                try {
                    JSONObject result = new JSONObject(response.toString());
                    JSONArray videosArray = result.getJSONArray("results");


                    for (int i=0;i<videosArray.length();i++){
                        JSONObject videoJson = videosArray.getJSONObject(i);

                        Video video = new Video();
                        video.objectId = videoJson.getString(Constants.OBJECT_ID);
                        video.videoName = videoJson.getString(Constants.VIDEO_NAME);
                        video.videoDescription = videoJson.getString(Constants.VIDEO_DESCRIPTION);
                        video.videoDuration = videoJson.getString(Constants.VIDEO_DURATION);
                        video.videoLevel = videoJson.getString(Constants.VIDEO_LEVEL);
                        video.videoLevel = videoJson.getString(Constants.VIDEO_LEVEL);
                        video.createdAt = videoJson.getString(Constants.CREATED_AT);
                        video.videoMp4Url = videoJson.getString(Constants.VIDEO_URL);

                        try {
                            // Video poster
                            JSONObject poster = (JSONObject) videoJson.get(Constants.VIDEO_POSTER_OBJECT);
                            video.videoPosterUrl= poster.getString(Constants.URL);

                            // Video Instructor
                            JSONObject instructor = (JSONObject) videoJson.get(Constants.VIDEO_INSTRUCTOR);
                            JSONObject user = (JSONObject) instructor.get(Constants.USER_POINTER);

                            // Instructor name
                            video.videoInstructorName = user.getString(Constants.USER_FIST_NAME) + " " +user.getString(Constants.USER_LAST_NAME);

                            // Instructor image
                            JSONObject userImage = (JSONObject) user.get("profilePictureThumbnail");
                            video.videoInstructorImage = userImage.getString(Constants.URL);
                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                        mVideos.add(video);
                    }

                    if (!mViewStop) {

                        mTopProgressBar.setVisibility(View.GONE);
                        mBottomProgressBar.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(false);
                        mAdapter.notifyItemInserted(mVideos.size());
                    }

                    Log.d("videos", "---->>:" + mVideos.size());
                    // Check fot videos size
                    if(mVideos.size() == 0){
                        mNoResults.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_videos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onStart() {
        super.onStart();
        mViewStop = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        mViewStop = true;
    }
}
