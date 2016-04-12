package androod.com.lazyloading;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        listView =(ListView) findViewById(R.id.lv);
        dataModelList= new ArrayList<DataModel>();
        refreshData();


        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
             refreshData();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    }

    private void updateListView(JSONArray responseArray)
    {
        try
        {
            dataModelList.clear();

            for (int i = 0; i < responseArray.length(); i++) {
                if(i!=0) {
                    JSONObject listjasson = (JSONObject) responseArray.get(i);
                    DataModel item = new DataModel(listjasson,this);
                    dataModelList.add(item);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        customAdapter = new CustomAdapter(this,dataModelList);
        listView.setAdapter(customAdapter);
        swipeContainer.setRefreshing(false);
    }
    
    public void refreshData(){

        asHclient.setURLEncodingEnabled(false);
        RequestParams params = new RequestParams();
        params.put("Permission", "Admin");

        asHclient.post(getResources().getString(R.string.url_domian) + "getAllData", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                // Initiated the request
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Successfully got a response
                try {
                    final String responseString = new String(responseBody, "UTF-8");
                    Log.d("Webservices", responseString);

                    //responce is in XML

                    String responseStr1 = getStringFromXml(responseString);
                    responseStr1 = responseStr1.replace("null,", "\"\",");
                    JSONArray responseJsonArray = new JSONArray(responseStr1);
                    JSONObject status_Json = responseJsonArray.getJSONObject(0);
                    String status = status_Json.getString("Status");
                    if (status.equalsIgnoreCase("true")) {

                        updateListView(responseJsonArray);

                    } else {
                        Toast.makeText(getApplicationContext(), "API return False", Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                    error) {

                Toast.makeText(getApplicationContext(), "Internet Issue.Try Again! ", Toast.LENGTH_LONG).show();
                error.printStackTrace(System.out);
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onRetry(int retryNo) {
                // Request was retried
            }

            @Override
            public void onFinish() {
                // Completed the request (either success or failure)
            }
        });

    }

    public String getStringFromXml(String xml){
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }
        // return DOM
        Node child=doc.getFirstChild();
        return child.getFirstChild().getNodeValue();

    }


    private AsyncHttpClient asHclient = new AsyncHttpClient();
    private ListView listView;
    private List<DataModel> dataModelList;
    private CustomAdapter customAdapter;
    private SwipeRefreshLayout swipeContainer;
}
