package task.fizz.in.fizztask;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import task.fizz.in.fizztask.models.Restaurant;


public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.MyViewHolder> {

    List<Restaurant> dataList = new ArrayList<>();

    MyViewHolder myViewHolder;

    public HomeListAdapter(List<Restaurant> pdataList) {
        this.dataList = pdataList;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.list_row, viewGroup, false);

        return new MyViewHolder(itemView);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        protected TextView titleView;

        public MyViewHolder(View v) {
            super(v);

            titleView = (TextView) v.findViewById(R.id.tv_name);


        }
    }//ViewHolder


    @Override
    public void onBindViewHolder(MyViewHolder paramHolder, final int position) {
        //ContactInfo ci = contactList.get(i);
        myViewHolder = paramHolder;

        try {

            Restaurant rowData = (Restaurant) dataList.get(position);
            myViewHolder.titleView.setText( rowData.getName() );

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}