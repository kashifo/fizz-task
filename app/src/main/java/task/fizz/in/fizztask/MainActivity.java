package task.fizz.in.fizztask;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import static task.fizz.in.fizztask.Constants.DEFAULT_CATEGORY;
import static task.fizz.in.fizztask.Constants.DEFAULT_LATITUDE;
import static task.fizz.in.fizztask.Constants.DEFAULT_LIMIT;
import static task.fizz.in.fizztask.Constants.DEFAULT_LONGITUDE;
import static task.fizz.in.fizztask.Constants.DEFAULT_TOKEN;
import static task.fizz.in.fizztask.Constants.URL_BASE;


public class MainActivity extends AppCompatActivity implements ItemClickListener {

    private final String TAG = this.getClass().getSimpleName();
    RelativeLayout topView;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    int currentPage = 1;
    boolean isLoading, isLastPage;
    ProgressBar progressBar, progressBarMini;
    TextView emptyView;
    HomeListAdapter adapter;

    List<Restaurant> restaurantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topView = (RelativeLayout) findViewById(R.id.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rv_home_list);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setItemAnimator(new SlideInUpAnimator());
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBarMini = (ProgressBar) findViewById(R.id.progressBarMini);
        emptyView = (TextView) findViewById(R.id.tv_empty);

        getData();
    }

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
                    currentPage+=1;
                    getData();
                }
            }
        }
    };

    void getData() {

        String URL = URL_BASE + "/" + DEFAULT_LATITUDE + "/" + DEFAULT_LONGITUDE + "/" + DEFAULT_CATEGORY + "/" + currentPage + "/" + DEFAULT_LIMIT + DEFAULT_TOKEN;

        Log.i(TAG, "getData()");
        isLoading = true;
        if( currentPage>1 ){
            progressBarMini.setVisibility(View.VISIBLE);
        }

        VolleyRequest request = new VolleyRequest(TAG);
        request.getRequest(dataCallback, URL, null);

    }

    NetworkCallback dataCallback = new NetworkCallback(this) {
        @Override
        public void onAPIResponse(boolean err, String str) {
            super.onAPIResponse(err, str);

            Log.d(TAG, "onAPIResponse=" + str);

            if (err) {

                if (str != null) {
                    if (str.contains("UnknownHostException")) {
                        showDialog("No Network");
                    } else {
                        showDialog("Error - " + err);
                    }
                } else {
                    showDialog("Unknown Error");
                }

            } else {

                new parseTask(str).execute();

            }
        }
    };


    class parseTask extends AsyncTask<Void, Void, Void> {

        String data;

        public parseTask(String data) {
            this.data = data;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        public Void doInBackground(Void... args) {
            //setData(rawData);
            serializeData(data);
            return null;
        }

        @Override
        public void onPostExecute(Void result) {
            super.onPostExecute(result);

            isLoading = false;

            if ( restaurantList!=null && !restaurantList.isEmpty()) {

                if (adapter == null) {
                    progressBar.setVisibility(View.GONE);
                    adapter = new HomeListAdapter(MainActivity.this, restaurantList);
                    adapter.setClickListener(MainActivity.this);
                    recyclerView.setAdapter(adapter);
                } else {
                    progressBarMini.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
            }
        }

    }//parseTask

    @Override
    public void onClick(int position) {
        Log.d( TAG, "onClick received" );
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("data", restaurantList.get(position));
        startActivity(intent);
    }

    void serializeData(String data) {

        try {

            JSONObject jsonObject = new JSONObject(data);
            JSONArray dataArray = jsonObject.getJSONArray("data");

            if( dataArray.length() > 0 ) {

                Gson gson = new Gson();
                Type listType = new TypeToken<List<Restaurant>>() {
                }.getType();
                List<Restaurant> restaurantListTemp = gson.fromJson(dataArray.toString(), listType);

                if ( restaurantListTemp!=null && !restaurantListTemp.isEmpty()) {

                    if(restaurantList==null){
                        restaurantList = new ArrayList<>();
                    }

                    restaurantList.addAll(restaurantListTemp);
                    Log.d(TAG, "restaurantList.size=" + restaurantList.size());
                }
            }else{
                isLastPage = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error - " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    void showDialog(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }


}
