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


public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.MyViewHolder> {

    private final String TAG = getClass().getSimpleName();
    Context context;
    List<Restaurant> dataList = new ArrayList<>();
    
    public HomeListAdapter(Context context, List<Restaurant> pdataList) {
        this.context = context;
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

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + rowData.getContactNumber() ));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity( intent );

                }
            });


            paramHolder.ib_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String latitude = rowData.getCoordinates().getLatitude();
                        Log.i( TAG, "latitude="+latitude );

                        /*String uri = String.format(Locale.ENGLISH, "geo:%s,%s", itemList.get(pos).getLatitude(), itemList.get(pos).getLongitude() );
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);*/
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText( context, "Error:"+e.getMessage(), Toast.LENGTH_LONG ).show();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}