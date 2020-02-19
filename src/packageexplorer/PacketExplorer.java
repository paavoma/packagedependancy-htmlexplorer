/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package packageexplorer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


/**
 *
 * @author crazm_000
 */
public class PacketExplorer extends Application {

    public final static String PROJECTPATH = System.getProperty("user.dir");
    public static boolean ISDEBIAN = false;

    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Read file");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                ParserInterface parser = new Parser();
                HtmlBuilderInterface htmlBuilder = new HtmlBuilder();
                htmlBuilder.isOperatingSystemDebian();
                if (ISDEBIAN) {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Found operating system");
                    alert.setHeaderText(null);
                    alert.setContentText("You are using Debian or Ubuntu based operating system: Using /var/lib/dpkg/status");

                    alert.showAndWait();
                    parser.readFile("/var/lib/dpkg/status");
                } else {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Found operating system");
                    alert.setHeaderText(null);
                    alert.setContentText("You are using other than Debian or Ubuntu based operating system: Using mockup data");

                    alert.showAndWait();
                    
                    parser.readFile("status.real");
                    
                }
                
                //ArrayList<Package> packages = parser.getPackages();

                htmlBuilder.buildAllHtmlPages(parser.getPackages());
                try {
                    htmlBuilder.openIndexPageWithBrowser();
                } catch (URISyntaxException ex) {
                    Logger.getLogger(PacketExplorer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(PacketExplorer.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Package Explorer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}