package task.fizz.in.fizztask;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import task.fizz.in.fizztask.models.Restaurant;
import task.fizz.in.fizztask.network.NetworkCallback;
import task.fizz.in.fizztask.network.VolleyRequest;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;
import static task.fizz.in.fizztask.Commons.notEmpty;
import static task.fizz.in.fizztask.Constants.DEFAULT_CATEGORY;
import static task.fizz.in.fizztask.Constants.DEFAULT_LATITUDE;
import static task.fizz.in.fizztask.Constants.DEFAULT_LIMIT;
import static task.fizz.in.fizztask.Constants.DEFAULT_LONGITUDE;
import static task.fizz.in.fizztask.Constants.DEFAULT_TOKEN;
import static task.fizz.in.fizztask.Constants.URL_BASE;

//First activity, shows when app is launched
public class MainActivity extends AppCompatActivity implements ItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RelativeLayout topView;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private int currentPage = 1;
    private boolean isLoading, isLastPage;
    private ProgressBar progressBar, progressBarMini;
    private TextView emptyView;
    private HomeListAdapter adapter;
    private List<Restaurant> restaurantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topView = (RelativeLayout) findViewById(R.id.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rv_home_list);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBarMini = (ProgressBar) findViewById(R.id.progressBarMini);
        emptyView = (TextView) findViewById(R.id.tv_empty);

        getData();
    }

    //pagination
    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!isLoading && !isLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= PAGE_SIZE) {
                    currentPage += 1;
                    getData();
                }
            }
        }
    };

    //creates url and requests calls API
    private void getData() {

        String URL = URL_BASE + "/" + DEFAULT_LATITUDE + "/" + DEFAULT_LONGITUDE + "/" + DEFAULT_CATEGORY + "/" + currentPage + "/" + DEFAULT_LIMIT + DEFAULT_TOKEN;

        Log.i(TAG, "getData()-" + URL);
        isLoading = true;
        if (currentPage > 1) {
            progressBarMini.setVisibility(View.VISIBLE);
        }

        VolleyRequest request = new VolleyRequest(TAG);
        request.getRequest(dataCallback, URL, null);

    }

    //called when response is received from API
    private NetworkCallback dataCallback = new NetworkCallback(this) {
        @Override
        public void onAPIResponse(boolean err, String str) {
            super.onAPIResponse(err, str);

            Log.d(TAG, "onAPIResponse=" + str);

            if ( notEmpty(str) ) {

                if (err) {

                    if (str.contains("UnknownHostException")) {
                        Snackbar.make(topView, "No Network", Snackbar.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(topView, "Error - " + str, Snackbar.LENGTH_LONG).show();
                    }
                    //status code is checked in parsing section, not here

                } else {

                    //when there is no error, parse data
                    new parseTask(str).execute();

                }
            }
        }
    };


    //for background parsing and serializing
    private class parseTask extends AsyncTask<Void, Void, Void> {

        String data;

        parseTask(String data) {
            this.data = data;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        public Void doInBackground(Void... args) {
            serializeData(data);
            return null;
        }

        @Override
        public void onPostExecute(Void result) {
            super.onPostExecute(result);

            isLoading = false;

            if ( notEmpty(restaurantList) ) {

                if (adapter == null) {
                    progressBar.setVisibility(View.GONE);
                    adapter = new HomeListAdapter(MainActivity.this, restaurantList);
                    adapter.setClickListener(MainActivity.this);
                    recyclerView.setAdapter(adapter);
                } else {
                    progressBarMini.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
            }else{
                //show no data when there is no data on the first load, not during pagination
                if( adapter==null ){
                    emptyView.setVisibility(View.VISIBLE);
                }
            }
        }

    }//parseTask

    //click callback received from adapter
    @Override
    public void onClick(int position) {
        Log.d(TAG, "onClick received");
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("data", restaurantList.get(position));
        startActivity(intent);
    }

    //serialized using gson
    private void serializeData(String data) {

        try {

            JSONObject rootObject = new JSONObject(data);

            if( rootObject.getBoolean("status") && rootObject.getInt("statusCode")==200 ) {

                JSONArray dataArray = rootObject.getJSONArray("data");

                if (dataArray.length() > 0) {

                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Restaurant>>() {
                    }.getType();
                    List<Restaurant> restaurantListTemp = gson.fromJson(dataArray.toString(), listType);

                    if (notEmpty(restaurantListTemp)) {

                        if (restaurantList == null) {
                            restaurantList = new ArrayList<>();
                        }

                        restaurantList.addAll(restaurantListTemp);
                        Log.d(TAG, "restaurantList.size=" + restaurantList.size());
                    }
                } else {
                    isLastPage = true;
                }
            }else{
                Snackbar.make(topView, "Error - Status Code = " + rootObject.getInt("statusCode"), Snackbar.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Snackbar.make(topView, "Error - " + e.getMessage(), Snackbar.LENGTH_LONG).show();
        }

    }

}
