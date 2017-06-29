package task.fizz.in.fizztask;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import task.fizz.in.fizztask.models.Restaurant;

import static task.fizz.in.fizztask.Commons.notEmpty;

//Launched when a restaurant is clicked
public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();
    private String title = "Details";
    private Restaurant restaurant;

    private ImageView iv_bgImg;
    private ImageView iv_logo;
    private TextView tv_name;
    private TextView tv_area;
    private TextView tv_time;
    private TextView tv_offerDetails;
    private TextView tv_address;
    private TextView tv_distance;
    private TextView tv_otherDetails;
    private AppCompatButton btn_getDirections;
    private FloatingActionButton fab_call;
    MenuItem menuCall, menuMap; //for changing visibility


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initViews(); //initialize UI

        try {
            //get data from calling activity
            restaurant = (Restaurant) getIntent().getSerializableExtra("data");
            loadData(); //load the data into UI
        } catch (Exception e) {
            e.printStackTrace();
        }

        setupToolbar();

    }


    //collapsible toolbar which also shows/hides menu
    private void setupToolbar() {
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
                    showMenu();
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    hideMenu();
                    isShow = false;
                }
            }
        });
    }


    private void initViews() {

        iv_bgImg = (ImageView) findViewById(R.id.iv_bgImg);
        iv_logo = (ImageView) findViewById(R.id.iv_logo);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_area = (TextView) findViewById(R.id.tv_area);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_offerDetails = (TextView) findViewById(R.id.tv_offerDetails);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_distance = (TextView) findViewById(R.id.tv_distance);
        tv_otherDetails = (TextView) findViewById(R.id.tv_otherDetails);

        btn_getDirections = (AppCompatButton) findViewById(R.id.btn_directions);
        btn_getDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMaps();
            }
        });

        fab_call = (FloatingActionButton) findViewById(R.id.fab_call);
        fab_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialNumber();
            }
        });

    }

    private void dialNumber() {
        if (Commons.notEmpty(restaurant.getContactNumber())) {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + restaurant.getContactNumber()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void openMaps() {
        try {
            String latitude = String.valueOf(restaurant.getLatitude());
            String longitude = String.valueOf(restaurant.getLongitude());
            Log.i(TAG, "latitude=" + latitude + "longitude=" + longitude);

            String geoUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude + " (" + restaurant.getName() + ")";

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadData() {

        if (restaurant != null) {

            try {

                Glide
                        .with(this)
                        .load(restaurant.getHeroImage())
                        .centerCrop()
                        .into(iv_bgImg);

                Glide
                        .with(this)
                        .load(restaurant.getLogo())
                        .into(iv_logo);

                tv_name.setText(restaurant.getName());
                tv_area.setText(restaurant.getArea() + ", " + restaurant.getCity());
                tv_offerDetails.setText(fromHtml(getOfferDetails()));
                tv_otherDetails.setText(fromHtml(getOtherDetails()));
                tv_address.setText(restaurant.getAddress());

                double distance = Math.round(restaurant.getDistanceFromUser() * 100.0) / 100.0; //rounding the distance to 2 decimal points
                tv_distance.setText(distance + " kms from you");
                tv_time.setText(getTimings());

                title = restaurant.getName();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    //parses the timings
    private String getTimings() {

        String timings = "";

        try {

            List<String> stringList = restaurant.getTimings().get(0);
            if (stringList != null && !stringList.isEmpty() && stringList.size() >= 2) {
                timings = stringList.get(0) + "-" + stringList.get(1);
                if (stringList.size() >= 4)
                    timings += ", " + stringList.get(2) + "-" + stringList.get(3);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return timings;
    }

    //combines all offer fields
    private String getOfferDetails() {

        String details = "";

        if (restaurant != null) {

            if (notEmpty(restaurant.getOfferPercentage()))
                details += "<b>Offer : </b>" + restaurant.getOfferPercentage();

            if (notEmpty(restaurant.getSpecialOffer()))
                details += "<br><b>Special Offer : </b>" + restaurant.getSpecialOffer();

            if (restaurant.getCashbackPercentage() != 0)
                details += "<br><b>Cashback : </b>" + restaurant.getCashbackPercentage() + "%";

        }

        return details;
    }

    //combines all other fields
    private String getOtherDetails() {

        String details = "";

        if (restaurant != null) {

            details += "<b>Active : </b>" + (restaurant.isActive() ? "Yes" : "No");
            details += "<br><b>Discounted : </b>" + (restaurant.isDiscounted() ? "Yes" : "No");
            details += "<br><b>Favorite : </b>" + (restaurant.isFavorite() ? "Yes" : "No");
            details += "<br><b>Loyalty awarded : </b>" + (restaurant.isAwardedLoyalty() ? "Yes" : "No");

        }

        return details;
    }


    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu)
    {
        menuCall = menu.findItem(R.id.action_call);
        menuMap = menu.findItem(R.id.action_map);
        return true;
    }

    private void showMenu(){

        if( menuCall!=null ){
            menuCall.setVisible(true);
        }

        if( menuMap!=null ){
            menuMap.setVisible(true);
        }

    }

    private void hideMenu(){

        if( menuCall!=null ){
            menuCall.setVisible(false);
        }

        if( menuMap!=null ){
            menuMap.setVisible(false);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.action_call:
                dialNumber();
                return true;

            case R.id.action_map:
                openMaps();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }


}
