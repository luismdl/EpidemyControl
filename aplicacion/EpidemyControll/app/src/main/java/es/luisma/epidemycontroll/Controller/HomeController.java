package es.luisma.epidemycontroll.Controller;

import es.luisma.epidemycontroll.Model.DAO.UsersIntegration;
import es.luisma.epidemycontroll.Model.Dominio.Usuario;

public class HomeController {

    private UsersIntegration model;

    public HomeController(){model = new UsersIntegration();}

    public int getState(String user){

        try {
            Usuario u = model.login(user);
            return u.getSTATE();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
