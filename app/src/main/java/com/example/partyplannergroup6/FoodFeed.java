package com.example.partyplannergroup6;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.sax.Element;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import android.net.Uri;




public class FoodFeed extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView food_list = null;
    private EditText feed_title = null;
    final String FILENAME = "food_feed.xml";

    RSSFeed feed = null;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_feed);

        feed_title = (EditText)findViewById(R.id.titleTextViews);
        food_list = (ListView) findViewById(R.id.itemsListView);
        new DownloadFeed().execute(new String[]{"https://www.bonappetit.com/feed/rss"});

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    class DownloadFeed extends AsyncTask<String, Void, Void> {
        // FUNCTION    : doInBackground
        // DESCRIPTION : Opens the URL and rights it's content to the file.
        // PARAMETERS  : String... params
        // RETURNS     : none
        @Override
        protected Void doInBackground(String... params) {

            try{

                // get the URL
                URL url = new URL(params[0]);

                // get the input stream
                InputStream in = url.openStream();

                // get the output stream
                Context context = FoodFeed.this;
                FileOutputStream out =
                        context.openFileOutput(FILENAME, Context.MODE_PRIVATE);

                // read input and write output
                byte[] buffer = new byte[1024];
                int bytesRead = in.read(buffer);
                StringBuffer sb = new StringBuffer();
                while (bytesRead != -1)
                {
                    sb.append(new String(buffer, "UTF-8"));
                    out.write(buffer, 0, bytesRead);
                    bytesRead = in.read(buffer);
                }
                Log.i("MyApp", sb.toString());
                out.close();
                in.close();
            }
            catch (IOException e) {
                Log.e("News reader", e.toString());
            }
            return null;
        }
        // FUNCTION    : doInBackground
        // DESCRIPTION : Rights the operation's description to the log file and executes the ReadFeed.
        // PARAMETERS  : Void result
        // RETURNS     : none

        @Override
        protected void onPostExecute(Void result) {
            Log.d("News reader", "Feed downloaded: " + new Date());
            new ReadFeed().execute();
        }
    }

    class ReadFeed extends AsyncTask<Void, Void, RSSFeed> {

        // FUNCTION    : doInBackground
        // DESCRIPTION : Gets the xml reader, reads the file, parses it's data and assgins the result to the feed.
        // PARAMETERS  : Void... params
        // RETURNS     : RSSFeed
        @Override
        protected RSSFeed doInBackground(Void... params) {
            try {

                // get the XML reader
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                XMLReader xmlreader = parser.getXMLReader();

                // set content handler
                RSSHandler theRssHandler = new RSSHandler();
                xmlreader.setContentHandler(theRssHandler);

                // read the file from internal storage
                FileInputStream in = openFileInput(FILENAME);

                // parse the data
                InputSource is = new InputSource(in);
                xmlreader.parse(is);

                // return the feed
                RSSFeed feed = theRssHandler.getFeed();
                return feed;
            }
            catch (Exception e) {
                Log.e("News reader", e.toString());
                return null;
            }
        }

        // This is executed after the feed has been read
        @Override
        protected void onPostExecute(RSSFeed result) {
            Log.d("News reader", "Feed read: " + new Date());

            // update the display for the activity
            FoodFeed.this.feed = result;
            FoodFeed.this.updateDisplay();
        }
    }

    // FUNCTION    : updateDisplay
    // DESCRIPTION : Sets the adapter to the list and appends the titles to the list.
    // PARAMETERS  : Void
    // RETURNS     : none

    public void updateDisplay(){
        if (feed == null) {
            feed_title.setText("Unable to get RSS feed");
            return;
        }

        // set the title for the feed
        feed_title.setText("Food Feed");
        String c = feed_title.getText().toString();

        // get the items for the feed
        ArrayList<RSSItem> items = feed.getAllItems();

        // create a List of Map<String, ?> objects
        ArrayList<HashMap<String, Object>> data =
                new ArrayList<HashMap<String, Object>>();
        for (RSSItem item : items) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("title", item.getTitle());
            data.add(map);
        }

        // create the resource, from, and to variables
        int resource = R.layout.liist_container;
        String[] from = {"title"};
        int[] to = {R.id.titleTextViews};

        // create and set the adapter
        SimpleAdapter adapter = new SimpleAdapter(this, data, resource, from, to);

        food_list.setOnItemClickListener(this);
        food_list.setAdapter(adapter);
        Log.d("News reader", "Feed displayed: " + new Date());
    }

    // FUNCTION    : onItemClick
    // DESCRIPTION : Once it is clicked, it will go the user to the link's web-page.
    // PARAMETERS  : AdapterView<?> parent, View v, int position, long id
    // RETURNS     : none
    public void onItemClick(AdapterView<?> parent, View v,
                            int position, long id) {

        RSSItem item = feed.getItem(position);

        // create an intent
        Intent intent = new Intent();
        String link = item.getLink();
        Uri viewUri = Uri.parse(link);

        // create the intent and start it
        Intent viewIntent = new Intent(Intent.ACTION_VIEW, viewUri);
        startActivity(viewIntent);
        this.startActivity(viewIntent);

    }
}
