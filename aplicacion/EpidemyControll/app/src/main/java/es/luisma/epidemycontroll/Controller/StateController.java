package es.luisma.epidemycontroll.Controller;

import android.util.Log;

import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.Field;


import java.text.DateFormat;
import java.util.concurrent.TimeUnit;

import static java.text.DateFormat.getTimeInstance;

public class StateController {

    private static final String TAG = "test";

    public StateController(){}


    public int updateData(DataSet dataSet,Double lat, Double lon, String state, String details){
        Log.i(TAG, lat.toString());
        Log.i(TAG, lon.toString());
        Log.i(TAG, state);
        Log.i(TAG, details);
        dumpDataSet(dataSet);
        return 1;
    }

    private static void dumpDataSet(DataSet dataSet) {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getTimeInstance();


        for (DataPoint dp : dataSet.getDataPoints()) {
            Log.i(TAG, "Data point:");
            Log.i(TAG, "\tType: " + dp.getDataType().getName());
            Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                Log.i(TAG, "\tField: " + field.getName() + " Value: " + dp.getValue(field));
            }
        }
    }
}
