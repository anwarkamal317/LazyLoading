package androod.com.lazyloading;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by Ch on 4/12/2016.
 */
public class DataModel {

    public DataModel(JSONObject emailData, Context cont)
    {
        context = cont;
        try
        {
            Name = emailData.getString("Name");
            ImagePath = emailData.getString("ImagePath");

            justifyStrings();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public void justifyStrings()
    {

        //go to string and replace your base URL for url_domain
        ImagePath =  context.getResources().getString(R.string.url_domian) + ImagePath.replace("~","");
    }

    String Name,ImagePath;
    Context context;
}
