package passwordauthen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MenuScreen {

    @FXML
    private Stage homescreen;
    private Scene home;
    private Parent root;
    @FXML
    private Label hellouser_label;
    @FXML
    private Button logout;
    @FXML
    private AnchorPane ultimate;
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root1;
    @FXML
    private Button testbutton;
    @FXML
    TextField add_username;
    @FXML
    TextField add_password;
    @FXML
    TextField add_service;
    @FXML
    Label add_prompt;
    @FXML
    TextField delete_choice;
    @FXML
    Button main_screen_button;
    @FXML
    Button proceed_delete;
    @FXML
    TextField website_name;
    @FXML
    private Label last_login_label;

    public void getname(){
        ManageSql obj = new ManageSql();
        Connection con = obj.connectToDb();
        String query = "SELECT * FROM "+obj.GLOBAL_DB+" WHERE id = 1";
        String name = null;
        String last_login = null;
        try{
            Statement sqlstatement = con.createStatement();
            ResultSet rs = sqlstatement.executeQuery(query);
            while (rs.next()) {
                name = rs.getString("name");
                last_login = rs.getString("last_login");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        String decoded_name = Encryption.getDecoded(name);
        String decoded_last_login = Encryption.getDecoded(last_login);
        hellouser_label.setText("Hello "+decoded_name);
        last_login_label.setText(decoded_last_login);
    }

    public void logout()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You are about to logout..");
        alert.setContentText("Are you sure you want to exit?: ");
        if(alert.showAndWait().get() == ButtonType.OK) {
            ManageSql update = new ManageSql();
            update.update_login_time();
            Stage stage = (Stage) ultimate.getScene().getWindow();
            stage.close();
        }
    }
    @FXML
    public void goback(ActionEvent e)throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("NewMenuScreen.fxml"));
        root = loader.load();
        MenuScreen obj = loader.getController();
        obj.getname();
        homescreen = (Stage) ((Node) e.getSource()).getScene().getWindow();
        home = new Scene(root);
        homescreen.setScene(home);
        home.setFill(Color.TRANSPARENT);
        homescreen.show();
    }
    @FXML
    public void add(ActionEvent e)throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("add_credentials/New_addMenu.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.show();
    }
    public void add_proceed() {
        String tempusr = add_username.getText();
        String temppwd = add_password.getText();
        String tempsrvc = add_service.getText();
        if (tempusr.isEmpty() || temppwd.isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Empty Username or Password field error");
            alert.setHeaderText("Username or Password cannot be empty.Please type empty if you do not wish to enter any text");
            if (alert.showAndWait().get() == ButtonType.OK) {
            }
        } else if (tempsrvc.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Empty Service field error");
            alert.setHeaderText("Service field cannot be empty.Please type empty if you do not wish to enter any text");
            if (alert.showAndWait().get() == ButtonType.OK) {
            }
        } else {
            try {
                Sll obj = new Sll();
                obj.add_credentials(tempusr, temppwd, tempsrvc);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Added Credentials successfully");
                if (alert.showAndWait().get() == ButtonType.OK) {
                }
            } catch (Exception e) {

            }
        }
    }

    public void display_menu(ActionEvent e)throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("displaycreds/NewShow_credentials.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.show();
    }

    public void display_after_delete(ActionEvent e)throws IOException
    {
        goback(e);
        display_menu(e);
    }

    public void delete_menu(ActionEvent e)throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("NewDelete_Credentials.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.show();

    }
    public void delete_credentials(int choice)
    {
        ManageSql obj = new ManageSql();
        obj.delete_credentials(choice);
    }

    @FXML
    private void addtest(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("displaycreds/Show_credentials.fxml"));
        stage = (Stage)(testbutton.getScene().getWindow());
        scene = new Scene(root);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.show();
    }

    @FXML
    private void open_website(ActionEvent e)throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("WebsiteOpen.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.show();
    }

    public void website_open() {
        String name = website_name.getText();
        if (name.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Website name cannot be numm");
            alert.setHeaderText("Please enter a valid website");
            if (alert.showAndWait().get() == ButtonType.OK) {
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Are you sure you want to continue?");
            alert.setHeaderText("You will be redirected to "+name+" on your default browser");
            if (alert.showAndWait().get() == ButtonType.OK) {
                OpenWeb obj = new OpenWeb();
                obj.open(name);
            }

        }
    }

    public void search(ActionEvent e)throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("display_specific/Show_ListView.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.show();
    }

    public void update_menu(ActionEvent e)throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("update_credentials/updatemenu.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.show();

    }

}
