package es.luisma.epidemycontroll.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import es.luisma.epidemycontroll.Model.DAO.UsersIntegration;
import es.luisma.epidemycontroll.Model.Dominio.Usuario;

public class RegisterController {

    private UsersIntegration model;

    public RegisterController(){
        model = new UsersIntegration();
    }

    public int register(String user, String pass,String email, String birth){

        try {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");
            Date date = format.parse(birth);
            Usuario usera = new Usuario(user,email,pass,date);
            int i = model.register(usera);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
