import au.com.bytecode.opencsv.CSVReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.text.SimpleDateFormat;


public class CsvParser {
    public static void main(String[] str){
        int numRecords = 0;
        int numValidRecords = 0;
        int numFailedRecords = 0;
        String[] headers = null;

        CSVReader reader = openCsv();
        //sqlite database
        final String createTableSQL = "CREATE TABLE DATA (" +
                "A text NOT NULL," +
                "B text NOT NULL," +
                "C text PRIMARY KEY," +
                "D text NOT NULL," +
                "E text NOT NULL," +
                "F text NOT NULL," +
                "G text NOT NULL," +
                "H text NOT NULL," +
                "I text NOT NULL," +
                "J text NOT NULL);";
        Connection connection;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite::memory:");
            Statement stmt = connection.createStatement();
            stmt.execute(createTableSQL);

            //parses through each line of the csv. if it is a valid entry, it is added to the database, if not it is added to a file named "bad-data-currentTimeStamp.csv"
            String[] nextLine = null;
            try {
                //open bad data csv file
                String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
                String badDataFileName = "bad-data-" + timeStamp + ".csv";
                FileWriter fw = new FileWriter(badDataFileName);

                headers = reader.readNext();
                while ((nextLine = reader.readNext()) != null) {
                    numRecords++;
                    if(validRecord(nextLine)){
                        numValidRecords++;
                        //write to database;
                        writeToDatabase(nextLine, stmt);
                    }
                    else{
                        numFailedRecords++;
                        //write to log file
                        String joinedLine = String.join(",", nextLine);
                        fw.write(joinedLine + "\n");
                    }
                }
                //closes bad data file
                fw.close();
                //writes all contents of database to console
                ResultSet rs = stmt.executeQuery("SELECT * FROM DATA");
                while(rs.next()){
                    System.out.println(rs.getString("A") + " - " +
                            rs.getString("B") + " - " +
                            rs.getString("C") + " - " +
                            rs.getString("D") + " - " +
                            rs.getString("E") + " - " +
                            rs.getString("F") + " - " +
                            rs.getString("G") + " - " +
                            rs.getString("H") + " - " +
                            rs.getString("I") + " - " +
                            rs.getString("J") + " - ");
                }
            }
            catch(java.io.IOException e){
                System.out.println("file parsing error " + e.toString());
            }
        }
        catch(java.sql.SQLException e){
            System.out.println("error creating sqlite database " + e.toString());
        }

        //writes record information to log file
        writeLog(numRecords, numValidRecords, numFailedRecords);
    }

    public static CSVReader openCsv(){
        CSVReader reader = null;
        try {
            //MAKE SURE THE FILENAME HERE IS CORRECT OR ELSE IT WILL NOT FIND IT. you can do the full path if it is not in the project folder and it will work too
            reader = new CSVReader(new FileReader("ms3Interview.csv"), ',','"');
        }
        catch(java.io.FileNotFoundException e){
            System.out.println("file not found");
        }

        return reader;
    }

    public static boolean validRecord(String[] sa){
        boolean valid = true;
        for(String str : sa){
            if(str.equals("")){
                valid = false;
            }
        }
        return valid;
    }

    public static void writeLog(int received, int valid, int failed){
        try{
            FileWriter fw = new FileWriter("log.txt");
            fw.write("Records Received = " + received + " | Records Valid = " + valid + " | Records Failed = " + failed);
            fw.close();
        }
        catch(java.io.IOException e){
            System.out.println("error writing log file" + e.toString());
        }
    }

    public static void writeToDatabase(String[] values, java.sql.Statement stmt){
        String insertSql = "INSERT INTO DATA(A,B,C,D,E,F,G,H,I,J) VALUES(\"" + values[0] + "\",\""+ values[1] +"\",\"" + values[2] +"\",\"" + values[3] +"\",\"" + values[4] +
                "\",\"" + values[5] +"\",\"" + values[6] +"\",\"" + values[7] +"\",\"" + values[8] +"\",\"" + values[9] + "\");";
        try {
            stmt.execute(insertSql);
        }
        catch(java.sql.SQLException e){
            System.out.println("error writing data to database " + e.toString());
            System.out.println(insertSql);
        }
    }
}
