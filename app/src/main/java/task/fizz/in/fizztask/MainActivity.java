package task.fizz.in.fizztask;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import task.fizz.in.fizztask.network.VolleyRequest;

import static task.fizz.in.fizztask.R.id.progressBar;

public class MainActivity extends AppCompatActivity {

    final String TAG = this.getClass().getSimpleName();
    LinearLayout topView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    void getData(){

        Log.i( TAG, "getData()" );
        showLoading();
        VolleyRequest request = new VolleyRequest(TAG);
        request.getRequest( desigCallback, Constants.URL_LEADS, null );

    }

    NetworkCallback desigCallback = new NetworkCallback(this) {
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


    @Override
    public void enquiryInterface(Bundle bundle) {

    }


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

            if (!leadsList.isEmpty()) {
                LeadsAdapter adapter = new LeadsAdapter(HomeFragment.this, leadsList);
                recyclerView.setAdapter(adapter);
            }
        }

    }//parseTask

    void serializeData(String data){

        try {

            Gson gson = new Gson();
            Type listType = new TypeToken<List<Lead>>() {
            }.getType();
            leadsList = gson.fromJson(data, listType);
            Log.d(TAG, "leadsList.size=" + leadsList.size());

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error - " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    void showDialog(String message){
        new AlertDialog.Builder(getActivity())
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
