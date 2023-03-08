import java.sql.*;
import java.util.Arrays;

public class FinalProjectQueryJadrielDelim {
    // terminal commands:
    // java -cp mysql-connector-j-8.0.31.jar;. FinalProjectQueryJadrielDelim q1 1990 05
    // java -cp mysql-connector-j-8.0.31.jar;. FinalProjectQueryJadrielDelim q2 1660 35

    public static final String jdbcURL = "jdbc:mysql://localhost:3306/resulttrackerjadrieldelim";
    public static final String username = "root";
    public static final String password = "root";

    public static Connection getConnection (String jdbcURL, String username, String password) throws SQLException {
        System.out.println("Connecting to database...");
        return DriverManager.getConnection(jdbcURL, username, password);
    }

    public static boolean q1Checker(String[] query, Connection conn) throws SQLException {
        // check if a player exists within the database given a year and month
        // query[1] is year, query[2] is month
        String findPlayers = "SELECT * FROM players WHERE year(birthday)=? AND month(birthday)=?";
        PreparedStatement statement = conn.prepareStatement(findPlayers);

        statement.setInt(1, Integer.parseInt(query[1]));
        statement.setInt(2, Integer.parseInt(query[2]));

        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            if (rs.getString(1) != null) {
                System.out.println("Year and month is valid!");
                return true;
            }
        }
        conn.close();
        statement.close();
        return false;
    }

    public static boolean q2Checker(String[] query, Connection conn) throws SQLException {
        // check if playerID and teamID exist within the database, if not return false and print error msg
        // also check if team is disbanded, if it is return false
        // also check if playerID is already part of teamID
        // query[1] is playerID, query[2] is teamID
        // I probably could've combined some of these into 1 query, but I currently do not have the brain power so:

        boolean playerID = false;
        boolean teamID = false;
        boolean notDisbanded = false;
        boolean notCurrentTeam = false;
        String findPlayerID = "SELECT player_id FROM players WHERE player_id=?";

        PreparedStatement statement = conn.prepareStatement(findPlayerID);
        statement.setInt(1, Integer.parseInt(query[1]));
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            if (rs.getString(1) != null) {
                playerID = true;
                break;
            }
        }

        String findTeamID = "SELECT team_id FROM teams WHERE team_id=?";
        statement = conn.prepareStatement(findTeamID);
        statement.setInt(1,Integer.parseInt(query[2]));
        rs = statement.executeQuery();

        while (rs.next()) {
            if (rs.getString(1) != null) {
                teamID = true;
                break;
            }
        }

        String checkDisbanded = "SELECT team_id FROM teams WHERE disbanded IS NULL AND team_id=?";
        statement = conn.prepareStatement(checkDisbanded);
        statement.setInt(1, Integer.parseInt(query[2]));
        rs = statement.executeQuery();
        while (rs.next()) {
            if (rs.getString(1) != null) {
                notDisbanded = true;
                break;
            }
        }

        String checkCurrentTeam = "SELECT team_id FROM Members WHERE player_id = ? AND end_date IS NULL";
        statement = conn.prepareStatement(checkCurrentTeam);
        statement.setInt(1, Integer.parseInt(query[1]));
        rs = statement.executeQuery();
        while (rs.next()) {
            if (!rs.getString(1).equals(query[2])) {
                notCurrentTeam = true;
                break;
            }
        }

        if (!playerID) {
            System.out.println("PlayerID does not exist!");
            return false;
        }
        if (!teamID) {
            System.out.println("TeamID does not exist!");
            return false;
        }
        if (!notDisbanded) {
            System.out.println("Cannot add player to a disbanded team!");
            return false;
        }
        if (!notCurrentTeam) {
            System.out.println("Player is already part of this team, no changes made.");
            return false;
        }
        return true;
    }

    public static void q1(String[] query, Connection conn) throws SQLException {
        // query[1] = year, query[2] = month
        String query1 = "SELECT real_name, tag, nationality, count(tournament_id) AS 'Number of Match Wins' FROM players, matches\n" +
                "WHERE year(birthday) = ? AND month(birthday) = ?\n" +
                "AND ((matches.playerA_id = player_id AND playerA_score > playerB_score)\n" +
                "OR (matches.playerB_id = player_id AND playerB_score > playerA_score))\n" +
                "GROUP BY real_name";

        PreparedStatement statement = conn.prepareStatement(query1);
        statement.setInt(1, Integer.parseInt(query[1]));
        statement.setInt(2, Integer.parseInt(query[2]));

        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            String c1 = rs.getString(1);
            String c2 = rs.getString(2);
            String c3 = rs.getString(3);
            String c4 = rs.getString(4);
            System.out.format("\n%s, %s, %s, %s", c1, c2, c3, c4);
        }
    }

    public static void q2(String[] query, Connection conn) throws SQLException {
        // 1st, update enddate to current date, then insert new row with new teamID and startDate as current date
        // use curdate() for current date
        String updateEndDate = "UPDATE Members SET end_date = curdate() WHERE player_id = ? AND end_date IS NULL";
        String insertRow = "INSERT INTO Members(player_id, team_id, start_date) VALUES (?, ?, curdate())";

        PreparedStatement statement = conn.prepareStatement(updateEndDate);
        statement.setInt(1, Integer.parseInt(query[1]));
        statement.executeUpdate();

        statement = conn.prepareStatement(insertRow);
        statement.setInt(1,Integer.parseInt(query[1]));
        statement.setInt(2,Integer.parseInt(query[2]));
        statement.executeUpdate();
    }

    public static void main(String[] args) {
        String[] q1 = new String[3];
        String[] q2 = new String[3];
        boolean executeQ1 = false;
        boolean executeQ2 = false;

        for (int i = 0; i < args.length; i++) {
            if (args[0].equals("q1")) {
                q1[i] = args[i];
                executeQ1 = true;
            }
            else if (args[0].equals("q2")) {
                q2[i] = args[i];
                executeQ2 = true;
            }
            else {
                System.out.println("Please enter 'q1' or 'q2' as your first argument!");
                System.exit(0);
            }
        }

        try {
            //System.out.println(Arrays.toString(q1));
            Connection conn = getConnection(jdbcURL, username, password);
            if (executeQ1) {
                if (q1Checker(q1, conn)) {
                    q1(q1,conn);
                }
                else {
                    System.out.println("Please enter a valid year and month!");
                }
            }
            else if (executeQ2) {
                if (q2Checker(q2, conn)) {
                    //System.out.println("Q2 is " + Arrays.toString(q2));
                    //System.out.println("This is a valid playerID and teamID!");
                    q2(q2, conn);
                    System.out.println("Player's team has been successfully updated!");
                }
                else {
                    System.out.println("Please enter a valid playerID and teamID!");
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
