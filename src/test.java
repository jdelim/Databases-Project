import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class test {

    public static void main(String[] args) {

        try {
            System.out.println("Connecting to database...");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/midterm1", "root", "root");

            Statement statement = connection.createStatement();

            String selectAll = "select * from Menu";

            String query1 = "SELECT drink FROM Orders WHERE is_member = 'False' AND drink not in (" +
                    "SELECT drink FROM Orders WHERE is_member = 'True')";

            ResultSet resultSet = statement.executeQuery(selectAll);

            ResultSet newResult = statement.executeQuery(query1);

            while (newResult.next()) {
                //System.out.println(resultSet.getString(1));
                System.out.println(newResult.getString(1));
            }
            //resultSet.close();
            statement.close();
            System.out.println("Success!");
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
       }
}
