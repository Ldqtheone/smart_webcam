import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class Main extends Application {

    public static String recoveryLineCommand() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Veuillez saisir le chemin de l'image :");
        String filename = sc.nextLine();
        sc.close();
        return filename;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Create the window
        primaryStage.setTitle("SmartCam 1.0");

        /*Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction((action) -> {
            System.out.println("Hello World!");
        });
         */

        // Create the elements
        JavaFxImageFileChooser fileImageChooser = new JavaFxImageFileChooser(); //Custom FileChooser class
        TextField userInput = new TextField(); //Text field for user input
        Label labelResult = new Label("No result"); //Label for the futur result

        Button openButton = new Button("Enregistrer une image...");

        ImageView imageView = new ImageView(); //Image for render

        openButton.setOnAction(
                event -> {
                    File file = fileImageChooser.createFileChooser().showSaveDialog(primaryStage);
                    if(file != null)
                        try {
                            BufferedImage bufferedImage = ImageIO.read(file);
                            ImageIO.write(bufferedImage, "jpg", new File(PathFunctions.getPicturePath() + "/" + userInput.getText()+ ".jpg"));
                        } catch (IOException ignored) {
                        }
                }
        );

        // action event
        EventHandler<ActionEvent> event = (ActionEvent e) -> {
            //Set the user image
            imageView.setImage(new Image(this.getClass().getResource("/tensorPics/" + userInput.getText()+ ".jpg").toString()));
            //get pathfile from user input
            Path pathfile = PathFunctions.createPathFile(userInput.getText());
            //Start analysis of the image
            labelResult.setText(startAnalysis(userInput.getText()));
        };

        // when enter is pressed
        userInput.setOnAction(event);

        imageView.setFitHeight(100);
        imageView.setFitWidth(100);

        TilePane root = new TilePane();
        //root.getChildren().add(btn);
        // add elements
        root.getChildren().add(userInput);
        root.getChildren().add(labelResult);
        root.getChildren().add(imageView);
        root.getChildren().add(openButton);
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }



    public static String startAnalysis(String args) {
        ImageDesc imgDesc = new ImageDesc();

        // recovery line in the shell
        //String filename = args.toString();

        // path of File
        Path pahFile = PathFunctions.createPathFile(args);

        return imgDesc.imgtoByteArray(pahFile);
        //return "";
    }

}
