package passwordauthen;

import javafx.beans.Observable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import passwordauthen.ManageSql;
import passwordauthen.displaycreds.List_credentials;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Delete_Credentials implements Initializable {
    @FXML
    private TableView<Credentials> delete_table;

    @FXML
    private TableColumn<Credentials, Integer> table_id;

    @FXML
    private TableColumn<Credentials, String> table_username;

    @FXML
    private TableColumn<Credentials,String> table_password;

    @FXML
    private TableColumn<Credentials, String> table_service;

    @FXML
    private TableColumn<Credentials, String> table_last_access;

    @FXML
    ObservableList<Credentials> list = FXCollections.observableArrayList();

    @FXML
    AnchorPane ultimate;

    @FXML
    TextField delete_choice;

    @FXML
    Button proceed_delete;


    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        initCol();
        ManageSql handler = new ManageSql();
        loadData();
    }

    private void loadData() {
        ManageSql obj = new ManageSql();
        Connection con = obj.connectToDb();
        String query = "SELECT * FROM "+obj.GLOBAL_DB+" WHERE service != 'MASTER'";
        try{
            Statement sqlstatement = con.createStatement();
            ResultSet rs = sqlstatement.executeQuery(query);
            int i =1;
            while (rs.next()) {
                String temp_user = rs.getString("username");
                String temp_pass = rs.getString("password");
                String temp_srvc = rs.getString("service");
                String temp_last = rs.getString("last_access_time");
                list.add(new Credentials(i,temp_user,temp_pass,temp_srvc,temp_last));
                i++;
            }
            delete_table.getItems().setAll(list);

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private void initCol()
    {
        table_id.setCellValueFactory(new PropertyValueFactory<>("id1"));
        table_username.setCellValueFactory(new PropertyValueFactory<>("usrname"));
        table_password.setCellValueFactory(new PropertyValueFactory<>("passwd"));
        table_service.setCellValueFactory(new PropertyValueFactory<>("srvc"));
        table_last_access.setCellValueFactory(new PropertyValueFactory<>("last_access"));

    }


    public static class Credentials{
        private final SimpleIntegerProperty id1;
        private final SimpleStringProperty usrname;
        private final SimpleStringProperty passwd;
        private final SimpleStringProperty srvc;
        private final SimpleStringProperty last_access;

        Credentials(int id2,String usrname1,String passwd1,String srvc1,String last_acess1)
        {
            this.id1 = new SimpleIntegerProperty(id2);
            this.usrname = new SimpleStringProperty(usrname1);
            this.passwd = new SimpleStringProperty(passwd1);
            this.srvc = new SimpleStringProperty(srvc1);
            this.last_access = new SimpleStringProperty(last_acess1);

        }

        public int getId1() {
            return id1.get();
        }


        public String getUsrname() {
            return usrname.get();
        }

        public String getPasswd() {
            return passwd.get();
        }

        public String getSrvc(){
            return srvc.get();
        }


        public String getLast_access() {
            return last_access.get();
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
    public void proceed(ActionEvent e)throws IOException {
        String temp = delete_choice.getText();
        int choice = Integer.parseInt(temp);
        if (choice == 0) {
            Alert zeroerror = new Alert(Alert.AlertType.ERROR);
            zeroerror.setTitle("Enter a valid input");
            zeroerror.setHeaderText("0 is not valid input.The range starts from 1");
            if (zeroerror.showAndWait().get() == ButtonType.OK) {
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Are you sure about deleting that credential?");
            alert.setHeaderText("Click OK to delete credential with ID:" + choice);
            if (alert.showAndWait().get() == ButtonType.OK) {
                MenuScreen obj = new MenuScreen();
                obj.delete_credentials(choice);
                Alert back = new Alert(Alert.AlertType.CONFIRMATION);
                back.setTitle("Do you want to return to main screen?");
                back.setHeaderText("Click OK to go back or CANCEL to stay in this page");
                back.setContentText("List will be updated only when you return to home screen");
                if (back.showAndWait().get() == ButtonType.OK) {
                    goback(e);
                }
            }

        }
    }

    @FXML
    public void goback(ActionEvent e)throws IOException
    {
        MenuScreen obj = new MenuScreen();
        obj.goback(e);
    }
}
