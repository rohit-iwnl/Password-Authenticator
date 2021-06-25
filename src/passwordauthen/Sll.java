package passwordauthen;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;

public class Sll
{
    ManageSql obj = new ManageSql();
    LinkedList usernames = new LinkedList();
    LinkedList passwords = new LinkedList();
    LinkedList services = new LinkedList();
    Scanner sc = new Scanner(System.in);
    public void add_credentials(String usr,String pwd,String srvc) throws Exception
    {
        AESCrypto crypto = new AESCrypto();
        int temp_count = obj.count();
        Date date=new Date();
        String temp_username = crypto.encrypt(usr);
        String temp_password = crypto.encrypt(pwd);
        String temp_service = crypto.encrypt(srvc);
        String temp_date = crypto.encrypt(date.toString());
        String sql ="INSERT INTO "+obj.GLOBAL_DB+" (id,username,password,service,last_access_time,phone) VALUES("+(temp_count+1)+",'"+temp_username+"','"+temp_password+"','"+temp_service+"','"+temp_date+"',0)";
        obj.execute(sql);
    }

    private static String getEncoded(String data)
    {
        return Base64.getEncoder().encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }

    public void display()
    {
        try {
            obj.display_records();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void search()
    {
        System.out.println("Enter service name");
        String srvc = sc.nextLine();
        obj.search_credentials(srvc);
    }

    public void openwebsite()
    {

        System.out.println("Enter the service you want to open :");
        String srvc = sc.nextLine();
        System.out.println(srvc);
//        OpenWeb openw = new OpenWeb();
//        openw.open(srvc);
    }

    public void update_credentials()
    {
        display();
        System.out.println("Enter the credential number to updated :");
        int id = sc.nextInt();
        id=id+1;
        try {
            System.out.println("Press 1 to update username and 2 to update password");
            int choice = sc.nextInt();
            String sql = null;
            String field = null;
            switch (choice) {
                case 1:
                    field = "username";
                    break;
                case 2:
                    field = "password";
                    break;
                default:
                    System.out.println("Please enter a valid input");

            }
            System.out.println("Enter the new " + field + " to be updated");
            String updated = sc.next();
            sql = "UPDATE " + obj.GLOBAL_DB + " SET " + field + " = '" + updated + "' WHERE id = " + id;
            obj.execute(sql);
            Date date = new Date();
            sql = "UPDATE " + obj.GLOBAL_DB + " SET last_access_time = '" + date + "' WHERE id = " + id;
            obj.execute(sql);
            System.out.println("Updated successfully");
        }catch (InputMismatchException e)
        {
            System.out.println("Please enter only numbers");
        }

    }



}