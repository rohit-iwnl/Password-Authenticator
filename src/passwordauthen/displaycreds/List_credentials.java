package passwordauthen.displaycreds;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import passwordauthen.AESCrypto;
import passwordauthen.Encryption;
import passwordauthen.ManageSql;
import passwordauthen.MenuScreen;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class List_credentials implements Initializable {

    @FXML
    private Stage homescreen;
    private Scene home;
    private Parent root;

    @FXML
    TableView<Credentials> table;

    @FXML
    private TableColumn<Credentials, Integer> table_id;

    @FXML
    private TableColumn<Credentials, String> table_username;

    @FXML
    private TableColumn<Credentials, String> table_password;

    @FXML
    private TableColumn<Credentials, String> table_service;

    @FXML
    private TableColumn<Credentials, String> table_last_access;

    @FXML
    ObservableList<Credentials> list = FXCollections.observableArrayList();

    @FXML
    AnchorPane ultimate;


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
        AESCrypto crypto = new AESCrypto();
        String query = "SELECT * FROM "+obj.GLOBAL_DB+" WHERE id != 1";
        try{
            Statement sqlstatement = con.createStatement();
            ResultSet rs = sqlstatement.executeQuery(query);
            int i =1;
            while (rs.next()) {
                String temp_user = crypto.decrypt(rs.getString("username"));
                String temp_pass = crypto.decrypt(rs.getString("password"));
                String temp_srvc = crypto.decrypt(rs.getString("service"));
                String temp_last = crypto.decrypt(rs.getString("last_access_time"));
                list.add(new Credentials(i,temp_user,temp_pass,temp_srvc,temp_last));
                i++;
            }
            table.getItems().setAll(list);

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            System.out.println(e);
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

        Credentials(int id2,String usrname1,String passwd1,String srvc2,String last_acess1)
        {
            this.id1 = new SimpleIntegerProperty(id2);
            this.usrname = new SimpleStringProperty(usrname1);
            this.passwd = new SimpleStringProperty(passwd1);
            this.srvc = new SimpleStringProperty(srvc2);
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

    @FXML
    public void goback(ActionEvent e)throws IOException
    {
        MenuScreen obj = new MenuScreen();
        obj.goback(e);
    }
}
