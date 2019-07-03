package es.luisma.epidemycontroll.Controller;

import android.util.Log;

import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.Field;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import es.luisma.epidemycontroll.Model.DAO.UsersIntegration;
import es.luisma.epidemycontroll.Model.DAO.alertsIntegration;

import static java.text.DateFormat.getTimeInstance;

public class StateController {

    private static final String TAG = "test";

    private UsersIntegration modelUser ;
    private alertsIntegration modelState;

    public StateController(){
        modelUser = new UsersIntegration();
        modelState = new alertsIntegration();
    }


    public int updateData(String username, DataSet dataSet,Double lat, Double lon, String state, String details){
        int i = -1;
        try{
            int statei;
            if(state.equals("Bien")){
                statei=0;
            }else{
                statei=1;
            }
            i= modelUser.changeState(username,statei);
            JSONObject json = new JSONObject();
            json.put("username", username);
            java.text.SimpleDateFormat sdf =
                    new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(new Date());
            json.put("timestamp", new Date());
            json.put("state", statei);
            json.put("details", details);

            JSONObject location = new JSONObject();
            location.put("type","Point");
            JSONArray coords = new JSONArray();
            coords.put(lon);
            coords.put(lat);
            location.put("coordinates",coords);
            json.put("location",location);
            JSONArray heartBeats = new JSONArray();
            heartBeats =dumpDataSet(dataSet,heartBeats);
            json.put("heartbeat",heartBeats);
            Log.i(TAG, json.toString());
            modelState.save(json);

        }catch (Exception e){
            e.printStackTrace();
        }

        return i;
    }

    private JSONArray dumpDataSet(DataSet dataSet,JSONArray dataRes) throws JSONException {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getTimeInstance();


        for (DataPoint dp : dataSet.getDataPoints()) {

            JSONObject dataPoint = new JSONObject();
            dataPoint.put("timestamp",dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                dataPoint.put("value",dp.getValue(field));
            }
            dataRes.put(dataPoint);
        }
        return dataRes;
    }
}
