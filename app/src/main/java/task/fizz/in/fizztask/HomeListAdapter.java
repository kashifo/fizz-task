package task.fizz.in.fizztask;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import task.fizz.in.fizztask.models.Restaurant;

import static task.fizz.in.fizztask.Commons.notEmpty;


public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.MyViewHolder> {

    private static final String TAG = HomeListAdapter.class.getSimpleName();
    private Context context;
    private List<Restaurant> dataList = new ArrayList<>();
    private ItemClickListener clickListener;

    public HomeListAdapter(Context context, List<Restaurant> pdataList) {
        this.context = context;
        this.dataList = pdataList;
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        if (dataList != null)
            return dataList.size();
        else
            return 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.list_row, viewGroup, false);

        return new MyViewHolder(itemView);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected ImageView iv_bg;
        protected ImageView iv_logo;
        protected TextView tv_name;
        protected TextView tv_address;
        protected ImageButton ib_dial;
        protected ImageButton ib_map;

        public MyViewHolder(View v) {
            super(v);

            iv_bg = (ImageView) v.findViewById(R.id.iv_bgImg);
            iv_logo = (ImageView) v.findViewById(R.id.iv_logo);
            tv_name = (TextView) v.findViewById(R.id.tv_name);
            tv_address = (TextView) v.findViewById(R.id.tv_address);
            ib_dial = (ImageButton) v.findViewById(R.id.ib_dial);
            ib_map = (ImageButton) v.findViewById(R.id.ib_map);
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onClick(getAdapterPosition());
            }
        }
    }//ViewHolder


    @Override
    public void onBindViewHolder(MyViewHolder paramHolder, final int position) {

        try {

            final Restaurant rowData = dataList.get(position);

            Glide
                    .with(context)
                    .load(rowData.getHeroImage())
                    .centerCrop()
                    .into(paramHolder.iv_bg);

            Glide
                    .with(context)
                    .load(rowData.getLogo())
                    .centerCrop()
                    .into(paramHolder.iv_logo);

            paramHolder.tv_name.setText(rowData.getName());
            paramHolder.tv_address.setText(rowData.getArea());

            paramHolder.ib_dial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if( notEmpty(rowData.getContactNumber()) ) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + rowData.getContactNumber()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context, "Sorry, phone no. not available", Toast.LENGTH_LONG).show();
                    }

                }
            });


            paramHolder.ib_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String latitude = String.valueOf(rowData.getLatitude());
                        String longitude = String.valueOf(rowData.getLongitude());
                        Log.i(TAG, "latitude=" + latitude + "longitude=" + longitude);

                        String geoUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude + " (" + rowData.getName() + ")";
                        ;
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}