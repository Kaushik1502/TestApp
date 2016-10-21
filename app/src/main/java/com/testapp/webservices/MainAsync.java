package com.testapp.webservices;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.testapp.model.Pojo;
import com.testapp.sqlite.MyDataBase;
import com.testapp.utils.CallbackResponse;
import com.testapp.utils.GPSTracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by cws on 19/10/16.
 */
public class MainAsync extends AsyncTask<Void, Void, String> {
    CallbackResponse callbackResponse;
    MyDataBase myDataBase;
    Context con;
    GPSTracker gpsTracker;

    public MainAsync(Context con) {
        this.con = con;
        this.myDataBase = new MyDataBase(con);
        this.gpsTracker = new GPSTracker(con);
    }

    @Override
    protected String doInBackground(Void... params) {
        return getString();
    }

    private String getString() {
        // TODO Auto-generated method stub
        URL obj = null;
        HttpURLConnection con = null;
        try {
            obj = new URL("http://ec2-54-169-238-70.ap-southeast-1.compute.amazonaws.com:5000/businesses");
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                Log.i("TAG", response.toString());
                return response.toString();


            } else {
                Log.i("TAG", "POST request did not work.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

        ArrayList<Pojo> catListDao;
        if (result != null) {
            JSONObject jsonObject;
            try {
                catListDao = new ArrayList<Pojo>();
                jsonObject = new JSONObject(result);
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                JSONArray jsonArray = jsonObject1.getJSONArray("businesses");

                if (jsonArray.length() > 0) {
                    if (myDataBase.numberOfRows() > 0)
                        myDataBase.deleteData();
                    for (int ii = 0; ii < jsonArray.length(); ii++) {
                        Pojo dataObject = new Pojo();
                        JSONObject jsonObject11 = jsonArray.getJSONObject(ii);
                        String name = jsonObject11.getString("name");
                        JSONObject jsonObject2 = jsonObject11.getJSONObject("address");
                        String locality = jsonObject2.getString("locality");
                        String image = jsonObject11.getString("profileImage");
                        String location = jsonObject11.getString("location");
                        String distance = "";
                        if (location != null || !location.equals("")) {
                            double lattt = Double.parseDouble(location.split("\\,")[0]);
                            double longt = Double.parseDouble(location.substring(location.lastIndexOf(",") + 1));
                            distance = calculateDistance(gpsTracker.getLatitude(), gpsTracker.getLongitude(), lattt, longt);
                        }
                        dataObject.setName(name);
                        dataObject.setAddress(locality);
                        dataObject.setImage(image);
                        dataObject.setLocation(distance);
                        catListDao.add(dataObject);
                        boolean issucces = myDataBase.addData(name, locality, distance, image);
                    }
                    callbackResponse.CatList(catListDao);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public String calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        Location selected_location = new Location("A");
        selected_location.setLatitude(lat1);
        selected_location.setLongitude(lon1);
        Location near_locations = new Location("B");
        near_locations.setLatitude(lat2);
        near_locations.setLongitude(lon2);
        double distance = selected_location.distanceTo(near_locations);
        double inKms = distance / 1000;
        DecimalFormat df = new DecimalFormat("#.##");
        inKms = Double.valueOf(df.format(inKms));
        return "" + inKms;
    }

    public void setListener(CallbackResponse callbackResponse) {
        this.callbackResponse = callbackResponse;
    }
}

