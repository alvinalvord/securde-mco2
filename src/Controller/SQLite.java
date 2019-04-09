package Controller;

import Model.*;
import sun.rmi.runtime.*;

import java.io.*;
import java.sql.*;
import java.util.*;

public class SQLite {

    public int DEBUG_MODE = 0;
    String driverURL = "jdbc:sqlite:" + "database.db";

    public void createNewDatabase() {
        try (Connection conn = DriverManager.getConnection(driverURL)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Database database.db created.");

                Controller.Logger.log ("database creation", "database was created");
            }
        } catch (Exception ex) {
            Controller.Logger.log ("database access error", "forced exit due to failure to connect to database @database create");
            System.exit (0);
        }
    }
    
    public void createHistoryTable() {
        String sql = "CREATE TABLE IF NOT EXISTS history (\n"
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " username TEXT NOT NULL,\n"
            + " name TEXT NOT NULL,\n"
            + " stock INTEGER DEFAULT 0,\n"
            + " price REAL DEFAULT 0.0, \n"
            + " timestamp TEXT NOT NULL\n"
            + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table history in database.db created.");
            Controller.Logger.log ("history table creation", "history table was created");
        } catch (Exception ex) {
            Controller.Logger.log ("database access error", "forced exit due to failure to connect to database @history table creation");
        }
    }
    
    public void createLogsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS logs (\n"
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " event TEXT NOT NULL,\n"
            + " username TEXT NOT NULL,\n"
            + " desc TEXT NOT NULL,\n"
            + " timestamp TEXT NOT NULL\n"
            + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table logs in database.db created.");
            Controller.Logger.log ("logs table creation", "logs table was created");
        } catch (Exception ex) {
            Controller.Logger.log ("database access error", "forced exit due to failure to connect to database @logs table creation");
        }
    }
     
    public void createProductTable() {
        String sql = "CREATE TABLE IF NOT EXISTS product (\n"
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " name TEXT NOT NULL UNIQUE,\n"
            + " stock INTEGER DEFAULT 0,\n"
            + " price REAL DEFAULT 0.00\n"
            + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table product in database.db created.");
            Controller.Logger.log ("product table creation", "product table was created");
        } catch (Exception ex) {
            Controller.Logger.log ("database access error", "forced exit due to failure to connect to database @product table creation");
        }
    }
     
    public void createUserTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " username TEXT NOT NULL UNIQUE,\n"
            + " password TEXT NOT NULL,\n"
            + " role INTEGER DEFAULT 2,\n"
            + " locked INTEGER DEFAULT 0\n"
            + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table users in database.db created.");
            Controller.Logger.log ("user table creation", "user table was created");
        } catch (Exception ex) {
            Controller.Logger.log ("database access error", "forced exit due to failure to connect to database @user table creation");
            System.exit (0);
        }
    }
    
    public void dropHistoryTable() {
        String sql = "DROP TABLE IF EXISTS history;";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            Logger.log ("drop table", "successfully dropped history table");
        } catch (Exception ex) {
            Logger.log ("drop table", "failed to drop history table");
        }
    }
    
    public void dropLogsTable() {
        String sql = "DROP TABLE IF EXISTS logs;";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            Logger.log ("drop table", "successfully dropped logs table");
        } catch (Exception ex) {
            Logger.log ("drop table", "failed to drop logs table");
        }
    }
    
    public void dropProductTable() {
        String sql = "DROP TABLE IF EXISTS product;";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table product in database.db dropped.");
            Logger.log ("drop table", "successfully dropped product table");
        } catch (Exception ex) {
            Logger.log ("drop table", "failed to drop product table");
        }
    }
    
    public void dropUserTable() {
        String sql = "DROP TABLE IF EXISTS users;";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table users in database.db dropped.");
            Controller.Logger.log ("drop table", "users table dropped");
        } catch (Exception ex) {
            Controller.Logger.log ("database access error", "forced exit due to failure to connect to database @drop user table");
            System.exit (0);
        }
    }
    
    public void addHistory(String username, String name, int stock, float price, String timestamp) {
        String sql = "INSERT INTO history(username,name,stock,price,timestamp) VALUES('" + username + "','" + name + "','" + stock + "','" + price + "','" + timestamp + "')";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (Exception ex) {
                Logger.log ("history", "failed to add history");
        }
    }
    
    public void addLogs(String event, String username, String desc, String timestamp) {
        String sql = "INSERT INTO logs(event,username,desc,timestamp) VALUES('" + event + "','" + username + "','" + desc + "','" + timestamp + "')";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (Exception ex) {
                Logger.log ("logs", "failed to add logs");
        }
    }
    
    public boolean addProduct(String name, int stock, double price) {
        String sql = "INSERT INTO product(name,stock,price) VALUES('" + name + "','" + stock + "','" + price + "')";
        boolean retval = false;
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()){
            stmt.execute(sql);
            retval = true;
            Logger.log ("database update", "a new product has been added");
            Logger.dblog ("database update", Main.getInstance ().model.getUser ().getUsername (), "added a new product");
        } catch (Exception ex) {
                Logger.log ("database error", "failed to add a new product");
        }
        return retval;
    }
    
    public void addUser(String username, String password) {
        String hashedPassword = PasswordEncryption.hash (password);
        String sql = "INSERT INTO users(username,password) VALUES('" + username + "','" + hashedPassword + "')";

        execAddUser (sql, username);

//        try (Connection conn = DriverManager.getConnection(driverURL);
//            Statement stmt = conn.createStatement()){
//            stmt.execute(sql);
//
//            Controller.Logger.log ("user add", "user " + username + " added");
//      PREPARED STATEMENT EXAMPLE
//      String sql = "INSERT INTO users(username,password) VALUES(?,?)";
//      PreparedStatement pstmt = conn.prepareStatement(sql)) {
//      pstmt.setString(1, username);
//      pstmt.setString(2, password);
//      pstmt.executeUpdate();
//        } catch (Exception ex) {
//            Controller.Logger.log ("database access error", "forced exit due to failure to connect to database @account creation");
//            System.exit (0);
//        }
    }
    
    
    public ArrayList<History> getHistory(){
        String sql = "SELECT id, username, name, stock, price, timestamp FROM history";
        ArrayList<History> histories = new ArrayList<History>();
        List<User> users = getUsers ();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while (rs.next()) {
                histories.add(new History(rs.getInt("id"),
                                   rs.getString("username"),
                                   rs.getString("name"),
                                   rs.getInt("stock"),
                                   rs.getFloat ("price"),
                                   rs.getString("timestamp")));
            }
        } catch (Exception ex) {
                Logger.log ("database error", "unable to retrieve history");
        }

        Iterator<History> iter = histories.iterator ();

        while (iter.hasNext ()) {
            History cur = iter.next ();

            if (Main.getInstance ().model.isClient ()) {
                if (!cur.getUsername ().equalsIgnoreCase (Main.getInstance ().model.getUser ().getUsername ())) {
                    iter.remove ();
                }
            } else {
                if (!cur.getUsername ().equalsIgnoreCase (Main.getInstance ().model.getUser ().getUsername ())) {
                    for (User u: users) {
                        if (cur.getUsername ().equalsIgnoreCase (u.getUsername ())) {
                            if (Main.getInstance ().model.getUser ().getRole () <= u.getRole ()) {
                                iter.remove ();
                            }
                            break;
                        }
                    }
                }
            }
        }

        return histories;
    }
    
    public ArrayList<Logs> getLogs(){
        String sql = "SELECT id, event, username, desc, timestamp FROM logs";
        ArrayList<Logs> logs = new ArrayList<Logs>();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while (rs.next()) {
                logs.add(new Logs(rs.getInt("id"),
                                   rs.getString("event"),
                                   rs.getString("username"),
                                   rs.getString("desc"),
                                   rs.getString("timestamp")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.log ("logs", "failed to retrieve logs");
        }
        return logs;
    }

    public void purchaseProduct (String productname, int newcount) {
        StringBuilder sb = new StringBuilder ();
        sb.append ("UPDATE product ")
                .append (" SET stock = ").append (newcount)
                .append (" WHERE name = '").append (productname).append ("';");

        String sql = sb.toString ();

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()) {
            stmt.execute (sql);
        } catch (SQLException e) {
            Logger.log ("purchase error", "failed to purchase " + productname);
            Logger.dblog ("ERROR", Main.getInstance ().model.getUser ().getUsername (), "failed to purchase " + productname);
            System.exit (0);
        }
    }

    public ArrayList<Product> getProduct(){
        String sql = "SELECT id, name, stock, price FROM product";
        ArrayList<Product> products = new ArrayList<Product>();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while (rs.next()) {
                products.add(new Product(rs.getInt("id"),
                                   rs.getString("name"),
                                   rs.getInt("stock"),
                                   rs.getFloat("price")));
            }
        } catch (Exception ex) {
            Logger.log ("get product", "failed to get products");
        }
        return products;
    }
    
    public ArrayList<User> getUsers(){
        String sql = "SELECT id, username, password, role, locked FROM users";
        ArrayList<User> users = new ArrayList<User>();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while (rs.next()) {
                users.add(new User(rs.getInt("id"),
                                   rs.getString("username"),
                                   rs.getString("password"),
                                   rs.getInt("role"),
                                   rs.getInt("locked")));
            }
        } catch (Exception ex) {
                Logger.log ("get users", "failed to get users");
        }
        return users;
    }

    public void execAddUser (String sql, String username) {
        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()){
            stmt.execute(sql);

            Controller.Logger.log ("user add", "user " + username + " added");
            Logger.dblog ("NOTICE", username, "user account created");
        } catch (Exception ex) {
            Controller.Logger.log ("database access error", "forced exit due to failure to connect to database @account creation");
            System.exit (0);
        }
    }

    public void addUser(String username, String password, int role) {
        String hashedPassword = PasswordEncryption.hash (password);
        String sql = "INSERT INTO users(username,password,role) VALUES('" + username + "','" + hashedPassword + "','" + role + "')";

        execAddUser (sql, username);
    }
    
    public void removeUser(String username) {
        String sql = "DELETE FROM users WHERE username='" + username + "';";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("User " + username + " has been deleted.");
            Logger.log ("user delete", "user " + username + " has been deleted");
            Logger.dblog ("user delete", Main.getInstance ().model.getUser ().getUsername (), "user " + username + " has been deleted");
        } catch (Exception ex) {
            Logger.log ("user delete", "failed to delete user " + username);
        }
    }

    public void editProduct (Product product, String name, int stock, float price) {
        StringBuilder sb = new StringBuilder ();
        sb.append ("UPDATE product ")
                .append ("SET name = '").append (name).append ("', ")
                .append (" stock = ").append (stock).append (", ")
                .append (" price = ").append (price)
                .append (" WHERE id = ").append (product.getId ()).append (";");

        String sql = sb.toString ();

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()) {
            stmt.execute (sql);

            Logger.log ("database update", "edited product " + product.getId ());
            Logger.dblog ("databse update", Main.getInstance ().model.getUser ().getUsername (), "edited product "+ product.getId ());
        } catch (SQLException e) {
            Logger.log ("database error", "unable to edit product " + product.getId ());
        }
    }

    public Product getProduct(String name){
        String sql = "SELECT id, name, stock, price FROM product WHERE name='" + name + "';";
        Product product = null;
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            product = new Product(rs.getInt ("id"),
                    rs.getString("name"),
                   rs.getInt("stock"),
                   rs.getFloat("price"));
        } catch (Exception ex) {
                Logger.log ("database error", "unable to retrieve product");
                System.exit (0);
        }
        return product;
    }

    public User getUser (String username, String password) {
        StringBuilder sb = new StringBuilder ();
        sb.append (" select id, username, password, role, locked ")
                .append (" from users ")
                .append (" where username = '")
                .append (username)
                .append ("' and ")
                .append (" password = '")
                .append (PasswordEncryption.hash (password))
                .append ("'");

        String sql = sb.toString ();

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){

            User user = null;
            while (rs.next()) {
                user = new User(rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("role"),
                        rs.getInt ("locked"));
            }

            Controller.Logger.log ("data get", "login request was queried");
            return user;
        } catch (Exception ex) {
            Controller.Logger.log ("database access error", "forced exit due to failure to connect to database @login");
            System.exit (0);
        }

        return null;
    }

    public boolean userExists (String user) {
        StringBuilder sb = new StringBuilder ();
        sb.append ("select username ")
                .append (" from users ")
                .append (" where username = '").append (user).append ("'");

        String sql = sb.toString ();

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next ()) { return true; }

            Controller.Logger.log ("data get", "username" + user + " availability checked");
        } catch (Exception ex) {
            Controller.Logger.log ("database access error", "forced exit due to failure to connect to database @register");
            System.exit (0);
        }

        return false;
    }

    public void saveAndClearLogs (String filepath) {
        List<Logs> logs = getLogs ();

        try {
            PrintWriter printWriter = new PrintWriter (new FileWriter (filepath, true));

            for (Logs l : logs) {
                printWriter.println (l.toString ());
            }
            printWriter.close ();
        } catch (IOException e) {
            Logger.log ("log error", "failed to save logs");
            System.exit (0);
        }

        Logger.log ("save logs", "logs have been saved to " + filepath);
        dropLogsTable ();
        createLogsTable ();

        Logger.dblog ("NOTICE", Main.getInstance ().model.getUser ().getUsername (), "database logs were saved into " + filepath);
    }

    public boolean deleteProduct (int id) {
        StringBuilder sb = new StringBuilder ();
        sb.append ("DELETE FROM product ")
                .append (" WHERE id = ").append (id).append (";");

        boolean retval = false;
        String sql = sb.toString ();
        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()) {
            stmt.execute (sql);
            retval = true;
            Logger.log ("database update", "deleted product " + id);
            Logger.dblog ("databse update", Main.getInstance ().model.getUser ().getUsername (), "deleted product "+ id);
        } catch (SQLException e) {
            Logger.log ("database error", "unable to delete product " + id);
        }

        return retval;
    }

    public List<User> getUsers2 () {
        if (Main.getInstance ().model.isAdmin ()) {
            return getUsers ();
        } else {
            List<User> list = new ArrayList<> ();
            Main.getInstance ().model.setUser (getUserByName (Main.getInstance ().model.getUser ().getUsername ()));
            list.add (Main.getInstance ().model.getUser ());
            return list;
        }
    }

    private User getUserByName (String username) {
        StringBuilder sb = new StringBuilder ();
        sb.append (" select id, username, password, role, locked ")
                .append (" from users ")
                .append (" where username = '")
                .append (username)
                .append ("'");

        String sql = sb.toString ();

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){

            User user = null;
            while (rs.next()) {
                user = new User(rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("role"),
                        rs.getInt ("locked"));
            }

            return user;
        } catch (Exception ex) {
            Controller.Logger.log ("database access error", "user cannot be retrieved");
        }

        return null;
    }

    public boolean updatePassword (String user, String pw) {
        String hashedPassword = PasswordEncryption.hash (pw);
        StringBuilder sb = new StringBuilder ();
        sb.append ("UPDATE users ")
                .append (" SET password = '").append (hashedPassword).append ("' ")
                .append (" WHERE username = '").append (user).append ("';");

        String sql = sb.toString ();

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()) {
            stmt.execute (sql);
            Logger.log ("password change", user + " changed password");
            Logger.dblog ("password change", Main.getInstance ().model.getUser ().getUsername (), "changed password");
            return true;
        } catch (Exception e) {
            Logger.log ("password change error", "failed to change password " + user);
        }
        return false;
    }

    public void lockUser (String username, int value) {
        StringBuilder sb = new StringBuilder ();
        sb.append ("UPDATE users ")
                .append (" SET locked = ").append (value)
                .append (" WHERE username = '").append (username).append ("'")
                .append (";");

        String sql = sb.toString ();

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()) {
            stmt.execute (sql);
            Logger.log ("user lock", (value == 0 ? "unlocked ": "locked ") + username);
            Logger.dblog ("user lock", Main.getInstance ().model.getUser ().getUsername (), (value == 0 ? "unlocked ": "locked ") + username);
        } catch (Exception e) {
            Logger.log ("database error", "failed to lock/unlock user");
        }

    }
}