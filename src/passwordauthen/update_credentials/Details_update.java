package passwordauthen.update_credentials;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import passwordauthen.AESCrypto;
import passwordauthen.Encryption;
import passwordauthen.ManageSql;
import passwordauthen.MenuScreen;

import java.io.IOException;
import java.util.Date;


public class Details_update {

    @FXML
    private TextField update_username;

    @FXML
    private TextField update_password;

    @FXML
    private TextField update_service;

    public void update(ActionEvent e)throws IOException
    {
        try {
            AESCrypto crypto = new AESCrypto();
            String temp_user = crypto.encrypt(update_username.getText());
            String temp_pass = crypto.encrypt(update_password.getText());
            String temp_service = crypto.encrypt(update_service.getText());
            int update_id = Update_credentials.update_id;
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Update Confirmation");
            alert.setHeaderText("You are about to update");
            alert.setContentText("Credentials will be update once you go back to main menu");
            if (alert.showAndWait().get() == ButtonType.OK) {
                ManageSql obj = new ManageSql();
                if (!temp_user.isEmpty()) {
                    String sql = "UPDATE " + obj.GLOBAL_DB + " SET username = '" + temp_user + "' WHERE id = " + (update_id + 1);
                    obj.execute(sql);
                }
                if (!temp_pass.isEmpty()) {
                    String sql = "UPDATE " + obj.GLOBAL_DB + " SET password = '" + temp_pass + "' WHERE id = " + (update_id + 1);
                    obj.execute(sql);
                }
                if (!temp_service.isEmpty()) {
                    String sql = "UPDATE " + obj.GLOBAL_DB + " SET service = '" + temp_service + "' WHERE id = " + (update_id + 1);
                    obj.execute(sql);
                }
                Date date = new Date();
                String encoded_date = crypto.encrypt(date.toString());
                String update = "UPDATE " + obj.GLOBAL_DB + " SET last_access_time = '" + encoded_date + "' WHERE id = " + (update_id + 1);
                obj.execute(update);
                Alert updated_alert = new Alert(Alert.AlertType.CONFIRMATION);
                updated_alert.setTitle("Credentials Updated");
                updated_alert.setHeaderText("Click OK to go back to main menu");
                updated_alert.setContentText("Click CANCEL to stay here");
                if (updated_alert.showAndWait().get() == ButtonType.OK) {
                    goback(e);
                }

            }
        }catch (Exception ex)
        {
            System.out.println(ex);
        }



    }

    public void goback(ActionEvent e)throws IOException
    {
        MenuScreen obj = new MenuScreen();
        obj.goback(e);
    }
}
