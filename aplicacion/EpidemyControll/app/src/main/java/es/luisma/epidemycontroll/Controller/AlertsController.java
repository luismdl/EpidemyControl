package es.luisma.epidemycontroll.Controller;

import org.json.JSONArray;
import org.json.JSONObject;

import es.luisma.epidemycontroll.Model.DAO.UsersIntegration;
import es.luisma.epidemycontroll.Model.DAO.alertsIntegration;
import es.luisma.epidemycontroll.Model.Dominio.Usuario;

public class AlertsController {

    private UsersIntegration model;

    private alertsIntegration modelState;

    public AlertsController(){model = new UsersIntegration();
        modelState = new alertsIntegration();}

    public int getState(String user){

        try {
            Usuario u = model.login(user);
            return u.getSTATE();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    public JSONArray getAlerts(){
        try {
            return modelState.getAlerts();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray getAlerts(String user){
        try {
            return modelState.getAlerts(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getStateId(String id){
        try {
            return modelState.getState(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
