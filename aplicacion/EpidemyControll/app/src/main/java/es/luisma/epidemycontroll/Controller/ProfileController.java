package es.luisma.epidemycontroll.Controller;

import java.util.Date;

import es.luisma.epidemycontroll.Model.DAO.UsersIntegration;
import es.luisma.epidemycontroll.Model.Dominio.Usuario;

public class ProfileController {
    private UsersIntegration model;
    public ProfileController(){model = new UsersIntegration();}

    public Usuario getData(String user){

        try {
            Usuario u = model.login(user);
            return u;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int savePass(String user, String newPass){
        Usuario a = new Usuario(user,"",newPass,new Date());
        try {
            int i = model.update(a);
            return i;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return -2;
    }

    public int deleteUser(String user){
        try {
            int i = model.delete(user);
            return i;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return -2;
    }
}
