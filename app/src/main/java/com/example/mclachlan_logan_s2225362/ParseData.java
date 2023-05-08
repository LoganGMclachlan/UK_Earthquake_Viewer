// Name                 Logan Mclachlan
// Student ID           s2225362

package com.example.mclachlan_logan_s2225362;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;

// class that takes in xml data and converts it into a usable linked list
public class ParseData {

    // parses the xml data into a linked list
    public static ArrayList<Earthquake> parseXML(String xmlIn){

        ArrayList<Earthquake> dataList = new ArrayList<Earthquake>();

        // try statement to handle errors
        try
        {
            // sets up xml parser
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput( new StringReader( xmlIn ) );
            int eventType = xpp.getEventType();

            // creates an initial earthquake instance
            Earthquake earthquake = new Earthquake();

            // loops through xml data
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                // Found a start tag
                if(eventType == XmlPullParser.START_TAG)
                {
                    // Check if we have item tag
                    if (xpp.getName().equalsIgnoreCase("item"))
                    {
                        // creates a new earthquake instance
                        earthquake = new Earthquake();
                    }
                    else if (xpp.getName().equalsIgnoreCase("title"))
                    {
                        xpp.next();
                        // sets title
                        earthquake.setTitle(xpp.getText().substring(22,xpp.getText().lastIndexOf(",")-5));

                        // gets magnitude from description
                        earthquake.setMagnitude(Double.parseDouble(xpp.getText().substring(24, 27)));
                    }
                    else if (xpp.getName().equalsIgnoreCase("description"))
                    {
                        xpp.next();

                        // gets depth data
                        Integer depthIndex = xpp.getText().indexOf("Depth:");
                        earthquake.setDepth(Integer.parseInt(xpp.getText().substring(depthIndex+7,depthIndex+9).trim()));
                    }
                    else if (xpp.getName().equalsIgnoreCase("link"))
                    {
                        xpp.next();
                        // sets link
                        earthquake.setLink(xpp.getText());
                    }
                    else if (xpp.getName().equalsIgnoreCase("pubDate"))
                    {
                        xpp.next();
                        // sets date
                        earthquake.setDate(xpp.getText());
                    }
                    else if (xpp.getName().equalsIgnoreCase("lat"))
                    {
                        xpp.next();
                        // sets latitude
                        earthquake.setLatitude(Double.parseDouble(xpp.getText()));
                    }
                    else if (xpp.getName().equalsIgnoreCase("long"))
                    {
                        xpp.next();
                        // sets longitude
                        earthquake.setLongitude(Double.parseDouble(xpp.getText()));
                    }
                }
                else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")){
                    // If an item end tag if found, add earthquake to the list
                    dataList.add(earthquake);
                }

                // Get the next event
                eventType = xpp.next();

            } // End of while
        }
        catch (XmlPullParserException ae1)
        {
            // logs an error
            Log.e("MyTag","Parsing error: " + ae1.toString());
        }
        catch (IOException ae1)
        {
            // logs an error
            Log.e("MyTag","IO error during parsing");
        }

        // returns linked list of parsed earthquake data
        return dataList;
    }

}
