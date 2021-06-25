package passwordauthen;

import javafx.scene.paint.Color;

import javax.crypto.SecretKey;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Loginuser extends Controller{
    ManageSql obj = new ManageSql();
    static Scanner sc = new Scanner(System.in);
    public void new_user() {
        System.out.println("Enter username of the master account");
        String usrmaster = sc.nextLine();
        
        System.out.println("Enter password of the master account");
        String pwdmaster = sc.nextLine();
        System.out.println("Enter your phone number without International Code");
        String phone = sc.nextLine();
        int check = obj.check(usrmaster);
        if(check == 1)
        {
            System.err.println("Account already exists. Login with the same");
            System.exit(0);
        }
        String sql="CREATE TABLE "+usrmaster+" (id int NOT NULL AUTO_INCREMENT, username varchar(50) NOT NULL, password varchar(50) NOT NULL,service varchar(50) NOT NULL,last_access_time varchar(50) DEFAULT NULL,phone varchar(11) NOT NULL,PRIMARY KEY (id))";
        Connection con = obj.connectToDb();
        String masterloginupdate = "INSERT INTO "+usrmaster+" (username,password,service,phone) VALUES('"+usrmaster+"','"+pwdmaster+"','MASTER','"+phone+"')";
        obj.execute(sql);
        obj.execute(masterloginupdate);
        System.out.println("Account has been created.Login with the same");
        System.out.println("EXITING NOW...");
        System.exit(0);

    }
    public void existing_user(String temp_username,String temp_password,int otpcheck) {
        System.out.println(otpcheck);
        System.out.println("hi");
        String usrcheck = "";
        String pwdcheck = "";
        String phone = "";
        obj.GLOBAL_DB = temp_username;
        String query = "SELECT id, username, password, service, phone FROM "+obj.GLOBAL_DB+" WHERE id=1;";
        Connection con = obj.connectToDb();
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
        while ((usrcheck.compareTo(temp_username) != 0) || (pwdcheck.compareTo(temp_password) != 0))
        {
            incorrect_warning.setText("Incorrect username/password");
        }
        if ((usrcheck.compareTo(temp_username) != 0) && (pwdcheck.compareTo(temp_password) != 0)){
            System.out.println("Credentials matched!");
            System.out.println("Enter the OTP that was sent to your phone:");
            if(otpcheck == finalotp){
                System.out.println("Login successful");
            }
            else{
                System.err.println("The entered OTP is wrong");
            }
        }
        else {
            System.err.println("Incorrect Username/Password/. Please check details again");
            System.exit(0);
        }
    }



}
