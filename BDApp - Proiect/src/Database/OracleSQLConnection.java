/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.CallableStatement;
import java.util.*; 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author budea
 */
public class OracleSQLConnection implements DatabaseConnection {
    private Connection con;
    
    private final static String DBURL = "jdbc:oracle:thin:@localhost:1521:XE";
    private final static String DBUSER = "radu";
    private final static String DBPASS = "1991129";
    
    public OracleSQLConnection(){  
        try{    
            // Load Oracle JDBC Driver
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }
    
    @Override
    public boolean connect() {
        try{    
            // Connect to Oracle Database
            if(con == null) {
                con = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
            }
            return con != null;

        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public void closeConnection() {
        try{   
            if(con != null){
                con.close();
                con = null;
            }
        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    public boolean checkIfAdminExists(String id_admin, String admin_password){
        
        boolean ret_val = false;
        
        connect(); 
        
        try{    
            Statement statement = con.createStatement();
            
            String query = "SELECT id_admin, parola_admin FROM Conturi_admin WHERE " +
                    "id_admin=" + id_admin + "";
            
            
            ResultSet rs = statement.executeQuery(query);

            if(rs.next()) {  
                int id = rs.getInt("id_admin");
                String password_admin = rs.getString("parola_admin");
                
                if(password_admin == null ? PasswordEncrypt.getPlainTextPassword(password_admin) == null : admin_password.equals(PasswordEncrypt.getPlainTextPassword(password_admin))){
                    ret_val = true; 
                    
                    System.out.println(id + " " + PasswordEncrypt.getPlainTextPassword(password_admin));
                }
                else{
                    ret_val = false;
                    JOptionPane.showMessageDialog(null,"Parola introdusa este incorecta!","Eroare",JOptionPane.WARNING_MESSAGE);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Contul asociat adminului cu ID-ul " + id_admin + " nu exista!","Eroare",JOptionPane.WARNING_MESSAGE);
                ret_val = false;
            }
            
            rs.close();
            statement.close();
            
            closeConnection();
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
            
            closeConnection();
            return false;
        }
        
        return ret_val;
    }
    
    
    
    public boolean checkIfUserExists(String user_id, String user_password){
        
        boolean ret_val = false;
        
        connect(); 
        
        try{    
            Statement statement = con.createStatement();
            
            String query = "SELECT clienti_cod_client, parola_client FROM Conturi_clienti WHERE " +
                    "clienti_cod_client=" + user_id;
            
            
            ResultSet rs = statement.executeQuery(query);

            if(rs.next()) {  
                int id = rs.getInt("clienti_cod_client");
                String user_password_crypted = rs.getString("parola_client");
                
                if(user_password_crypted == null ? PasswordEncrypt.getPlainTextPassword(user_password_crypted) == null : user_password.equals(PasswordEncrypt.getPlainTextPassword(user_password_crypted))){
                    ret_val = true;                         
                }
                else{
                    ret_val = false;
                    JOptionPane.showMessageDialog(null,"Parola introdusa este incorecta!","Eroare",JOptionPane.WARNING_MESSAGE);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Contul asociat clientului cu ID-ul " + user_id + " nu exista!","Eroare",JOptionPane.WARNING_MESSAGE);
                ret_val = false;
            }
            
            rs.close();
            statement.close();
            
            closeConnection();
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta verificare unui cont din baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);
            e.printStackTrace();
            closeConnection();
            return false;
        }
        
        return ret_val;
    }
    
     
    public boolean userExists(String user_id){
        
        boolean ret_val = false;
        
        connect(); 
        
        try{    
            Statement statement = con.createStatement();
            
            String query = "SELECT * FROM Clienti WHERE " +
                    "cod_client=" + user_id;
            
            
            ResultSet rs = statement.executeQuery(query);

            if(rs.next()) {  
                ret_val = true;
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Clientul cu ID-ul " + user_id + " nu exista!","Eroare",JOptionPane.WARNING_MESSAGE);
                ret_val = false;
            }
            
            rs.close();
            statement.close();
            
            closeConnection();
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta verificare unui cont din baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);
            e.printStackTrace();
            closeConnection();
            return false;
        }
        
        return ret_val;
    }
    
    public List<String> getAllUsersFromDB(){
        connect();
        
        List<String> users = new ArrayList<>();
        
        try{ 
            Statement statement = con.createStatement();
            
            String query = "SELECT * FROM Clienti";
            
            ResultSet rs = statement.executeQuery(query);
            
            String new_list_val = "";
            
            while(rs.next()) {  
                new_list_val += rs.getInt("cod_client") + "  ";
                new_list_val += rs.getString("nume") + "  ";
                new_list_val += rs.getLong("CNP") + "  ";
                new_list_val += rs.getString("telefon") + "  ";
                new_list_val += rs.getString("email");

                users.add(new_list_val);
                
                new_list_val = "";
            }
            
            rs.close();
            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la citirea clientilor din baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);

            closeConnection();
        }
        
        return users;
    }

    
    public void insertNewUser(String name, long cnp, String phone, String email){
        connect();
        
        try{    
            Statement statement = con.createStatement();
            
            String query = "INSERT INTO Clienti (nume,cnp,telefon,email) VALUES ('";
            query += name + "'," + Long.toString(cnp) + ",'" + phone + "','" + email + "')";
            
            statement.executeUpdate(query);
            statement.executeUpdate("COMMIT");
            
            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            
            switch(e.getErrorCode())
            {
                case 1:
                {
                    JOptionPane.showMessageDialog(null,"Ati inserat un CNP care deja se afla in baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);
                    break;
                }
                default:
                    JOptionPane.showMessageDialog(null,"Eroare necunoscuta la inserarea unui client in baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);
            }
            closeConnection();
        }
    }
    
     public void deleteUser(int user_id){
        connect();
        try{    
            Statement statement = con.createStatement();
            
            String query = "DELETE FROM Clienti WHERE cod_client=" + String.valueOf(user_id);
           
            statement.executeUpdate(query);
            statement.executeUpdate("COMMIT");

            
            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la stergerea unui client din baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);

            closeConnection();
        }
    }
     
     public void updateUser(long user_id, String name, long cnp, String phone, String email){
        connect();
        try{    
            Statement statement = con.createStatement();
            
            String query = "UPDATE Clienti SET ";
            
            boolean update_is_valid = false;
            
            if(name != null){
                query += "nume='" + name + "'" + ",";
                update_is_valid = true;
            }
            
            if(cnp != -1){
                query += "cnp=" + cnp  + ",";
                update_is_valid = true;
            }
            
            if(phone != null){
                query += "telefon='" + phone + "'"  + ",";
                update_is_valid = true;
            }
     
            if(email != "" && email != null){
                query += "email='" + email + "'" + ",";;
                update_is_valid = true;
            }            
            
            if(update_is_valid){
                if(query.endsWith(","))
                {
                    query = query.substring(0,query.length() - 1); 
                }

                query += " WHERE cod_client=" + String.valueOf(user_id);

                System.out.println(query);

                statement.executeUpdate(query);
                statement.executeUpdate("COMMIT");

            }else{
                JOptionPane.showMessageDialog(null,"Nu se poate actualiza clientul cu valori nule!!","Eroare",JOptionPane.WARNING_MESSAGE);

            }

            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la actualizarea unui client in baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);

            closeConnection();
        }
    }


    public List<String> getAllMetersFromDB(){
        connect();

        List<String> meters = new ArrayList<>();

        try{ 
            Statement statement = con.createStatement();

            String query = "SELECT * FROM Contoare";

            ResultSet rs = statement.executeQuery(query);

            String new_list_val = "";

            while(rs.next()) {  
                new_list_val += rs.getInt("id_contor") + "  ";
                new_list_val += rs.getString("index_real") + "  ";
                new_list_val += rs.getLong("index_virtual") + "  ";
                new_list_val += rs.getString("data_instalare") + "  ";
                new_list_val += rs.getString("data_ultima_citire") + "  ";
                new_list_val += rs.getBoolean("index_actualizat_de_client") + "  ";
                new_list_val += rs.getInt("cod_contract");

                meters.add(new_list_val);

                new_list_val = "";
            }

            rs.close();
            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la citirea contoarelor din baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);

            closeConnection();
        }

        return meters;
    }
    
    public void insertNewMeter(String install_date, int contract_code){
        connect();
        
        try{    
            Statement statement = con.createStatement();
            
            String query = "INSERT INTO Contoare VALUES (NULL, 0, 0, TO_DATE('";
            
            if(install_date != null)
            {
                query += install_date.toString() + "', 'YYYY-MM-DD'), NULL,0," + contract_code + ")";
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Data start invalida la inserarea unui nou contor!","Eroare",JOptionPane.WARNING_MESSAGE);
            }
            
            System.out.println(query);
            statement.executeUpdate(query);
            statement.executeUpdate("COMMIT");
            
            statement.close();
            closeConnection();
        }catch(SQLException e){
            if(e.getErrorCode() == 1)
                JOptionPane.showMessageDialog(null,"Clientul selectat are deja un contor instalat!","Eroare",JOptionPane.WARNING_MESSAGE);
            else
                JOptionPane.showMessageDialog(null,"Eroare necunoscuta la inserarea unui client in baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);

            closeConnection();
        }
    }
    
    
    
    public List<String> getAllContractsFromDB(){
        connect();

        List<String> contracts = new ArrayList<>();

        try{ 
            Statement statement = con.createStatement();

            String query = "SELECT * FROM Contracte";

            ResultSet rs = statement.executeQuery(query);

            String new_list_val = "";

            while(rs.next()) {  
                new_list_val += rs.getInt("cod_contract") + "  ";
                new_list_val += rs.getString("adresa_contract") + "  ";
                new_list_val += rs.getInt("cod_postal") + "  ";
                new_list_val += rs.getLong("cod_client") + "  ";
                new_list_val += rs.getString("data_start") + "  ";
                new_list_val += rs.getInt("id_serviciu") + "  ";

                contracts.add(new_list_val);

                new_list_val = "";
            }

            rs.close();
            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la citirea contractelor din baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);

            closeConnection();
        }

        return contracts;
    }
    
    
        public List<String> getAllServiceTypes(){
        connect();

        List<String> services = new ArrayList<>();

        try{ 
            Statement statement = con.createStatement();

            String query = "SELECT * FROM tip_serviciu";

            ResultSet rs = statement.executeQuery(query);

            String new_list_val = "";

            while(rs.next()) {  
                new_list_val += rs.getInt("id_serviciu") + "  ";
                new_list_val += rs.getString("nume_serviciu") + "  ";
                new_list_val += rs.getFloat("pret_per_kilowatt");

                services.add(new_list_val);

                new_list_val = "";
            }

            rs.close();
            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la citirea tipurilor de servicii din baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);

            closeConnection();
        }

        return services;
    }
        
    
    public void insertNewContract( String contract_address, int postal_code, long client_code, String start_date, int service_id) throws ParseException{
        connect();
        
        try{    
            Statement statement = con.createStatement();
            
            String query = "INSERT INTO Contracte (adresa_contract,cod_postal,cod_client,data_start,id_serviciu) VALUES ('";
            query +=  contract_address + "'," + postal_code + "," + client_code + ",";
            if(start_date != null)
            {
                query += "TO_DATE('" + start_date + "', 'YYYY-MM-DD')," + service_id + ")";
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Data start invalida la inserarea unui nou contract!","Eroare",JOptionPane.WARNING_MESSAGE);
            }
            
            System.out.println(query);
            
            
            statement.executeUpdate(query);
            statement.executeUpdate("COMMIT");
            
            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la inserarea unui client in baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);

            closeConnection();
        }
    }
     
    public void updateContract(long contract_code, String adress, int postal_code, long client_code, String start_date, int service_id){
        connect();
        try{    
            Statement statement = con.createStatement();
            
            String query = "UPDATE Contracte SET ";
            
            boolean update_is_valid = false;
            
            if(adress != null){
                query += "adresa_contract='" + adress + "'" + ",";
                update_is_valid = true;
            }
            
            if(postal_code != -1){
                query += "cod_postal=" + postal_code  + ",";
                update_is_valid = true;
            }
            
            if(client_code != -1){
                query += "cod_client=" + client_code + ",";
                update_is_valid = true;
            }
     
            if(start_date != "" && start_date != null){
                query += "data_start=" + "TO_DATE('" + start_date + "', 'YYYY-MM-DD'),";
                update_is_valid = true;
            }   
            
            if(service_id != -1){
                query += "id_serviciu=" + service_id;
                update_is_valid = true;
            }
     
            
            if(update_is_valid){
                if(query.endsWith(","))
                {
                    query = query.substring(0,query.length() - 1); 
                }

                query += " WHERE cod_contract=" + String.valueOf(contract_code);

                System.out.println(query);

                statement.executeUpdate(query);
                statement.executeUpdate("COMMIT");
            }else{
                JOptionPane.showMessageDialog(null,"Nu se poate actualiza contractul cu valori nule!!","Eroare",JOptionPane.WARNING_MESSAGE);

            }

            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la actualizarea unui client in baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);

            closeConnection();
        }
    }
    
    
    public void deleteContract(long contract_id){
        connect();
        try{    
            Statement statement = con.createStatement();
            
            String query = "DELETE FROM Contracte WHERE cod_contract=" + String.valueOf(contract_id);
           
            statement.executeUpdate(query);
            statement.executeUpdate("COMMIT");
            
            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la stergerea unui contract din baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);

            closeConnection();
        }
    }
   
        public List<String> getAllBillFromDB(){
        connect();

        List<String> services = new ArrayList<>();

        try{ 
            Statement statement = con.createStatement();

            String query = "SELECT * FROM Facturi ORDER BY numar_factura";

            ResultSet rs = statement.executeQuery(query);

            String new_list_val = "";

            while(rs.next()) {  
                new_list_val += rs.getLong("numar_factura") + "  ";
                new_list_val += rs.getLong("index_vechi") + "  ";
                new_list_val += rs.getLong("index_nou")+ "  ";
                new_list_val += rs.getString("data_factura")+ "  ";
                new_list_val += rs.getLong("cod_contract")+ "  ";
                new_list_val += rs.getString("tip_factura")+ "  ";
                new_list_val += rs.getFloat("suma_in_avans");

                services.add(new_list_val);

                new_list_val = "";
            }

            rs.close();
            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la citirea facturilor din baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);

            closeConnection();
        }

        return services;
    }
        
    public void generateBill(){
        String command ="{call generare_facturi}";

        try {
            
            connect();
            
            CallableStatement cstmt =  con.prepareCall(command);
            
            cstmt.execute();
            
            cstmt.close();
            closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(OracleSQLConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void insertNewService(String service_name, float price ) throws ParseException{
        connect();
        
        try{    
            Statement statement = con.createStatement();
            
            String query = "INSERT INTO tip_serviciu (nume_serviciu,pret_per_kilowatt) VALUES ('";
            if(!service_name.equals(""))
            {
                query += service_name + "',";
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Nume serviciu invalid!!","Eroare",JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if(price > 0)
            {
                query += price + ")";
            }
            else
            {                
                JOptionPane.showMessageDialog(null,"Pret per kw invalid!","Eroare",JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            System.out.println(query);
            
            statement.executeUpdate(query);
            statement.executeUpdate("COMMIT");
            
            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la inserarea uui nou serviciu in baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);

            closeConnection();
        }
    }    
    
     public void updateServices(int service_id, String service_name, float price){
        connect();
        try{    
            Statement statement = con.createStatement();
            
            String query = "UPDATE tip_serviciu SET ";
            
            boolean update_is_valid = false;
            
            if(service_name != null){
                query += "nume_serviciu='" + service_name + "'" + ",";
                update_is_valid = true;
            }
            
            if(price != -1){
                query += "pret_per_kilowatt=" + price;
                update_is_valid = true;

            }
     
            
            if(update_is_valid){
                if(query.endsWith(","))
                {
                    query = query.substring(0,query.length() - 1); 
                }

                query += " WHERE id_serviciu=" + String.valueOf(service_id);

                System.out.println(query);

                statement.executeUpdate(query);
                statement.executeUpdate("COMMIT");
            }else{
                JOptionPane.showMessageDialog(null,"Nu se poate actualiza serviciul cu valori nule!","Eroare",JOptionPane.WARNING_MESSAGE);
            }

            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la actualizarea unui serviciu in baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);

            closeConnection();
        }
    }    
     
         
    public void deleteService(int service_id){
        connect();
        try{    
            Statement statement = con.createStatement();
            
            String query = "DELETE FROM tip_serviciu WHERE id_serviciu=" + String.valueOf(service_id);
           
            statement.executeUpdate(query);
            statement.executeUpdate("COMMIT");
            
            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la stergerea unui serviciu din baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);

            closeConnection();
        }
    }
    

    public List<String> getAllEmployees(){
        connect();

        List<String> employees = new ArrayList<>();

        try{ 
            Statement statement = con.createStatement();

            String query = "SELECT * FROM Angajati";

            ResultSet rs = statement.executeQuery(query);

            String new_list_val = "";

            while(rs.next()) {  
                new_list_val += rs.getLong("id_angajat") + "  ";
                new_list_val += rs.getString("nume_angajat");

                employees.add(new_list_val);

                new_list_val = "";
            }

            rs.close();
            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la citirea angajatilor din baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);

            closeConnection();
        }

        return employees;
    }
        
    public void updateEmployee(int employee_id, String employee_name){
        connect();
        try{    
            Statement statement = con.createStatement();
            
            String query = "UPDATE Angajati SET ";
            
            boolean update_is_valid = false;
            
            if(employee_name != null){
                query += "nume_angajat='" + employee_name + "'" ;
                update_is_valid = true;
            }
            
            if(update_is_valid){
                if(query.endsWith(","))
                {
                    query = query.substring(0,query.length() - 1); 
                }

                query += " WHERE id_angajat=" + String.valueOf(employee_id);

                System.out.println(query);

                statement.executeUpdate(query);
                statement.executeUpdate("COMMIT");
            }else{
                JOptionPane.showMessageDialog(null,"Nu se poate actualiza serviciul cu valori nule!","Eroare",JOptionPane.WARNING_MESSAGE);
            }

            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la actualizarea unui serviciu in baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);

            closeConnection();
        }
    }  
    
        public void deleteEmployee(int employee_id){
        connect();
        try{    
            Statement statement = con.createStatement();
            
            String query = "DELETE FROM Angajati WHERE id_angajat=" + String.valueOf(employee_id);
           
            statement.executeUpdate(query);
            statement.executeUpdate("COMMIT");
            
            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la stergerea unui angajat din baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);

            closeConnection();
        }
    }
    
    public List<String> getAllMeterReadings(){
        connect();

        List<String> readings = new ArrayList<>();

        try{ 
            Statement statement = con.createStatement();

            String query = "SELECT contoare_angajati.contoare_id_contor, "
                    + "contoare_angajati.angajati_id_angajat, Angajati.nume_angajat,"
                    + " contoare_angajati.data_citirii FROM contoare_angajati,"
                    + " Angajati WHERE Angajati.nume_angajat in "
                    + "(SELECT nume_angajat from Angajati WHERE contoare_angajati.angajati_id_angajat = angajati.id_angajat) "
                    + "ORDER BY contoare_angajati.contoare_id_contor";

            ResultSet rs = statement.executeQuery(query);

            String new_list_val = "";

            while(rs.next()) {  
                new_list_val += rs.getLong("contoare_id_contor") + "  ";
                new_list_val += rs.getLong("angajati_id_angajat") + "  ";
                new_list_val += rs.getString("nume_angajat") + "  ";
                new_list_val += rs.getString("data_citirii");

                readings.add(new_list_val);

                new_list_val = "";
            }

            rs.close();
            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la citirea contoare_angajati din baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);

            closeConnection();
        }

        return readings;
    }
   
    public void insertNewMeterReading(int employee_id, long meter_id, long index){
        String command ="{call citire_contor(?,?,?)}";

        try {
            
            connect();
            
            CallableStatement cstmt =  con.prepareCall(command);
            
            cstmt.setInt(1, employee_id);
            cstmt.setLong(2, meter_id);
            cstmt.setLong(3, index);

            cstmt.execute();
            
            cstmt.close();
            closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(OracleSQLConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
    public List<String> getAllBilsByUser(long user_id){
        connect();

        List<String> bils = new ArrayList<>();

        try{ 
            Statement statement = con.createStatement();

            String query = "SELECT clienti.nume, clienti.cod_client,contracte.cod_contract,contracte.data_start, facturi.numar_factura,\n" +
                        "facturi.index_vechi,(facturi.index_nou-facturi.index_vechi) as kw_consumati,facturi.index_nou, facturi.data_factura, facturi.data_factura + 14 as data_scadenta,\n" +
                        "(facturi.index_nou-facturi.index_vechi)*(SELECT pret_per_kilowatt FROM tip_serviciu WHERE contracte.id_serviciu=tip_serviciu.id_serviciu) as suma_fara_TVA,\n" +
                        "0.19*(facturi.index_nou-facturi.index_vechi)*(SELECT pret_per_kilowatt FROM tip_serviciu WHERE contracte.id_serviciu=tip_serviciu.id_serviciu) as TVA,\n" +
                        "(facturi.index_nou-facturi.index_vechi)*(SELECT pret_per_kilowatt FROM tip_serviciu WHERE contracte.id_serviciu=tip_serviciu.id_serviciu) + \n" +
                        "0.19*(facturi.index_nou-facturi.index_vechi)*(SELECT pret_per_kilowatt FROM tip_serviciu WHERE contracte.id_serviciu=tip_serviciu.id_serviciu) as SUMA_CONSUM," +
                        "facturi.suma_in_avans,(facturi.index_nou-facturi.index_vechi)*(SELECT pret_per_kilowatt FROM tip_serviciu WHERE contracte.id_serviciu=tip_serviciu.id_serviciu) + \n" +
                        "0.19*(facturi.index_nou-facturi.index_vechi)*(SELECT pret_per_kilowatt FROM tip_serviciu WHERE contracte.id_serviciu=tip_serviciu.id_serviciu) - facturi.suma_in_avans as total_de_plata," +
                        "(SELECT nume_serviciu FROM tip_serviciu WHERE contracte.id_serviciu=tip_serviciu.id_serviciu) as nume_serviciu,\n" +
                        "(SELECT pret_per_kilowatt FROM tip_serviciu WHERE contracte.id_serviciu=tip_serviciu.id_serviciu) as pret_per_kw,\n" +
                        "facturi.tip_factura\n"+
                        "FROM contracte, clienti, facturi WHERE \n" +
                        "clienti.cod_client = contracte.cod_client AND contracte.cod_contract = facturi.cod_contract AND clienti.cod_client = ";
            query += user_id;
            
            query += " ORDER BY numar_factura";
            ResultSet rs = statement.executeQuery(query);

            String new_list_val = "";

            while(rs.next()) {  
                new_list_val += rs.getString("nume") + "  ";
                new_list_val += rs.getLong("cod_client") + "  ";
                new_list_val += rs.getLong("cod_contract") + "  ";
                new_list_val += rs.getString("data_start") + "  ";
                new_list_val += rs.getLong("numar_factura") + "  ";
                new_list_val += rs.getLong("index_vechi") + "  ";
                new_list_val += rs.getLong("kw_consumati") + "  ";
                new_list_val += rs.getLong("index_nou") + "  ";
                new_list_val += rs.getString("data_factura") + "  ";
                new_list_val += rs.getString("data_scadenta") + "  ";
                new_list_val += rs.getFloat("suma_fara_tva") + "  ";
                new_list_val += rs.getFloat("tva") + "  ";
                new_list_val += rs.getFloat("suma_consum") + "  ";
                new_list_val += rs.getFloat("suma_in_avans") + "  ";
                new_list_val += rs.getFloat("total_de_plata") + "  ";
                new_list_val += rs.getString("nume_serviciu") + "  ";
                new_list_val += rs.getFloat("pret_per_kw") + "  ";
                new_list_val += rs.getString("tip_factura");

                bils.add(new_list_val);

                new_list_val = "";
            }

            rs.close();
            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la citirea facturilor din baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);

            closeConnection();
        }

        return bils;
      }
      
    public boolean createNewUserAccount(long user_id, String password){
        connect();

        try{ 
            Statement statement = con.createStatement();

            String query = "INSERT INTO Conturi_clienti VALUES(";
            
            if(user_id > -1)
            {
                query += user_id + ",'";
            }
            else
            {
                JOptionPane.showMessageDialog(null,"ID-ul clientului este invalid!","Eroare",JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
            if(!password.equals(""))
            {
                query += PasswordEncrypt.getEncryptedPassword(password) + "')";
            }
            else
            {                
                JOptionPane.showMessageDialog(null,"Parola invalida!!","Eroare",JOptionPane.WARNING_MESSAGE);
                return false;
            }

            System.out.println(query);
            
            ResultSet rs = statement.executeQuery(query);

            rs.close();
            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            if(e.getErrorCode() == 1)
                JOptionPane.showMessageDialog(null,"Deja exista un cont asociat acestui utilizator!","Eroare",JOptionPane.WARNING_MESSAGE);
            else
                JOptionPane.showMessageDialog(null,"Eroare necunoscuta la creare unui nou cont de utilizator!","Eroare",JOptionPane.WARNING_MESSAGE);
            
            closeConnection();
            
            return false;
        }
        return true;
      }
    
    public String getUser(long user_id){
        connect(); 
        
        String new_list_val = "";

        try{    
            Statement statement = con.createStatement();
            
            String query = "SELECT * FROM Clienti WHERE cod_client=" + user_id;  
            
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()) {  
                new_list_val += rs.getInt("cod_client") + "  ";
                new_list_val += rs.getString("nume") + "  ";
                new_list_val += rs.getLong("CNP") + "  ";
                new_list_val += rs.getString("telefon") + "  ";
                new_list_val += rs.getString("email");
            }

            rs.close();
            statement.close();
            
            closeConnection();     
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la citirea unui client din baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);
            e.printStackTrace();
            closeConnection();
        }
        
        return new_list_val;
    }
    
        
    public List<String> getAllMeterByUser(long user_id){
        connect();

        List<String> meters = new ArrayList<>();

        try{ 
            Statement statement = con.createStatement();

            String query = "SELECT id_contor, data_instalare, Contoare.cod_contract, "
                    + "adresa_contract, cod_postal from Contoare LEFT JOIN Contracte"
                    + " ON contracte.cod_contract = contoare.cod_contract WHERE contracte.cod_client = ";
            query += user_id;

            ResultSet rs = statement.executeQuery(query);

            String new_list_val = "";

            while(rs.next()) {  
                new_list_val += rs.getLong("id_contor") + "  ";
                new_list_val += rs.getString("data_instalare") + "  ";
                new_list_val += rs.getLong("cod_contract") + "  ";
                new_list_val += rs.getString("adresa_contract") + "  ";
                new_list_val += rs.getString("cod_postal");

                meters.add(new_list_val);

                new_list_val = "";
            }

            rs.close();
            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la citirea contoarelor din baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);

            closeConnection();
        }

        return meters;
      }
    
    public void updateIndexByUser(long meter_id, long index){
        String command ="{call actualizare_contor_client(?,?)}";

        try {    
            connect();
            
            CallableStatement cstmt =  con.prepareCall(command);
            
            cstmt.setLong(1, meter_id);
            cstmt.setLong(2, index);
            
            cstmt.execute();
            
            cstmt.close();
            closeConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }     
    
    
    public void insertNewEmployee(String employee_name){
        connect();
        
        try{    
            Statement statement = con.createStatement();
            
            String query = "INSERT INTO Angajati (nume_angajat) VALUES ('";
            if(!employee_name.equals(""))
            {
                query += employee_name + "')";
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Nume angajat invalid!","Eroare",JOptionPane.WARNING_MESSAGE);
                return;
            }
           
            
            System.out.println(query);
            
            statement.executeUpdate(query);
            statement.executeUpdate("COMMIT");
            
            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la inserarea uui nou angajat in baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);

            closeConnection();
        } 
    }
    
        public String getEmployee(long employee_id){
        connect(); 
        
        String new_list_val = "";

        try{    
            Statement statement = con.createStatement();
            
            String query = "SELECT * FROM Angajati WHERE id_angajat=" + employee_id;  
            
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()) {  
                new_list_val += rs.getInt("id_angajat") + "  ";
                new_list_val += rs.getString("nume_angajat") + "  ";
            }

            rs.close();
            statement.close();
            
            closeConnection();     
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la citirea unui angajat din baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);
            e.printStackTrace();
            closeConnection();
        }
        
        return new_list_val;
    }
        
    public boolean checkIfEmployeeExists(String employee_id, String employee_password){
        
        boolean ret_val = false;
        
        connect(); 
        
        try{    
            Statement statement = con.createStatement();
            
            String query = "SELECT angajati_id_angajat, parola_angajat FROM Conturi_angajati WHERE " +
                    "angajati_id_angajat=" + employee_id ;
            
            
            ResultSet rs = statement.executeQuery(query);

            if(rs.next()) {  
                int id = rs.getInt("angajati_id_angajat");
                String password_employee = rs.getString("parola_angajat");
                
                if(password_employee == null ? PasswordEncrypt.getPlainTextPassword(password_employee) == null : employee_password.equals(PasswordEncrypt.getPlainTextPassword(password_employee))){
                    ret_val = true;      
                }
                else{
                    ret_val = false;
                    JOptionPane.showMessageDialog(null,"Parola introdusa este incorecta!","Eroare",JOptionPane.WARNING_MESSAGE);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Contul asociat angajatului cu ID-ul " + employee_id + " nu exista!","Eroare",JOptionPane.WARNING_MESSAGE);
                ret_val = false;
            }
            
            rs.close();
            statement.close();
            
            closeConnection();
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare la verificarea existentei unui cont de angajat din baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);
            closeConnection();
            return false;
        }
        
        return ret_val;
    }
    
     public boolean employeeExists(String employee_id){
        
        boolean ret_val = false;
        
        connect(); 
        
        try{    
            Statement statement = con.createStatement();
            
            String query = "SELECT * FROM Angajati WHERE " +
                    "id_angajat=" + employee_id ;
            
            
            ResultSet rs = statement.executeQuery(query);

            if(rs.next()) {  
                ret_val = true;
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Angajatul cu ID-ul " + employee_id + " nu exista!","Eroare",JOptionPane.WARNING_MESSAGE);
                ret_val = false;
            }
            
            rs.close();
            statement.close();
            
            closeConnection();
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare la verificarea existentei unui cont de angajat din baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);
            closeConnection();
            return false;
        }
        
        return ret_val;
    }
    
       
    public boolean createNewEmployeeAcount(long employee_id, String password){
        connect();


        try{ 
            Statement statement = con.createStatement();

            String query = "INSERT INTO Conturi_angajati VALUES(";
            
            if(employee_id > -1)
            {
                query += employee_id + ",'";
            }
            else
            {
                JOptionPane.showMessageDialog(null,"ID-ul angajatului este invalid!","Eroare",JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
            if(!password.equals(""))
            {
                query += PasswordEncrypt.getEncryptedPassword(password) + "')";
            }
            else
            {                
                JOptionPane.showMessageDialog(null,"Parola invalida!!","Eroare",JOptionPane.WARNING_MESSAGE);
                return false;
            }

            System.out.println(query);
            
            ResultSet rs = statement.executeQuery(query);

            rs.close();
            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            if(e.getMessage().equals("ORA-00001: unique constraint (RADU.CONTURI_ANGAJATI_PK) violated\n"))
                JOptionPane.showMessageDialog(null,"Deja exista un cont asociat acestui utilizator!","Eroare",JOptionPane.WARNING_MESSAGE);
            else
                JOptionPane.showMessageDialog(null,"Eroare necunoscuta la creare unui nou cont de angajat!","Eroare",JOptionPane.WARNING_MESSAGE);
            
            closeConnection();
            
            return false;
        }
        return true;
      }
    
       
    public void deleteMeter(int meter_id){
        connect();
        try{    
            Statement statement = con.createStatement();
            
            String query = "DELETE FROM Contoare WHERE id_contor=" + String.valueOf(meter_id);
           
            statement.executeUpdate(query);
            statement.executeUpdate("COMMIT");
            
            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la stergerea unui contor din baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);

            closeConnection();
        }
    }
    
    public List<String> getAllOneYearUser(){
        connect();

        List<String> ret = new ArrayList<>();

        try{ 
            Statement statement = con.createStatement();

            String query = "SELECT clienti.nume, clienti.cod_client,contracte.cod_contract,contracte.data_start, MONTHS_BETWEEN(CURRENT_DATE,contracte.data_start) as Durata_contract FROM contracte, clienti WHERE \n" +
            "MONTHS_BETWEEN(CURRENT_DATE,contracte.data_start) <= 12 AND clienti.cod_client = contracte.cod_client";
     
            
            //query += " ORDER BY numar_factura";
            ResultSet rs = statement.executeQuery(query);

            String new_list_val = "";

            while(rs.next()) {  
                new_list_val += rs.getString("nume") + "  ";
                new_list_val += rs.getLong("cod_client") + "  ";
                new_list_val += rs.getLong("cod_contract") + "  ";
                new_list_val += rs.getString("data_start") + "  ";
                new_list_val += rs.getFloat("durata_contract") + "  ";


                ret.add(new_list_val);

                new_list_val = "";
            }

            rs.close();
            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la citirea clientilor din baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);

            closeConnection();
        }

        return ret;
      }
    
    
    public List<String> getAllUsersWithoutContract(){
        connect();

        List<String> ret = new ArrayList<>();

        try{ 
            Statement statement = con.createStatement();

            String query = "SELECT * FROM clienti cl LEFT JOIN contracte co ON co.cod_client = cl.cod_client WHERE co.cod_client IS NULL";
     
            
            //query += " ORDER BY numar_factura";
            ResultSet rs = statement.executeQuery(query);

            String new_list_val = "";

            while(rs.next()) {  
                new_list_val += rs.getLong("cod_client") + "  ";
                new_list_val += rs.getString("nume") + "  ";
                new_list_val += rs.getLong("CNP") + "  ";
                new_list_val += rs.getString("telefon") + "  ";
                new_list_val += rs.getString("email") + "  ";


                ret.add(new_list_val);

                new_list_val = "";
            }

            rs.close();
            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la citirea clientilor din baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);

            closeConnection();
        }

        return ret;
      }
    
    public List<String> getHowManyContractsByUser(){
        connect();

        List<String> ret = new ArrayList<>();

        try{ 
            Statement statement = con.createStatement();

            String query = "SELECT cl.cod_client, cl.nume, count(co.cod_client) AS \"numar_contracte\" "
                    + "FROM clienti cl LEFT JOIN contracte co ON co.cod_client = cl.cod_client GROUP BY cl.cod_client, cl.nume";
     
            
            //query += " ORDER BY numar_factura";
            ResultSet rs = statement.executeQuery(query);

            String new_list_val = "";

            while(rs.next()) {  
                new_list_val += rs.getLong("cod_client") + "  ";
                new_list_val += rs.getString("nume") + "  ";
                new_list_val += rs.getLong("numar_contracte") + "  ";

                ret.add(new_list_val);

                new_list_val = "";
            }

            rs.close();
            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la citirea clientilor din baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);

            closeConnection();
        }

        return ret;
      }
    
     public List<String> gettAllCreditCardsByUser(int user_id){
        connect();

        List<String> cards = new ArrayList<>();

        try{ 
            Statement statement = con.createStatement();

            String query = "SELECT * FROM Carduri WHERE cod_client=";
            query += user_id;

            ResultSet rs = statement.executeQuery(query);

            String new_list_val = "";

            while(rs.next()) {  
                new_list_val += rs.getInt("id_card") + "  ";
                new_list_val += rs.getLong("nr_card") + "  ";
                new_list_val += rs.getString("data_expirare") + "  ";
                new_list_val += rs.getString("detinator_card");

                cards.add(new_list_val);

                new_list_val = "";
            }

            rs.close();
            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la citirea contoarelor din baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);

            closeConnection();
        }

        return cards;
      }
     
      
    public void insertNewCreditCard(long card_number, int cvv, String expire_date, String card_holder, long client_code){
        connect();
        
        try{    
            Statement statement = con.createStatement();
            
            String query = "INSERT INTO Carduri (nr_card,cvv,data_expirare,detinator_card,cod_client) VALUES (";
            if(card_number > 0)
            {
                query += card_number + ",";
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Numar card invalid!","Eroare",JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if(cvv > 0)
            {
                query += cvv + ",";
            }
            else
            {
                JOptionPane.showMessageDialog(null,"CVV invalid!","Eroare",JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if(!expire_date.equals(""))
            {
                 query += "TO_DATE('" + expire_date + "', 'YYYY-MM-DD'),";
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Data expirare invalida!","Eroare",JOptionPane.WARNING_MESSAGE);
                return;
            }     
            
            
            
            if(!card_holder.equals(""))
            {
                 query += "'" + card_holder + "',";
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Data expirare invalida!","Eroare",JOptionPane.WARNING_MESSAGE);
                return;
            }           
            
            if(client_code > 0)
            {
                query += client_code +")";
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Cod client invalid!","Eroare",JOptionPane.WARNING_MESSAGE);
                return;
            }            
            System.out.println(query);
            
            statement.executeUpdate(query);
            statement.executeUpdate("COMMIT");
            
            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la inserarea uui nou card de credit in baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);

            closeConnection();
        } 
    }
    
    public void deleteCreditCard(int id_card){
        connect();
        try{    
            Statement statement = con.createStatement();
            
            String query = "DELETE FROM Carduri WHERE id_card=" + String.valueOf(id_card);
           
            statement.executeUpdate(query);
            statement.executeUpdate("COMMIT");
            
            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la stergerea unui card din baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);

            closeConnection();
        }
    }
    
    public boolean addBilPayment(long bil_id, int account_id, float amount){
        String command ="{call tranzactie_plata_factura(?,?,?)}";

        try {
            
            connect();
            
            CallableStatement cstmt =  con.prepareCall(command);
            
            cstmt.setLong(1, bil_id);
            cstmt.setLong(2, account_id);
            cstmt.setFloat(3, amount);

            cstmt.execute();
            
            cstmt.close();
            closeConnection();
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(OracleSQLConnection.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,"Eroare la realizarea unei plati de facturi!","Eroare",JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }   
    
    public List<String> gettAllUnpaidBilsByUser(long user_id){
        connect();

        List<String> bils = new ArrayList<>();

        try{ 
            Statement statement = con.createStatement();

            String query = "--vreau sa vad ce facturi nu are platite un anumit client\n" +
            "SELECT clienti.nume, clienti.cod_client,contracte.cod_contract,contracte.data_start, facturi.numar_factura,\n" +
            "facturi.index_vechi,facturi.index_nou, facturi.data_factura FROM contracte, clienti, facturi LEFT JOIN plata_factura ON facturi.numar_factura = plata_factura.numar_factura WHERE \n" +
            "clienti.cod_client = contracte.cod_client AND contracte.cod_contract = facturi.cod_contract AND plata_factura.numar_factura IS NULL AND clienti.cod_client=";
            query += user_id;
            
            query += " ORDER BY numar_factura";
            ResultSet rs = statement.executeQuery(query);

            String new_list_val = "";

            while(rs.next()) {  
                new_list_val += rs.getLong("numar_factura") + "  ";
                new_list_val += rs.getString("nume") + "  ";
                new_list_val += rs.getLong("index_vechi") + "  ";
                new_list_val += rs.getLong("index_nou") + "  ";
                new_list_val += rs.getString("data_factura") ;

                bils.add(new_list_val);

                new_list_val = "";
            }

            rs.close();
            statement.close();
            closeConnection();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"Eroare necunoscuta la citirea facturilor din baza de date!","Eroare",JOptionPane.WARNING_MESSAGE);
            
            closeConnection();
        }

        return bils;
      }
}
