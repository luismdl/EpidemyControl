package es.luisma.epidemycontroll.Model.DAO;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class changeStateIntegration {

    private String base = "http://10.0.2.2:3000/";

    public changeStateIntegration(){}

    public int save(JSONObject data) throws ExecutionException, InterruptedException {
        int t = new PostHttp().execute(data).get();
        if(t ==201){
            return 0;
        }
        return -1;

    }

    public JSONArray getAlerts(Double lat, Double lon) throws ExecutionException, InterruptedException, JSONException {
        ArrayList<Double> a = new ArrayList<>();
        a.add(lat);
        a.add(lon);
        StringBuffer t = new GetHttp().execute(a).get();
        String s = t.toString();
        JSONArray json = new JSONArray(s);
        return json;

    }


    class GetHttp extends AsyncTask<ArrayList<Double>, Void, StringBuffer> {

        @Override
        protected StringBuffer doInBackground(ArrayList<Double>... coords) {
            HttpURLConnection con = null;
            StringBuffer content = null;
            try {
                con = (HttpURLConnection) new URL(base +"alerts/"+coords[0].get(0)+"/"+coords[0].get(1)).openConnection();
                con.setRequestMethod("GET");

                int status = con.getResponseCode();

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                con.disconnect();
            }

            return content;
        }

        protected void onPostExecute(StringBuffer feed) {

        }
    }

    class PostHttp extends AsyncTask<JSONObject, Void, Integer> {

        @Override
        protected Integer doInBackground(JSONObject... urls) {
            HttpURLConnection con = null;
            StringBuffer content = null;
            try {
                URL url = new URL(base +"state");
                con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty( "Content-Type", "application/json");
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                //con.connect();
                OutputStream os = con.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os,"UTF-8");
                osw.write(urls[0].toString());
                osw.flush();

                int status = con.getResponseCode();
                return status;

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                con.disconnect();
            }

            return -1;
        }

        protected void onPostExecute(StringBuffer feed) {

        }
    }
}
