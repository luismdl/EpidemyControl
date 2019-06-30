package es.luisma.epidemycontroll.Model.DAO;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
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
