package passwordauthen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Controller{
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    TextField usrname;
    @FXML
    TextField passwd;
    @FXML
    ImageView displaylogin;
    @FXML
    private AnchorPane ultimate;
    @FXML
    Label incorrect_warning;
    @FXML
    TextField input_otp;

    @FXML
    TextField input_key;

    public static int finalotp = 0;
    private int i = 0;

    public void login(ActionEvent e)throws IOException
    {

        String username = usrname.getText();
        String password = passwd.getText();
        if ((username.isEmpty()) || (password.isEmpty())) {
            Alert nullException = new Alert(Alert.AlertType.ERROR);
            nullException.setTitle("Please enter valid values");
            nullException.setHeaderText("Empty username/password error");
            nullException.setContentText("Username/password cannot be empty");
            if(nullException.showAndWait().get() == ButtonType.OK) {
            }
        }
        else
        {
            generate_otp(username,password,e);
        }

    }
    public void generate_otp(String temp_username,String temp_password,ActionEvent action)
    {
        ManageSql obj = new ManageSql();
        String usrcheck = "";
        String pwdcheck = "";
        String phone = "";
        obj.GLOBAL_DB = temp_username;
        String md5_username = Encryption.md5generate(temp_username);
        String md5_password = Encryption.md5generate(temp_password);
        String query = "SELECT id, username, password, service, phone FROM "+obj.GLOBAL_DB+" WHERE id=1;";
        Connection con = obj.connectToDb();
        if(con == null)
        {
            incorrect_warning.setText("SQL server offline");
        }
        try{
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next())
            {
                usrcheck = rs.getString("username");
                pwdcheck = rs.getString("password");
                phone = rs.getString("phone");
            }
            rs.close();
            stmt.close();

        } catch (SQLException e)
        {
            System.out.println(e);
        }
        if ((usrcheck.compareTo(md5_username) != 0) || (pwdcheck.compareTo(md5_password) != 0))
        {
            incorrect_warning.setText("Incorrect username/password");
            i++;
            System.out.println(i);
            if(i==3)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("EXIT");
                alert.setHeaderText("Too many incorrect tries for username or password");
                alert.setContentText("Try again later");
                if(alert.showAndWait().get() == ButtonType.OK) {
                    Stage stage = (Stage) ultimate.getScene().getWindow();
                    stage.close();
                }
            }


        }
        else {
            String decoded_phone = Encryption.getDecoded(phone);
            finalotp = Fast2sms.sendSms(decoded_phone);
            try {
                proceed(action);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void logout()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You are about to logout..");
        alert.setContentText("Are you sure you want to exit?: ");
        if(alert.showAndWait().get() == ButtonType.OK) {
            Stage stage = (Stage) ultimate.getScene().getWindow();
            stage.close();
        }

    }

    public void proceed(ActionEvent e)throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("Credentials_match.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.show();
;
    }

    @FXML
    private Label incorrect_otp;

    public void check_otp(ActionEvent event)throws IOException {


        String temp = input_otp.getText();
        String str_otp = String.valueOf(finalotp);
        if (temp.isEmpty()) {
            Alert nullException = new Alert(Alert.AlertType.ERROR);
            nullException.setTitle("Error Occurred");
            nullException.setHeaderText("OTP cannot be empty");
            nullException.setContentText("Please try again");
            if (nullException.showAndWait().get() == ButtonType.OK) {
            }
        } else {
            if (temp.compareTo(str_otp) == 0) {

                Parent root = FXMLLoader.load(getClass().getResource("Enter_key.fxml"));
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                scene.setFill(Color.TRANSPARENT);
                stage.show();
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("NewMenuScreen.fxml"));
//                root = loader.load();
//                MenuScreen obj = loader.getController();
//                obj.getname();
//                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//                scene = new Scene(root);
//                stage.setScene(scene);
//                scene.setFill(Color.TRANSPARENT);
//                stage.show();
            }
            else {
                incorrect_otp.setText("Incorrect OTP.Please try again");
            }
        }
    }

    public void new_user(ActionEvent e)throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("signup/Signup_newUser.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.show();
    }

    @FXML
    public void goback(ActionEvent e)throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.show();
    }

    public void check_key(ActionEvent e)throws IOException
    {
        String temp = input_key.getText();
        Alert ask_user = new Alert(Alert.AlertType.CONFIRMATION);
        ask_user.setTitle("Are you sure that the entered key is correct?");
        ask_user.setHeaderText("Wrong key will lead to data not being read properly");
        ask_user.setContentText("Click OK if the key is correct.\nEntered key is :"+temp);
        if(ask_user.showAndWait().get() == ButtonType.OK)
        {
            AESCrypto.userkey=temp;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NewMenuScreen.fxml"));
            root = loader.load();
            MenuScreen obj = loader.getController();
            obj.getname();
            stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            scene.setFill(Color.TRANSPARENT);
            stage.show();
        }
    }



}
