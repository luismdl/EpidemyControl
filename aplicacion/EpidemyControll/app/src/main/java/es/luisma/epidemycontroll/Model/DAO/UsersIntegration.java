package es.luisma.epidemycontroll.Model.DAO;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import es.luisma.epidemycontroll.Model.Dominio.Usuario;

public class UsersIntegration {

    private String base = "http://tfmRest.eu-gb.mybluemix.net/";

    public UsersIntegration(){}

    public Usuario login(String userName) throws IOException, ExecutionException, InterruptedException {

        URL url = new URL(base +"usuario/"+userName);
        StringBuffer t = new GetHttp().execute(url).get();
        String s = t.toString();
        Gson g = new Gson();
        Usuario u = g.fromJson(s, Usuario.class);
        return u;
    }

    public int register(Usuario user) throws IOException, ExecutionException, InterruptedException {

        Integer t = new PostHttp().execute(user).get();
        if(t ==201){
            return 0;
        }
        return -1;
    }

    public int update(Usuario user) throws IOException, ExecutionException, InterruptedException {

        Integer t = new PutHttp().execute(user).get();
        if(t ==200){
            return 0;
        }
        return -1;
    }
    public int delete(String user) throws IOException, ExecutionException, InterruptedException {

        Integer t = new DeleteHttp().execute(user).get();
        if(t ==200){
            return 0;
        }
        return -1;
    }






    class GetHttp extends AsyncTask<URL, Void, StringBuffer> {

        @Override
        protected StringBuffer doInBackground(URL... urls) {
            HttpURLConnection con = null;
            StringBuffer content = null;
            try {
                con = (HttpURLConnection) urls[0].openConnection();
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

    class PostHttp extends AsyncTask<Usuario, Void, Integer> {

        @Override
        protected Integer doInBackground(Usuario... urls) {
            HttpURLConnection con = null;
            StringBuffer content = null;
            try {
                URL url = new URL(base +"usuario");
                con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty( "Content-Type", "application/json");
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                Gson g = new Gson();
                String user = g.toJson(urls[0], Usuario.class);
                JsonElement je = g.fromJson(user, JsonElement.class);
                JsonObject jo = je.getAsJsonObject();

                java.text.SimpleDateFormat sdf =
                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentTime = sdf.format(urls[0].getBirthDate());
                jo.addProperty("birthDate", currentTime);
                user = jo.toString();
                System.out.println(user);
                OutputStream os = con.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                os.write(user.getBytes());
                os.flush();
                os.close();

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



    class PutHttp extends AsyncTask<Usuario, Void, Integer> {

        @Override
        protected Integer doInBackground(Usuario... urls) {
            HttpURLConnection con = null;
            StringBuffer content = null;
            try {
                URL url = new URL(base +"usuario/"+ urls[0].getUserName());
                con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty( "Content-Type", "application/json");
                con.setRequestMethod("PUT");
                con.setDoOutput(true);

                Gson g = new Gson();
                String user = g.toJson(urls[0], Usuario.class);
                JsonElement je = g.fromJson(user, JsonElement.class);
                JsonObject jo = je.getAsJsonObject();

                java.text.SimpleDateFormat sdf =
                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentTime = sdf.format(urls[0].getBirthDate());
                jo.addProperty("birthDate", currentTime);
                user = jo.toString();
                System.out.println(user);
                OutputStream os = con.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                os.write(user.getBytes());
                os.flush();
                os.close();

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

    class DeleteHttp extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... urls) {
            HttpURLConnection con = null;
            StringBuffer content = null;
            try {
                URL url = new URL(base +"usuario/"+ urls[0]);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("DELETE");
                con.setDoOutput(true);

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




