import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.util.Arrays;

public class Insert {

    // command lines used: javac Insert.java
    // java -cp mysql-connector-j-8.0.31.jar;. Insert players.csv teams.csv members.csv tournaments.csv matches.csv earnings.csv
    // note: included the sqlconnector jar file and players.csv file in same folder as Insert.java

    public static void PlayersInsert(String jdbcURL, String username, String password, String csv) {
        // use batch feature for better performance?, 20 statements at a time
        int batchSize = 20;
        Connection connection = null;

        try {
            System.out.println("Connecting to Database...");
            connection = DriverManager.getConnection(jdbcURL, username, password);
            connection.setAutoCommit(false);

            String sql = "INSERT INTO players (player_id, tag, real_name, nationality, birthday, game_race)" +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);

            //read in csv file
            BufferedReader lineReader = new BufferedReader(new FileReader(csv));
            String lineText;

            int count = 0;

            // traverse through csv file
            System.out.println("Inserting into Players table...");
            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");
                String player_id = data[0];
                String tag = data[1];
                String real_name = data[2];
                String nationality = data[3];
                String birthday = data[4];
                String game_race = data[5];

                // remove extra "s
                tag = tag.replace("\"", "");
                real_name = real_name.replace("\"", "");
                birthday = birthday.replace("\"", "");
                nationality = nationality.replace("\"", "");
                game_race = game_race.replace("\"","");

                statement.setInt(1, Integer.parseInt(player_id));
                statement.setString(2, tag);
                statement.setString(3, real_name);
                statement.setString(4, nationality);
                statement.setDate(5, Date.valueOf(birthday));
                statement.setString(6, game_race);

                statement.addBatch();
                count++;

                if (count % batchSize == 0) {
                    statement.executeBatch();
                }
            }

            lineReader.close();

            statement.executeBatch();
            connection.commit();
            connection.close();

        } catch (IOException fi) {
            fi.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                // if there is an error, then rollback the changes
                if (connection != null) {
                    connection.rollback();
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void TeamsInsert(String jdbcURL, String username, String password, String csv) {
        int batchSize = 20;
        Connection connection = null;

        try {
            System.out.println("Connecting to Database...");
            connection = DriverManager.getConnection(jdbcURL, username, password);
            connection.setAutoCommit(false);

            String sql = "INSERT INTO teams (team_id, team_name, founded, disbanded)" +
                    "VALUES (?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);

            //read in csv file
            BufferedReader lineReader = new BufferedReader(new FileReader(csv));
            String lineText;

            int count = 0;

            // traverse through csv file
            System.out.println("Inserting into Teams table...");
            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");

                String team_id = data[0];
                String team_name = data[1];
                String founded = data[2];
                String disbanded = null;
                if (data.length == 4) {
                    disbanded = data[3];
                }

                // strings to data
                team_name = team_name.replace("\"", "");
                founded = founded.replace("\"", "");
                if (data.length == 4) {
                    disbanded = disbanded.replace("\"", "");
                }

                // insert into table
                statement.setInt(1, Integer.parseInt(team_id));
                statement.setString(2, team_name);
                statement.setDate(3, Date.valueOf(founded));
                if (data.length == 4) {
                    statement.setDate(4, Date.valueOf(disbanded));
                }
                else if (data.length < 4) {
                    statement.setDate(4, null);
                }

                statement.addBatch();
                count++;

                if (count % batchSize == 0) {
                    statement.executeBatch();
                }
            }
            lineReader.close();

            statement.executeBatch();
            connection.commit();
            connection.close();
        }
        catch (IOException fi) {
            fi.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                // if there is an error, then rollback the changes
                if (connection != null) {
                    connection.rollback();
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void TournamentsInsert(String jdbcURL, String username, String password, String csv) {
        int batchSize = 20;
        Connection connection = null;

        try {
            System.out.println("Connecting to Database...");
            connection = DriverManager.getConnection(jdbcURL, username, password);
            connection.setAutoCommit(false);

            String sql = "INSERT INTO tournaments (tournament_id, tournament_name, region, major)" +
                    "VALUES (?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);

            //read in csv file
            BufferedReader lineReader = new BufferedReader(new FileReader(csv));
            String lineText;

            int count = 0;

            // traverse through csv file
            System.out.println("Inserting into Tournaments table...");
            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");

                String tournament_id = data[0];
                String tournament_name = data[1];
                String region = data[2];
                String major = data[3];

                // remove extra "s
                tournament_name = tournament_name.replace("\"", "");
                region = region.replace("\"", "");
                major = major.replace("\"", "");

                statement.setInt(1, Integer.parseInt(tournament_id));
                statement.setString(2, tournament_name);
                statement.setString(3, region);
                statement.setBoolean(4, Boolean.parseBoolean(major));

                statement.addBatch();
                count++;

                if (count % batchSize == 0) {
                    statement.executeBatch();
                }
            }

            lineReader.close();

            statement.executeBatch();
            connection.commit();
            connection.close();

        } catch (IOException fi) {
            fi.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                // if there is an error, then rollback the changes
                if (connection != null) {
                    connection.rollback();
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void MembersInsert(String jdbcURL, String username, String password, String csv) {
        int batchSize = 20;
        Connection connection = null;

        try {
            System.out.println("Connecting to Database...");
            connection = DriverManager.getConnection(jdbcURL, username, password);
            connection.setAutoCommit(false);

            String sql = "INSERT INTO members (player_id, team_id, start_date, end_date)" +
                    "VALUES (?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);

            //read in csv file
            BufferedReader lineReader = new BufferedReader(new FileReader(csv));
            String lineText;

            int count = 0;

            // traverse through csv file
            System.out.println("Inserting into Members table...");
            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");

                String player_id = data[0];
                String team_id = data[1];
                String start_date = data[2];
                String end_date = null;
                if (data.length == 4) {
                    end_date = data[3];
                }

                // remove extra "s
                start_date = start_date.replace("\"", "");
                if (data.length == 4) {
                    end_date = end_date.replace("\"", "");
                }

                statement.setInt(1, Integer.parseInt(player_id));
                statement.setInt(2, Integer.parseInt(team_id));
                statement.setDate(3, Date.valueOf(start_date));
                if (data.length == 4) {
                    statement.setDate(4, Date.valueOf(end_date));
                }
                else if (data.length < 4) {
                    statement.setDate(4, null);
                }


                statement.addBatch();
                count++;

                if (count % batchSize == 0) {
                    statement.executeBatch();
                }
            }

            lineReader.close();

            statement.executeBatch();
            connection.commit();
            connection.close();

        } catch (IOException fi) {
            fi.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                // if there is an error, then rollback the changes
                if (connection != null) {
                    connection.rollback();
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void EarningsInsert(String jdbcURL, String username, String password, String csv) {
        int batchSize = 20;
        Connection connection = null;

        try {
            System.out.println("Connecting to Database...");
            connection = DriverManager.getConnection(jdbcURL, username, password);
            connection.setAutoCommit(false);

            String sql = "INSERT INTO earnings (tournament_id, player_id, prize_money, position)" +
                    "VALUES (?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);

            //read in csv file
            BufferedReader lineReader = new BufferedReader(new FileReader(csv));
            String lineText;

            int count = 0;

            // traverse through csv file
            System.out.println("Inserting into Earnings table...");
            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");

                String tournament_id = data[0];
                String player_id = data[1];
                String prize_money = data[2];
                String position = data[3];

                statement.setInt(1, Integer.parseInt(tournament_id));
                statement.setInt(2, Integer.parseInt(player_id));
                statement.setInt(3, Integer.parseInt(prize_money));
                statement.setInt(4, Integer.parseInt(position));

                statement.addBatch();
                count++;

                if (count % batchSize == 0) {
                    statement.executeBatch();
                }
            }

            lineReader.close();

            statement.executeBatch();
            connection.commit();
            connection.close();

        } catch (IOException fi) {
            fi.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                // if there is an error, then rollback the changes
                if (connection != null) {
                    connection.rollback();
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void MatchesInsert(String jdbcURL, String username, String password, String csv) {
        int batchSize = 20;
        Connection connection = null;

        try {
            System.out.println("Connecting to Database...");
            connection = DriverManager.getConnection(jdbcURL, username, password);
            connection.setAutoCommit(false);

            String sql = "INSERT INTO matches (match_id, date_of, tournament_id, playerA_id, playerB_id, playerA_score, playerB_score," +
                    " is_offline) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);

            //read in csv file
            BufferedReader lineReader = new BufferedReader(new FileReader(csv));
            String lineText;

            int count = 0;

            // traverse through csv file
            System.out.println("Inserting into Matches table...");
            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");

                String match_id = data[0];
                String date_of = data[1];
                String tournament_id = data[2];
                String playerA_id = data[3];
                String playerB_id = data[4];
                String playerA_score = data[5];
                String playerB_score = data[6];
                String is_offline = data[7];

                //remove extra "s
                date_of = date_of.replace("\"", "");

                statement.setInt(1, Integer.parseInt(match_id));
                statement.setDate(2, Date.valueOf(date_of));
                statement.setInt(3, Integer.parseInt(tournament_id));
                statement.setInt(4, Integer.parseInt(playerA_id));
                statement.setInt(5, Integer.parseInt(playerB_id));
                statement.setInt(6, Integer.parseInt(playerA_score));
                statement.setInt(7, Integer.parseInt(playerB_score));
                statement.setBoolean(8, Boolean.parseBoolean(is_offline));

                statement.addBatch();
                count++;

                if (count % batchSize == 0) {
                    statement.executeBatch();
                }
            }

            lineReader.close();

            statement.executeBatch();
            connection.commit();
            connection.close();

        } catch (IOException fi) {
            fi.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                // if there is an error, then rollback the changes
                if (connection != null) {
                    connection.rollback();
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {

        String jdbcURL = "jdbc:mysql://localhost:3306/resulttrackerjadrieldelim";
        String username = "root";
        String password = "root";

        // take in csv files as command line arguments, switch statement
        String players = "";
        String teams = "";
        String members = "";
        String tournaments = "";
        String matches = "";
        String earnings = "";

        for (String val : args) {
            switch (val) {
                case "players.csv": players = val;
                case "teams.csv": teams = val;
                case "members.csv": members = val;
                case "tournaments.csv": tournaments = val;
                case "matches.csv": matches = val;
                case "earnings.csv": earnings = val;
            }
        }

        PlayersInsert(jdbcURL, username, password, players);
        TeamsInsert(jdbcURL, username, password, teams);
        TournamentsInsert(jdbcURL, username, password, tournaments);
        MembersInsert(jdbcURL, username, password, members);
        EarningsInsert(jdbcURL, username, password, earnings);
        MatchesInsert(jdbcURL, username, password, matches);

        System.out.println("\nAll data inserted! Have a great day! :)");
    }
}
