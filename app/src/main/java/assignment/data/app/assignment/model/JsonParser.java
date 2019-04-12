package assignment.data.app.assignment.model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import assignment.data.app.assignment.R;

public class JsonParser {
    private static final String TAG = "JsonParser";
    Context mContext;
    Handler mHandler;

    JsonParser(Context context, Handler handler) {
        mContext = context;
        mHandler = handler;
    }

    public void readJsonData() {
        Log.v(TAG, "reading json");
        String json = getStringFromJson();
        if (json != null && !json.isEmpty()) {
            Log.v(TAG, "Json found :" + json);
            try {
                JSONArray j_array = new JSONArray(json);
                JSONObject job;
                CardData cardData;
                for (int i = 0; i < j_array.length(); i++) {
                    job = j_array.getJSONObject(i);
                    cardData = parseJsonObject(job);
                    Message.obtain(mHandler, AppConstants.SEND_DATA, cardData).sendToTarget();
                    Log.v(TAG, "Sending data to UI: " + cardData.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.v(TAG, "Json found empty");
        }
    }

    /*
     * method parse the json data and return object*/
    private CardData parseJsonObject(JSONObject job) throws JSONException {
        String type = job.getString(AppConstants._TYPE);
        CardData cd = new CardData(type,
                job.getString(AppConstants._ID),
                job.getString(AppConstants._TTILE));
        ArrayList<String> data = new ArrayList<>();
        switch (type) {
            case AppConstants.IMAGE_DATA:
            case AppConstants.COMMENT:
                String dataMap = job.getString(AppConstants._DATAMAP);
                //Log.v(TAG,"dataMap = "+dataMap + " for type "+type);
                data.add(dataMap.equals("{}") ? "" : dataMap);
                break;
            case AppConstants.CHECK_DATA:
                JSONArray jarr = job.getJSONObject(AppConstants._DATAMAP).getJSONArray("options");
                for (int i = 0; i < jarr.length(); i++) {
                    data.add(jarr.getString(i));
                }
                break;
            //further cases can be added to parse different types
            default:
                Log.d(TAG, "Not handled case");
                break;
        }
        cd.setData(data);
        return cd;
    }

    private String getStringFromJson() {
        //AssetManager assetManager = mContext.getAssets();
        try {
            InputStream is = mContext.getResources().openRawResource(R.raw.input);
            byte[] data = new byte[is.available()];
            is.read(data);
            return new String(data);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
