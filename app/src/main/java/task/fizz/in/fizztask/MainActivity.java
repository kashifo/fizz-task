package task.fizz.in.fizztask;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import java.util.List;

import task.fizz.in.fizztask.models.Restaurant;
import task.fizz.in.fizztask.network.NetworkCallback;
import task.fizz.in.fizztask.network.VolleyRequest;


public class MainActivity extends AppCompatActivity {

    final String TAG = this.getClass().getSimpleName();
    RelativeLayout topView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView emptyView;
    
    List<Restaurant> restaurantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topView = (RelativeLayout) findViewById(R.id.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.rv_home_list);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        emptyView = (TextView) findViewById(R.id.tv_empty);

        getData();
    }

    void getData(){

        Log.i( TAG, "getData()" );
        showLoading();
        VolleyRequest request = new VolleyRequest(TAG);
        request.getRequest( dataCallback, Constants.URL_EXAMPLE, null );

    }

    NetworkCallback dataCallback = new NetworkCallback(this) {
        @Override
        public void onAPIResponse(boolean err, String str) {
            super.onAPIResponse(err, str);

            Log.d( TAG, "onAPIResponse="+str );
            hideLoading();

            if( err ){

                if( str!=null ) {
                    if (str.contains("UnknownHostException")) {
                        hideLoading();
                        showDialog("No Network");
                    } else {
                        showDialog("Error - " + err);
                    }
                }else{
                    showDialog("Unknown Error");
                }

            }else {

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

            /*if (!restaurantList.isEmpty()) {
                LeadsAdapter adapter = new LeadsAdapter(HomeFragment.this, restaurantList);
                recyclerView.setAdapter(adapter);
            }*/
        }

    }//parseTask

    void serializeData(String data){

        try {

            JSONObject jsonObject = new JSONObject(data);
            JSONArray dataArray = jsonObject.getJSONArray("data");

            Gson gson = new Gson();
            Type listType = new TypeToken<List<Restaurant>>() {
            }.getType();
            restaurantList = gson.fromJson(dataArray.toString(), listType);
            Log.d(TAG, "restaurantList.size=" + restaurantList.size());

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error - " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    void showDialog(String message){
        new AlertDialog.Builder(this)
                .setMessage( message )
                .setPositiveButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    void showLoading(){
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    void hideLoading(){
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }


}
