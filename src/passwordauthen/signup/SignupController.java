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
import passwordauthen.AESCrypto;
import passwordauthen.Controller;
import passwordauthen.Encryption;
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

    static String temp_name;
    static String temp_username;
    static String temp_pass;
    static String temp_phone;

    public void signup(ActionEvent e) throws IOException {
        temp_name = signup_name.getText();
        temp_username = signup_username.getText();
        temp_pass = signup_password.getText();
        temp_phone = signup_phone.getText();

        if ((temp_username.isEmpty()) || (temp_pass.isEmpty()) || (temp_name.isEmpty()) || (temp_phone.isEmpty())) {
            Alert nullException = new Alert(Alert.AlertType.ERROR);
            nullException.setTitle("Please enter valid values");
            nullException.setHeaderText("Empty username/password error");
            nullException.setContentText("Username/password cannot be empty");
            if (nullException.showAndWait().get() == ButtonType.OK) {
            }
        } else {
            if (temp_phone.length() != 10) {
                Alert nullException = new Alert(Alert.AlertType.ERROR);
                nullException.setTitle("Please enter valid phone number");
                nullException.setHeaderText("The phone number entered here has " + temp_phone.length() + " digits");
                nullException.setContentText("Phone numbers should have 10 digits");
                if (nullException.showAndWait().get() == ButtonType.OK) {
                }
            }
            if(!temp_phone.matches("[0-9]+") && temp_phone.length() == 10)
            {
                Alert nullException = new Alert(Alert.AlertType.ERROR);
                nullException.setTitle("Please enter valid phone number");
                nullException.setHeaderText("The phone number can contain only numbers");
                nullException.setContentText("Phone numbers should have 10 digits");
                if (nullException.showAndWait().get() == ButtonType.OK) {
                }
            }
            else if(temp_phone.matches("[0-9]+") && temp_phone.length() == 10) {
                Alert nullException = new Alert(Alert.AlertType.INFORMATION);
                nullException.setTitle("Proceed to create private key");
                nullException.setHeaderText("Click OK to go to next screen");
                if (nullException.showAndWait().get() == ButtonType.OK) {
                    cryptokey(e);
                }
//                signup_proceed(temp_name, temp_username, temp_pass, temp_phone, e);
            }
        }
    }

    @FXML
    public void signup_proceed(String temp_name, String temp_username, String temp_password, String temp_phone, ActionEvent e) {
        ManageSql obj = new ManageSql();
        int check = obj.check(temp_username);
        if (check == 1) {
            Alert nullException = new Alert(Alert.AlertType.ERROR);
            nullException.setTitle("Account already exists");
            nullException.setHeaderText("Please login with the same");
            if (nullException.showAndWait().get() == ButtonType.OK) {
            }
        } else {
            String sql = "CREATE TABLE " + temp_username + " (id int NOT NULL AUTO_INCREMENT, username varchar(70) NOT NULL, password varchar(255) NOT NULL,service varchar(255) NOT NULL,last_access_time varchar(255) DEFAULT NULL,phone varchar(255) NOT NULL,name varchar(255) DEFAULT NULL,last_login varchar(255) DEFAULT NULL,PRIMARY KEY (id))";
            Connection con = obj.connectToDb();
            Date date = new Date();
            String encoded_phone = Encryption.getEncoded(temp_phone);
            String encoded_name = Encryption.getEncoded(temp_name);
            String encoded_date = Encryption.getEncoded(date.toString());
            String masterloginupdate = "INSERT INTO " + temp_username + " (username,password,service,phone,name,last_login) VALUES(md5('" + temp_username + "'),md5('" + temp_password + "'),md5('MASTER'),'" + encoded_phone + "','" + encoded_name + "','" + encoded_date + "')";
            obj.execute(sql);
            obj.execute(masterloginupdate);
            try {
                goback(e);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }



    @FXML
    public void goback(ActionEvent e) throws IOException {
        Controller obj = new Controller();
        obj.goback(e);
    }

    @FXML
    TextField userinput_key;

    public void cryptokey(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Cryptokey.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.show();
    }

    public void userinput(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Userinputkey.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.show();
    }

    @FXML
    TextField privatekey_input;

    public void proceed_userinput(ActionEvent e) throws IOException {
        String private_key = privatekey_input.getText();
        if (private_key.length() != 16) {
            Alert length_error = new Alert(Alert.AlertType.ERROR);
            length_error.setTitle("Key needs to have 16 characters");
            length_error.setHeaderText("You have either entered lesser or more than 16 characters");
            if (length_error.showAndWait().get() == ButtonType.OK) {

            }
        } else {
            AESCrypto.userkey = private_key;
            Alert success = new Alert(Alert.AlertType.CONFIRMATION);
            success.setTitle("Data cannot be recovered if the wrong key is entered");
            success.setHeaderText("Write this key somewhere :" + private_key);
            success.setContentText("Click OK if you have noted it down");
            if (success.showAndWait().get() == ButtonType.OK) {
                Alert login_done = new Alert(Alert.AlertType.INFORMATION);
                login_done.setTitle("Account has been created");
                login_done.setHeaderText("Login with the same details");
                if (login_done.showAndWait().get() == ButtonType.OK) {
                    signup_proceed(temp_name,temp_username,temp_pass,temp_phone,e);
                }
            }
        }
    }

    public void randomkey(ActionEvent e)throws IOException
    {
        System.out.println(temp_name);
        System.out.println(temp_username);
        System.out.println(temp_pass);
        System.out.println(temp_phone);
        String random_key = AESCrypto.generaterandomkey(16);
        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setTitle("Data cannot be recovered if the wrong key is entered");
        success.setHeaderText("Write this key somewhere :" + random_key);
        success.setContentText("Click OK if you have noted it down");
        if (success.showAndWait().get() == ButtonType.OK) {
            AESCrypto.userkey = random_key;
            Alert login_done = new Alert(Alert.AlertType.INFORMATION);
            login_done.setTitle("Account has been created");
            login_done.setHeaderText("Login with the same details");
            if (login_done.showAndWait().get() == ButtonType.OK) {
                signup_proceed(temp_name,temp_username,temp_pass,temp_phone,e);
            }
        }
    }

}

