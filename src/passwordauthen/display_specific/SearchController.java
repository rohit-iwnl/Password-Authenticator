package passwordauthen.display_specific;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SearchController implements Initializable {

    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ListView<String> username_ListView;

    @FXML
    private Button proceed_button;

    @FXML
    private ListView<String> password_ListView;

    @FXML
    private ListView<String> service_ListView;

    @FXML
    Label current_item;

    ArrayList<String> usernames = new ArrayList<String>();
    ArrayList<String> passwords = new ArrayList<String>();
    ArrayList<String> services = new ArrayList<String>();

    String current;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1)
    {
        try {
            get_array();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        username_ListView.getItems().addAll(usernames);
        password_ListView.getItems().addAll(passwords);
        service_ListView.getItems().addAll(services);
        SearchProceedController getinput = new SearchProceedController();


        username_ListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
                current = username_ListView.getSelectionModel().getSelectedItem();
                getinput.choice = "username";
                getinput.input = current;

            }
        });

        password_ListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
                current = password_ListView.getSelectionModel().getSelectedItem();
                getinput.choice = "password";
                getinput.input = current;

            }
        });

        service_ListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
                current = service_ListView.getSelectionModel().getSelectedItem();
                getinput.choice = "service";
                getinput.input = current;

            }
        });

    }

    protected void get_array() throws SQLException {
        ManageSql obj = new ManageSql();
        Connection con = obj.connectToDb();
        AESCrypto crypto = new AESCrypto();
        String query = "SELECT * FROM "+obj.GLOBAL_DB+" WHERE id != 1";
        try{
            Statement sqlstatement = con.createStatement();
            ResultSet rs = sqlstatement.executeQuery(query);
            while (rs.next()) {
                String temp_user = crypto.decrypt(rs.getString("username"));
                String temp_pass = crypto.decrypt(rs.getString("password"));
                String temp_service = crypto.decrypt(rs.getString("service"));
                if(!usernames.contains(temp_user))
                {
                    usernames.add(temp_user);
                }
                if(!passwords.contains(temp_pass))
                {
                    passwords.add(temp_pass);
                }
                if(!services.contains(temp_service))
                {
                    services.add(temp_service);
                }
            }

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

    @FXML
    public void proceed_to_delete()throws IOException {
        SearchProceedController getinput = new SearchProceedController();
        if (getinput.choice == null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Search Confirmation");
            alert.setHeaderText("You haven't selected any field to base the search on");
            alert.setContentText("Please choose a field first to searched ");
            if (alert.showAndWait().get() == ButtonType.OK) {
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Search Confirmation");
            alert.setHeaderText("You are about to search based on the field : " + getinput.input);
            alert.setContentText("Are you sure you want to exit?: ");
            if (alert.showAndWait().get() == ButtonType.OK) {
                Parent root = FXMLLoader.load(getClass().getResource("Proceed_after_searching.fxml"));
                stage = (Stage) (proceed_button.getScene().getWindow());
                scene = new Scene(root);
                stage.setScene(scene);
                scene.setFill(Color.TRANSPARENT);
                stage.show();
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
