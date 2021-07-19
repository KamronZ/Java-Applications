package View_Controller;



import Model.AppointmentsDB;
import Utils.DBConnection;
import Utils.DBQuery;
import Utils.Language;
import Utils.Time;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static java.lang.Integer.parseInt;


/**
 * used to validate login
 */
public class LogInController {
    @FXML
    public TextField usernameTextField;
    @FXML
    public TextField passwordTextField;

    @FXML
    public boolean isEnglish = true;
    @FXML
    public Text userLocationLabel;
    @FXML
    public Button loginButtonLabel;
    @FXML
    public Text titleLabel;
    @FXML
    public Text usernameLabel;
    @FXML
    public Text passwordLabel;

    private static int userID;
    private static String userZoneID = Time.getTimeZoneID();
    private int loginCounter = 0;
    private int attempts = 4;

    @FXML
    /**
     * used for testing to change language of user
     */
    public void initialize() {
        /*Locale locale = new Locale("fr");
        Locale.setDefault(locale);*/
        setLabels(); // changes to french if language is french
    }

    /**
     * getter is needed to match userID in DB
     *
     * @return
     */
    public static int getUserID() {
        return userID;
    }

    /**
     * used to retrieve the current user zone ID
     *
     * @return
     */
    public static int getZoneID() {
        Integer zoneID = parseInt(userZoneID);

        return zoneID;
    }

    /**
     * used to set all labels to french or english
     */
    private void setLabels() {
        userLocationLabel.setText(Time.getTimeZone() + " | " + Time.getTimeZoneID()); // automatically displays in proper language

        if (Language.getLanguage().getLanguage().equalsIgnoreCase("fr")) {


            System.out.println(Language.getLanguage());
            isEnglish = false;
            loginButtonLabel.setText("Connexion");
            titleLabel.setText("connexion");
            usernameLabel.setText("Nom d'utilisateur");
            passwordLabel.setText("le mot de passe");
        }
    }

    /**
     * checks if users username and password match the DB
     *
     * @param actionEvent
     * @throws SQLException
     */
    public void loginButton(ActionEvent actionEvent) throws SQLException {


        String password = passwordTextField.getText();
        String username = usernameTextField.getText();


        String selectStatement = "SELECT * FROM users WHERE password = ? AND User_Name = ?";

        DBQuery.setPreparedStatement(DBConnection.getConnection(), selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement(); // creates statement reference


        ps.setString(1, password); // key is index, value is what provided;for ? number 1
        ps.setString(2, username); // key is index, value is what provided;for ? number 2

        ResultSet resultSet = ps.executeQuery();


        if (!resultSet.next()) {
            loginCounter++;
            logger(false, LocalDateTime.now(), username);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            if (isEnglish) {
                alert.setTitle("INVALID");
                alert.setContentText("Invalid password and username.");
            } else {
                alert.setTitle("INVALIDE");
                alert.setContentText("Mot de passe et nom d'utilisateur invalides");

            }
            alert.showAndWait();

        } else {

            logger(true, LocalDateTime.now(), username);
            String selectStatement2 = "SELECT User_ID FROM users WHERE password = ? AND User_name = ?";
            DBQuery.setPreparedStatement(DBConnection.getConnection(), selectStatement);
            PreparedStatement ps2 = DBQuery.getPreparedStatement(); // creates statement reference


            ps2.setString(1, password); // key is index, value is what provided;for ? number 1
            ps2.setString(2, username); // key is index, value is what provided;for ? number 2

            ResultSet rs2 = ps2.executeQuery();
            userID = resultSet.getInt("User_ID");


            try {
                // hide log in stage
                Stage stage = (Stage) loginButtonLabel.getScene().getWindow();
                stage.hide();

                // load Main Menu
                Parent root = FXMLLoader.load((getClass().getResource("/View_Controller/MainMenu.fxml")));
                Stage addPartStage = new Stage();
                addPartStage.setTitle("Main Menu");
                addPartStage.setScene(new Scene(root, 865, 599));
                addPartStage.show();

                AppointmentsDB.isUpcomingApt();

            } catch (Exception error) {
                error.printStackTrace();
            }
        }
    }


    /**
     * logs successful and unsuccessful attempts.  Kills connection and application if more than 4 failed attempts are made and logs
     * a severe warning in this case.
     * @param wasSuccessful true if log in was successful
     * @param logInTime LocalDateTime.now() of log in attempt
     * @param userName User name attempted to log in with
     */
    private void logger(boolean wasSuccessful, LocalDateTime logInTime, String userName) {
        logInTime = logInTime.truncatedTo(ChronoUnit.MILLIS);
        Timestamp logInAttemptInUTC = Time.convertToUTC(logInTime);
        if (wasSuccessful) {
            String info = "Log in attempt at: " + logInAttemptInUTC + " for username:" + userName + ", was successful";
            Logger log = Logger.getLogger("log.txt");

            try {
                FileHandler fh = new FileHandler("log.txt", true);
                SimpleFormatter sf = new SimpleFormatter();
                fh.setFormatter(sf);
                log.addHandler(fh);

                log.info(info);

            } catch (IOException e) {

            }
        } else {
            if (loginCounter <= 3) {
                try {
                    String info = "Log in attempt at: " + logInAttemptInUTC + " for username: " + userName + " was not successful.  " +
                            "this session has failed to log in:" + loginCounter + " times";
                    Logger log = Logger.getLogger("log.txt");

                    FileHandler fh = new FileHandler("log.txt", true);
                    SimpleFormatter sf = new SimpleFormatter();
                    fh.setFormatter(sf);
                    log.addHandler(fh);

                    log.warning(info);

                } catch (IOException e) {

                }
            } else {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("You have too many failed attempts! TERMINATING");
                DBConnection.closeConnection();
                alert.showAndWait();

               Stage stage = (Stage) loginButtonLabel.getScene().getWindow();
                stage.close();
                try {
                    String info = "Log in attempt at: " + logInAttemptInUTC + " for username: " + userName + " was not successful.  " +
                            "this session has failed to log in:" + (loginCounter ) + " times and program closed forcefully!";
                    Logger log = Logger.getLogger("log.txt");

                    FileHandler fh = new FileHandler("log.txt", true);
                    SimpleFormatter sf = new SimpleFormatter();
                    fh.setFormatter(sf);
                    log.addHandler(fh);

                    log.severe(info);
                } catch (IOException e) {

                }
            }
        }
    }
}

