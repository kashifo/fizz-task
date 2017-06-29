package task.fizz.in.fizztask;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import task.fizz.in.fizztask.models.Restaurant;

public class DetailActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    String title = "Details";
    Restaurant restaurant;

    ImageView iv_bgImg;
    ImageView iv_logo;
    TextView tv_name;
    TextView tv_area;
    TextView tv_time;
    TextView tv_offerDetails;
    TextView tv_address;
    TextView tv_distance;
    TextView tv_otherDetails;
    FloatingActionButton fab_call;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setupToolbar();
        initViews();

        try{
            restaurant = (Restaurant) getIntent().getSerializableExtra("data");
            loadData();
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void setupToolbar(){
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(title);
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }


    private void initViews(){

        iv_bgImg = (ImageView) findViewById(R.id.iv_bgImg);
        iv_logo = (ImageView) findViewById(R.id.iv_logo);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_area = (TextView) findViewById(R.id.tv_area);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_offerDetails = (TextView) findViewById(R.id.tv_offerDetails);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_distance = (TextView) findViewById(R.id.tv_distance);
        tv_otherDetails = (TextView) findViewById(R.id.tv_otherDetails);

        fab_call = (FloatingActionButton) findViewById(R.id.fab_call);
        fab_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + restaurant.getContactNumber() ));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity( intent );
            }
        });

    }

    private void loadData(){

        if(restaurant!=null){

            try{

                Glide
                        .with(this)
                        .load(restaurant.getHeroImage())
                        .centerCrop()
                        .into(iv_bgImg);

                Glide
                        .with(this)
                        .load(restaurant.getLogo())
                        .centerCrop()
                        .into(iv_logo);

                tv_name.setText( restaurant.getName() );
                tv_area.setText( restaurant.getArea() +", "+ restaurant.getCity() );
                tv_offerDetails.setText(Html.fromHtml(getOfferDetails()) );
                tv_address.setText( restaurant.getAddress() );

                double dist = Math.round( restaurant.getDistanceFromUser() * 100.0 ) / 100.0;
                tv_distance.setText( dist +" kms from you" );


            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    public String getOfferDetails(){

       String details = "";

        if( restaurant != null ) {

            if ( notEmpty( restaurant.getOfferPercentage() ) )
                details += "<b>Offer : </b>" + restaurant.getOfferPercentage();

            if ( notEmpty( restaurant.getSpecialOffer() ) )
                details += "<br><b>Special Offer : </b>" + restaurant.getSpecialOffer();

            if ( restaurant.getCashbackPercentage() != 0  )
                details += "<br><b>Cashback : </b>" + restaurant.getCashbackPercentage() +"%";

        }

        return details;
    }

    private boolean notEmpty(String str){
        if( str!=null && !str.isEmpty() ){
            return true;
        }else{
            return false;
        }
    }


}
