package passwordauthen;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;

import javax.crypto.SecretKey;
import java.sql.*;
import java.util.Date;
import java.util.Scanner;

public class ManageSql {
    Scanner sc = new Scanner(System.in);
    static final String DB_URL = "jdbc:mysql://localhost:3306/credentials";
    static final String USER = "root";
    static final String PASS = "";
    public static String GLOBAL_DB=null;
    Connection connect;
    ResultSet rs;
    int flag_delete = 0;
    int flag_count = 0;

    public void execute(String sql)
    {
        Connection con = connectToDb();
        try {
            Statement sqlstatement = con.createStatement();
            sqlstatement.executeUpdate(sql);
        } catch (SQLException throwables) {

        }
    }

    protected void display_records() throws SQLException {
        Connection con = connectToDb();
        System.out.printf("  %-22s%-22s%-22s%-22s\n", "USERNAMES", "PASSWORDS", "SERVICE","LAST ACCESS TIME");
        int i = 1;
        String query = "SELECT * FROM "+GLOBAL_DB+" WHERE service != 'MASTER'";
        try{
            Statement sqlstatement = con.createStatement();
            ResultSet rs = sqlstatement.executeQuery(query);
                while (rs.next()) {
                    System.out.printf(i + ":" + "%-22s%-22s%-22s%-22s\n", rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
                    i++;
                }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }


    }

    public int check(String dataBaseName)
    {
        int flag =0;
        try {
            Connection con = DriverManager.getConnection(DB_URL,USER,PASS);
            String query = "SELECT * FROM "+dataBaseName;
            Statement sqlstatement = con.createStatement();
            ResultSet rs = sqlstatement.executeQuery(query);
            if(rs != null)
                flag=1;
            else if(rs == null)
                flag = 0;
            rs.close();
        } catch (SQLException throwables) {
        }
        return flag;
    }

    public void search_credentials(String service)
    {
        Connection con = null;
        System.out.printf("  %-22s%-22s%-22s%-22s\n", "USERNAMES", "PASSWORDS", "SERVICE","LAST ACCESS TIME");
        int i = 1;
        try {
            con = DriverManager.getConnection(DB_URL,USER,PASS);
            Statement sqlstatement = con.createStatement();
            String query = "SELECT * FROM "+GLOBAL_DB+" WHERE service = '"+service+"'";
            ResultSet rs = sqlstatement.executeQuery(query);
            while (rs.next())
            {
                System.out.printf(i+":"+"%-22s%-22s%-22s%-22s\n",rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5));
                i++;
            }
            if(i==1)
            {
                System.err.println("No Records found");
            }else{
                System.out.println("Do you want to login to that service now : (Y|N)");
                int j = sc.nextInt();
                if(j==1)
                {
//                    OpenWeb.open(service);
                }
            }



            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }



    }

    public void delete_credentials(int deletechoice) {
        Connection con = connectToDb();
        String sql = "DELETE FROM " + GLOBAL_DB + " WHERE id=" + (deletechoice + 1);
        execute(sql);
        String query = "SELECT * FROM " + GLOBAL_DB + " WHERE id > " + (deletechoice);
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int nextcount = deletechoice + 2;
            int updatecount = deletechoice;
            int temp_count = count();
            if (deletechoice == (temp_count - 1)) {
                String update_del = "UPDATE " + GLOBAL_DB + " SET id = '" + (temp_count) + "' WHERE id = " + (temp_count + 1);
                execute(update_del);
            } else {
                while (rs.next()) {
                    String update = "UPDATE " + GLOBAL_DB + " SET id = '" + (updatecount+1) + "' WHERE id = " + nextcount;
                    nextcount++;
                    updatecount++;
                    execute(update);
                }
                flag_delete = 1;
                flag_count = nextcount;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }



    public Connection connectToDb(){
        try {
            connect = DriverManager.getConnection(DB_URL,USER,PASS);
        }catch (CommunicationsException e){
            System.err.println("Sql server offline");
        }
        catch (SQLException e){
            System.out.println(e);
        }
        return connect;
    }

    public int count()
    {
        Connection con = connectToDb();
        int i = 0;
        String query = "SELECT * FROM "+GLOBAL_DB;
        try{
            Statement sqlstatement = con.createStatement();
            ResultSet rs = sqlstatement.executeQuery(query);
            while (rs.next()) {
                i++;
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return i;

    }

    public SecretKey retrieve_key()
    {
        Connection con =  connectToDb();
        String query = "SELECT * FROM "+GLOBAL_DB+" WHERE id = 1";
        SecretKey key = null;
        String temp = null;
        try{
            Statement sqlstatement = con.createStatement();
            ResultSet rs = sqlstatement.executeQuery(query);
            while(rs.next())
            {
                temp = rs.getString(7);
            }
            System.out.println(temp);
        }
        catch (SQLException e)
        {
            System.out.println(e);
        }
        return key;
    }

    public void update_login_time()
    {
        Connection con = connectToDb();
        String query = "SELECT * FROM " + GLOBAL_DB;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            Date date=new Date();
            String update = "UPDATE " + GLOBAL_DB + " SET last_login = '" +date+ "' WHERE id = 1";
            execute(update);
        }catch (SQLException e)
        {
            System.out.println(e);
        }

    }


}
