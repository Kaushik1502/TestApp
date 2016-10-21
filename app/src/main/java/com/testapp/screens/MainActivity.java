package com.testapp.screens;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.testapp.R;
import com.testapp.com.adapters.HorizontalListAdapter;
import com.testapp.com.adapters.SpinnerAdapter;
import com.testapp.com.adapters.VerticalListAdapter;
import com.testapp.model.Pojo;
import com.testapp.sqlite.MyDataBase;
import com.testapp.utils.CallbackResponse;
import com.testapp.utils.GPSTracker;
import com.testapp.utils.Utils;
import com.testapp.webservices.MainAsync;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CallbackResponse, VerticalListAdapter.MyClickListener {
    Toolbar toolbar;
    EditText editText;
    ImageView search_img;
    TextView categories_pickedTv;
    Spinner spinnerCities;
    RelativeLayout layout_editText;
    RecyclerView horizontalRecyclerView, verticalRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    HorizontalListAdapter horizontalListAdapter;
    VerticalListAdapter verticalListAdapter;
    MyDataBase myDataBase;
    GPSTracker gpsTracker;
    ArrayList<Pojo> catList;
    CoordinatorLayout coordinatelayout;
    private NetworkChangeReceiver receiver;
    private int[] cities = new int[]{R.string.delhi, R.string.noida, R.string.gurgaon, R.string.mumbai, R.string.chennai,
            R.string.bangalore, R.string.kolkata};
    private int[] names = new int[]{R.string.google, R.string.drive, R.string.facebook, R.string.google_plus,
            R.string.instagram, R.string.linkedin, R.string.skype, R.string.tumblr, R.string.twitter, R.string.vimeo,
            R.string.youtube};
    private int[] images = new int[]{R.drawable.google, R.drawable.drive, R.drawable.facebook, R.drawable.google_plus,
            R.drawable.instagram, R.drawable.linkedin, R.drawable.skype, R.drawable.tumblr, R.drawable.twitter, R.drawable.vimeo,
            R.drawable.youtube};

    @Override
    public void onItemClick(int position, View v) {
        // Toast.makeText(MainActivity.this, " Pos " + position, Toast.LENGTH_SHORT).show();
        Snackbar snackbar = Snackbar
                .make(coordinatelayout, "" + catList.get(position).getName(), Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Database and GPS Tracker
        myDataBase = new MyDataBase(this);
        gpsTracker = new GPSTracker(this);

        //hiding keypad by default
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //Ids of all widgets
        toolbar = (Toolbar) findViewById(R.id.technique_two_toolbar);
        editText = (EditText) findViewById(R.id.editText);
        categories_pickedTv = (TextView) findViewById(R.id.categories_pickedTv);
        layout_editText = (RelativeLayout) findViewById(R.id.layout_editText);
        spinnerCities = (Spinner) findViewById(R.id.spinnerCities);
        horizontalRecyclerView = (RecyclerView) findViewById(R.id.horizontalRecyclerView);
        verticalRecyclerView = (RecyclerView) findViewById(R.id.verticalRecyclerView);
        search_img = (ImageView) findViewById(R.id.search_img);
        coordinatelayout = (CoordinatorLayout) findViewById(R.id.coordinatelayout);

        //Adapter of Spinner
        SpinnerAdapter spinnerArrayAdapter = new SpinnerAdapter(this, cities); //selected item will look like a spinner set from XML
        spinnerCities.setAdapter(spinnerArrayAdapter);
        spinnerCities.setSelection(0);
        //HorizontalListAdapter
        horizontalListAdapter = new HorizontalListAdapter(this, names, images);
        horizontalRecyclerView.setAdapter(horizontalListAdapter);
        String sourceString = "<b>" + "Categories " + "</b>" + "handpicked" + "<b>" + " for you" + "</b> ";
        categories_pickedTv.setText(Html.fromHtml(sourceString));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        //TextWatcher of ediText
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    if (s.length() > 0 && s.subSequence(0, 1).toString().equalsIgnoreCase(" ")) {
                        editText.setText("");
                        search_img.setVisibility(View.VISIBLE);
                    } else search_img.setVisibility(View.GONE);
                } else
                    search_img.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //Vertical RecyclerView
        verticalRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        verticalRecyclerView.setLayoutManager(mLayoutManager);
        if (Utils.isNetworkAvailable(this)) {
            if (gpsTracker.canGetLocation()) {
                MainAsync mainAsync = new MainAsync(this);
                mainAsync.setListener(this);
                mainAsync.execute();
            } else {
                Snackbar snackbar = Snackbar
                        .make(coordinatelayout, "You will not get distance as location is turned off.", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        } else {
            catList = new ArrayList<>();
            catList = myDataBase.getallData();
            if (catList.size() > 0) {
                verticalListAdapter = new VerticalListAdapter(MainActivity.this, catList);
                verticalRecyclerView.setAdapter(verticalListAdapter);
            } else {
                Snackbar snackbar = Snackbar
                        .make(coordinatelayout, "There is no data.", Snackbar.LENGTH_SHORT);
                snackbar.show();
                IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                receiver = new NetworkChangeReceiver();
                registerReceiver(receiver, filter);
            }
        }
    }

    @Override
    public void CatList(ArrayList<Pojo> pojoArrayList) {
        catList = (ArrayList<Pojo>) pojoArrayList.clone();
        verticalListAdapter = new VerticalListAdapter(MainActivity.this, pojoArrayList);
        verticalRecyclerView.setAdapter(verticalListAdapter);
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {

            if (gpsTracker.canGetLocation()) {
                MainAsync mainAsync = new MainAsync(MainActivity.this);
                mainAsync.setListener(MainActivity.this);
                mainAsync.execute();
            } else {
                Snackbar snackbar = Snackbar
                        .make(coordinatelayout, "You will not get distance as location is turned off.", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }


        }
    }

}
