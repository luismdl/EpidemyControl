package es.luisma.epidemycontroll.Controller;


import java.io.IOException;

import es.luisma.epidemycontroll.Model.DAO.UsersIntegration;
import es.luisma.epidemycontroll.Model.Dominio.Usuario;

public class LoginController {

    private UsersIntegration model;

    public LoginController(){
        model = new UsersIntegration();
    }

    public int login(String user, String pass){

        try {
            Usuario u = model.login(user);
            System.out.print(u);
            if(u.getPassword().equals(pass)){
                if(u.isAdmin()==1){
                    return 1;
                }
                return 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
