package es.luisma.epidemycontroll.Controller;

import org.json.JSONArray;

import es.luisma.epidemycontroll.Model.DAO.UsersIntegration;
import es.luisma.epidemycontroll.Model.DAO.alertsIntegration;
import es.luisma.epidemycontroll.Model.Dominio.Usuario;

public class HomeController {

    private UsersIntegration model;

    private alertsIntegration modelState;

    public HomeController(){model = new UsersIntegration();
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
    public JSONArray getAlerts(Double lat,Double lon){
        try {
            return modelState.getAlerts(lat,lon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
