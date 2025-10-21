
package test.sec.db.tut.ac.za;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

public class TestingTheSecondFriendsDB extends JFrame{

    private JPanel namePn1;
    private JPanel surnamePn1;
    private JPanel salaryPn1;
    private JPanel nameSurnameSalaryPn1;
    private JPanel buttonPn1;
    private JPanel displayPn1;
    private JPanel mainPn1;
    
    private JLabel nameL1;
    private JLabel surnameL1;
    private JLabel salaryL1;
    
    private JTextField nameTxtFld;
    private JTextField surnameTxtFld;
    private JTextField salaryTxtFld;
    
    private JButton addBtn ;
    private JButton updateBtn ;
    private JButton searchBtn;
    private JButton deleteBtn;
    private JButton displayBtn;
    
    private JTextArea displayTxtArea;
    
    public TestingTheSecondFriendsDB() {
        
        setTitle("My friends");
        setSize(500, 600);
        
        //ceate panels
        nameSurnameSalaryPn1 = new JPanel(new GridLayout(3 , 1));
        displayPn1 = new JPanel(new FlowLayout());
        displayPn1.setBorder(new TitledBorder(new LineBorder(Color.BLACK , 1) , "Displaying Friends"));
        
        buttonPn1 = new JPanel(new FlowLayout());
        mainPn1 = new JPanel(new BorderLayout());
        
        namePn1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        surnamePn1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        salaryPn1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        //creating the label
        nameL1 = new JLabel("Name:  ");
        surnameL1 = new JLabel("Surname:  ");
        salaryL1 = new JLabel("Salary:  ");
        
        //creating the text fields
        nameTxtFld = new JTextField(25);
        surnameTxtFld = new JTextField(25);
        salaryTxtFld = new JTextField(25);
        
        //creating buttons
        addBtn = new JButton("Add");
        updateBtn = new JButton("Update");
        searchBtn = new JButton("Search");
        deleteBtn = new JButton("Delete");
        displayBtn = new JButton("Display");
        
        //create the textarea
        displayTxtArea = new JTextArea(25 , 40);
        displayTxtArea.setEditable(false);
         
        //adding to the name panel
        namePn1.add(nameL1);
        namePn1.add(nameTxtFld);
        
        //adding to surname panel
        surnamePn1.add(surnameL1);
        surnamePn1.add(surnameTxtFld);
        
        //adding to salary panel
        salaryPn1.add(salaryL1);
        salaryPn1.add(salaryTxtFld);
        
        //adding to display panel
        displayPn1.add(displayTxtArea);
        //disSrcPan = new JScrollPane(displayPn1 , JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS , JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        //adding to the button panel
        buttonPn1.add(addBtn);
        buttonPn1.add(updateBtn);
        buttonPn1.add(searchBtn);
        buttonPn1.add(deleteBtn);
        buttonPn1.add(displayBtn);
        
        //adding to the namesurnamesalary panel
        nameSurnameSalaryPn1.add(namePn1);
        nameSurnameSalaryPn1.add(surnamePn1);
        nameSurnameSalaryPn1.add(salaryPn1);
        
        //adding to main
        mainPn1.add(nameSurnameSalaryPn1 , BorderLayout.NORTH);
        mainPn1.add(buttonPn1 , BorderLayout.CENTER);
        mainPn1.add(displayPn1 , BorderLayout.SOUTH);
        
        //adding main
        add(mainPn1);
        
       setVisible(true);
       
               // Button Actions
        addBtn.addActionListener(e -> addFriend());
        updateBtn.addActionListener(e -> updateFriend());
        searchBtn.addActionListener(e -> searchFriend());
        deleteBtn.addActionListener(e -> deleteFriend());
        displayBtn.addActionListener(e -> displayFriends());
    }
    
        private Connection connectToDB() {
        try {
            String url = "jdbc:derby://localhost:1527/SecondFriendDB";
            String user = "app";
            String password = "app";
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            displayTxtArea.setText("Database connection failed: " + e.getMessage());
            return null;
        }
    }
        
            private void addFriend() {
                
        try (Connection conn = connectToDB()) {
            String sql = "INSERT INTO FRIENDDB (NAME, SURNAME, SALARY) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nameTxtFld.getText());
            ps.setString(2, surnameTxtFld.getText());
            ps.setDouble(3, Double.parseDouble(salaryTxtFld.getText()));
            ps.executeUpdate();
            displayTxtArea.setText("Friend added successfully.");
        } catch (Exception ex) {
            displayTxtArea.setText("Error adding friend: " + ex.getMessage());
        }
    }
            
            private void updateFriend() {
        try (Connection conn = connectToDB()) {
            String sql = "UPDATE FRIENDDB SET SALARY = ? WHERE NAME = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, Double.parseDouble(salaryTxtFld.getText()));
            ps.setString(2, nameTxtFld.getText());
            int rows = ps.executeUpdate();
            displayTxtArea.setText(rows > 0 ? "Friend updated." : "Friend not found.");
        } catch (Exception ex) {
            displayTxtArea.setText("Error updating friend: " + ex.getMessage());
        }
    }  
     
          private void searchFriend() {
        try (Connection conn = connectToDB()) {
            String sql = "SELECT * FROM FRIENDDB WHERE NAME = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nameTxtFld.getText());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                surnameTxtFld.setText(rs.getString("SURNAME"));
                salaryTxtFld.setText(String.valueOf(rs.getDouble("SALARY")));
                displayTxtArea.setText("Friend found.");
            } else {
                displayTxtArea.setText("Friend not found.");
            }
        } catch (Exception ex) {
            displayTxtArea.setText("Error searching friend: " + ex.getMessage());
        }
    }   
              private void deleteFriend() {
        try (Connection conn = connectToDB()) {
            String sql = "DELETE FROM FRIENDDB WHERE NAME = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nameTxtFld.getText());
            int rows = ps.executeUpdate();
            displayTxtArea.setText(rows > 0 ? "Friend deleted." : "Friend not found.");
        } catch (Exception ex) {
            displayTxtArea.setText("Error deleting friend: " + ex.getMessage());
        }
    }
               private void displayFriends() {
        try (Connection conn = connectToDB()) {
            String sql = "SELECT * FROM FRIENDDB";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("Name: ").append(rs.getString("NAME"))
                  .append(", Surname: ").append(rs.getString("SURNAME"))
                  .append(", Salary: ").append(rs.getDouble("SALARY"))
                  .append("\n");
            }
            displayTxtArea.setText(sb.toString());
        } catch (Exception ex) {
            displayTxtArea.setText("Error displaying friends: " + ex.getMessage());
        }
    }   
              
}
