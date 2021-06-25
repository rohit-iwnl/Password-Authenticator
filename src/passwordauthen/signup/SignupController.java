package passwordauthen.signup;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import passwordauthen.Controller;
import passwordauthen.ManageSql;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.sql.Connection;
import java.util.Date;

public class SignupController {
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    TextField signup_name;
    @FXML
    TextField signup_username;
    @FXML
    TextField signup_password;
    @FXML
    TextField signup_phone;

    @FXML
    Button signup_button;

    @FXML
    private AnchorPane ultimate;

    public void signup(ActionEvent e)throws IOException
    {
        String temp_name = signup_name.getText();
        String temp_username = signup_username.getText();
        String temp_pass = signup_password.getText();
        String temp_phone = signup_phone.getText();

        if ((temp_username.isEmpty()) || (temp_pass.isEmpty()) || (temp_name.isEmpty()) || (temp_phone.isEmpty())) {
            Alert nullException = new Alert(Alert.AlertType.ERROR);
            nullException.setTitle("Please enter valid values");
            nullException.setHeaderText("Empty username/password error");
            nullException.setContentText("Username/password cannot be empty");
            if(nullException.showAndWait().get() == ButtonType.OK) {
            }
        }
        else
        {
            if(temp_phone.length() != 10)
            {
                Alert nullException = new Alert(Alert.AlertType.ERROR);
                nullException.setTitle("Please enter valid phone number");
                nullException.setHeaderText("The phone number entered here has "+temp_phone.length()+" digits");
                nullException.setContentText("Phone numbers should have 10 digits");
                if(nullException.showAndWait().get() == ButtonType.OK) {
                }
            }
            else {
                signup_proceed(temp_name, temp_username, temp_pass, temp_phone, e);
            }
        }
    }

    @FXML
    public void signup_proceed(String temp_name,String temp_username,String temp_password,String temp_phone,ActionEvent e) {
        ManageSql obj = new ManageSql();
        int check = obj.check(temp_username);
        if (check == 1) {
            Alert nullException = new Alert(Alert.AlertType.ERROR);
            nullException.setTitle("Account already exists");
            nullException.setHeaderText("Please login with the same");
            if (nullException.showAndWait().get() == ButtonType.OK) {
            }
        } else {
            String sql = "CREATE TABLE " + temp_username + " (id int NOT NULL AUTO_INCREMENT, username varchar(50) NOT NULL, password varchar(50) NOT NULL,service varchar(50) NOT NULL,last_access_time varchar(50) DEFAULT NULL,phone varchar(11) NOT NULL,name varchar(30) DEFAULT NULL,last_login varchar(50) DEFAULT NULL,PRIMARY KEY (id))";
            Connection con = obj.connectToDb();
            Date date=new Date();
            String masterloginupdate = "INSERT INTO " + temp_username + " (username,password,service,phone,name,last_login) VALUES('" + temp_username + "','" + temp_password + "','MASTER','" + temp_phone + "','"+temp_name+"','"+date+"')";
            obj.execute(sql);
            obj.execute(masterloginupdate);
            System.out.println("Account has been created.Login with the same");
            System.out.println("EXITING NOW...");
            Alert nullException = new Alert(Alert.AlertType.INFORMATION);
            nullException.setTitle("Account has been created");
            nullException.setHeaderText("Please login with the same");
            if (nullException.showAndWait().get() == ButtonType.OK) {
                try {
                    goback(e);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    @FXML
    public void goback(ActionEvent e)throws IOException
    {
        Controller obj = new Controller();
        obj.goback(e);
    }

}
