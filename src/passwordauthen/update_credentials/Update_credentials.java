package passwordauthen.update_credentials;

import javafx.beans.Observable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import passwordauthen.AESCrypto;
import passwordauthen.Encryption;
import passwordauthen.ManageSql;
import passwordauthen.MenuScreen;
import passwordauthen.displaycreds.List_credentials;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Update_credentials implements Initializable {

    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
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
    TextField update_choice;

    @FXML
    Button proceed_delete;

    @FXML
    TextField update_username;

    @FXML
    TextField update_password;

    @FXML
    TextField update_service;

    @FXML
    Button update_button;

    public static int update_id;

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
            delete_table.getItems().setAll(list);

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
        ManageSql count = new ManageSql();
        String temp = update_choice.getText();
        int choice = Integer.parseInt(temp);
        update_id = choice;
        if (choice == 0 || choice > (count.count()-1) ) {
            Alert zeroerror = new Alert(Alert.AlertType.ERROR);
            zeroerror.setTitle("Enter a valid input");
            zeroerror.setHeaderText("The entered number is not a valid id of a credential");
            if (zeroerror.showAndWait().get() == ButtonType.OK) {
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Are you sure about updating that credential?");
            alert.setHeaderText("Click OK to update credential with ID:" + choice);
            if (alert.showAndWait().get() == ButtonType.OK)
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("update_details.fxml"));
                root = loader.load();
                stage = (Stage)(update_button.getScene().getWindow());
                scene = new Scene(root);
                stage.setScene(scene);
                scene.setFill(Color.TRANSPARENT);
                stage.show();
            }
            }

        }

        public void update(ActionEvent e)throws IOException
        {

            String temp_user = update_username.getText();
            String temp_pass = update_password.getText();
            String temp_service = update_service.getText();
            System.out.println(temp_user);
            System.out.println(temp_pass);
            System.out.println(temp_service);

        }

        @FXML
        public void nextscreen()throws IOException
        {
            Parent root = FXMLLoader.load(getClass().getResource("update_details.fxml"));
            stage = (Stage)(update_button.getScene().getWindow());
            scene = new Scene(root);
            stage.setScene(scene);
            scene.setFill(Color.TRANSPARENT);
            stage.show();
        }

        public void goback(ActionEvent e)throws IOException
        {
            MenuScreen obj = new MenuScreen();
            obj.goback(e);
        }
}
