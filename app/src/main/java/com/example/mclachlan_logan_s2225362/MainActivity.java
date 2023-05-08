// Name                 Logan Mclachlan
// Student ID           s2225362
// Programme of Study   Computing

package com.example.mclachlan_logan_s2225362;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.text.ParseException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    // initializes ui components
    private TextView extraInfo;;
    private EditText earthquakeSearch;
    private EditText dateSearch;
    private Button searchDateButton;
    private Button searchEarthquakeButton;
    private Button resetList;

    private ArrayList<Earthquake> dataList;
    private String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";

    // runs when app is created
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // creates and sets view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the raw links to the graphical components
        extraInfo = (TextView)findViewById(R.id.extraInfo);
        earthquakeSearch = (EditText) findViewById(R.id.earthquakeSearch);
        dateSearch = (EditText) findViewById(R.id.dateSearch);
        searchDateButton = (Button)findViewById(R.id.searchDateButton);
        searchDateButton.setOnClickListener(view -> searchDate());
        searchEarthquakeButton = (Button)findViewById(R.id.searchEarthquakeButton);
        searchEarthquakeButton.setOnClickListener(view -> searchEarthquakes());
        resetList = (Button)findViewById(R.id.resetList);
        resetList.setOnClickListener(view -> listData(dataList));

        // gets initial earthquake data
        startProgress();
    }


    public void startProgress()
    {
        // Run network access on a separate thread
        new Thread(new getData(urlSource)).start();
    }

    private class getData implements Runnable
    {
        // gets the url from startProgress
        private String url;
        public getData(String aurl)
        {
            url = aurl;
        }

        // gets earthquake data
        @Override
        public void run()
        {
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";
            String result = "";
            int count = 0;

            try
            {
                // sets up connection to the earthquake api
                aurl = new URL(url);
                yc = aurl.openConnection();
                // gets raw data from api
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                // loops through data, ignoring out lines 1 to 12
                while ((inputLine = in.readLine()) != null)
                {
                    if (count > 12) {
                        // removes namespaces
                        inputLine = inputLine.replace("geo:", "");
                        result += inputLine;
                    }
                    count++;
                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception");
            }

            // parses raw data into an array list
            dataList = ParseData.parseXML(result);

            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    // displays all earthquake data
                    listData(dataList);
                }
            }
            );
        }
    }

    // method to find a specific earthquake
    public void searchEarthquakes(){
        String input = earthquakeSearch.getText().toString();
        ArrayList<Earthquake> target = new ArrayList<Earthquake>();

        // finds earthquake with matching title
        for (Earthquake e : dataList){
            String eTitle = e.getTitle().substring(6);
            if (eTitle == input){
                target.add(e);
                break;
            }
        }

        if (target.size() > 0){
            listData(target);
        }
        else{
            // displays error message
            Toast.makeText(getApplicationContext(), "Earthquake " + input + " not found", Toast.LENGTH_SHORT).show();
        }
    }

    // method to find earthquakes on a specific date
    public void searchDate(){
        try {
            // gets dates and parses into data object
            Date searchDate = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
                    .parse(dateSearch.getText().toString());

            ArrayList<Earthquake> newList = new ArrayList<Earthquake>();

            // finds what earthquake dates match input
            for (Earthquake e: dataList)
            {
                Date eDate = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
                        .parse(e.getDate(), new ParsePosition(4));
                System.out.print(eDate);

                // compares dates
                // still to do
                if (searchDate.before(eDate)){
                    newList.add(e);
                }
            }

            if (newList.size() > 0){
                listData(newList);
            }
            else{
                // displays error message
                Toast.makeText(getApplicationContext(), "No earthquake were found on " + searchDate.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        catch (ParseException e) {
            System.out.println(e);

            // display error message on ui
        }


    }

    // method that determines earthquake colour based on its magnitude
    public int getMagColour(double Magnitude){
        switch((int)Magnitude) {
            case 0:
                return R.color.LightBlue;
            case 1:
                return R.color.DodgerBlue;
            case 2:
                return R.color.lime;
            case 3:
                return R.color.green;
            case 4:
                return R.color.GreenYellow;
            case 5:
                return R.color.yellow;
            case 6:
                return R.color.Orange;
            case 7:
                return R.color.OrangeRed;
            case 8:
                return R.color.red;
            case 9:
                return R.color.Crimson;
            case 10:
                return R.color.Brown;
        }
        // returns the colour white if no cases are met
        return 0;
    }

    // method to display data in on a table
    public void listData(ArrayList<Earthquake> dataList){
        // initializes table parts
        TableLayout table = (TableLayout)findViewById(R.id.earthquakeData);
        table.removeAllViewsInLayout();
        TableRow row;
        TextView title;
        Button expand;

        for (Earthquake e: dataList)
        {
            // creates row and elements
            row = new TableRow(getApplicationContext());
            title = new TextView(getApplicationContext());
            expand = new Button(getApplicationContext());

            // sets text styling
            title.setText(e.getTitle());
            title.setTextSize(21);
            title.setPadding(15,20,0,20);
            title.setTypeface(null, Typeface.BOLD);
            title.setMaxWidth(1000);
            title.setBackgroundResource(getMagColour(e.getMagnitude()));

            // sets button logic
            expand.setOnClickListener(view ->
            {
                String description = e.getTitle() + "\n" +
                        "Date: " + e.getDate() + "\n" +
                        "Latitude: " + e.getLatitude() + ", Longitude: " + e.getLongitude() + "\n" +
                        "Depth: " + e.getDepth() + " km";

                extraInfo.setText(description);
            });

            // adds table row
            row.addView(title);
            row.addView(expand);
            table.addView(row);
        }
    }
}