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
import java.nio.file.Paths;
import java.util.Scanner;

public class Main extends Application {

    File choosenImg = null;

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
        TextField userInputDescription = new TextField(); //Text field for wanted description
        TextField userInputFolder = new TextField(); //Text field for destination folder
        TextField userInputPercent = new TextField(); //Text field for user input

        Label labelInputImg = new Label("Image name");
        Label labelDescription = new Label("Wanted description");
        Label labelFolder = new Label("Destination folder");
        Label labelPercent = new Label("Wanted percentage");

        Label labelResult = new Label("No result"); //Label for the futur result

        Button openButton = new Button("Enregistrer une image...");

        ImageView imageView = new ImageView(); //Image for render


        openButton.setOnAction(
                event -> {
                    this.choosenImg = fileImageChooser.createFileChooser().showSaveDialog(primaryStage);
                }
        );

        // action event
        EventHandler<ActionEvent> event = (ActionEvent e) -> {
            //Set the user image
            //System.out.println(this.getClass().getResource("/tensorPics/" + userInput.getText()+ ".jpg").toString());
            imageView.setImage(new Image("file:\\"+this.choosenImg.toString()));
            //get pathfile from user input
            Path pathfile = PathFunctions.createPathFile(userInput.getText());
            //Start analysis of the image
            if(!userInputPercent.getText().matches("\\d+")) {
                userInputPercent.setText("0");
            }
            //String[] tabLabelsUser = userInputDescription.getText().split(" ");
            String[] resultTab = startAnalysis(this.choosenImg.toString());
            labelResult.setText(resultTab[0]);
            System.out.println(resultTab[2]);
            //Check if the value are corresponding the wanted ones
            if( (Integer.parseInt(userInputPercent.getText()) <= Float.parseFloat(resultTab[2]))){
                if(userInputDescription.getText().equals("") || (userInputDescription.getText().contains(resultTab[1])) ) {
                    if(this.choosenImg != null) {
                        try {
                            BufferedImage bufferedImage = ImageIO.read(this.choosenImg);
                            ImageIO.write(bufferedImage, "jpg", new File(PathFunctions.getPicturePath() + "/" + userInput.getText() + ".jpg"));
                        } catch (IOException ignored) {
                        }
                    }
                }
                else{
                    labelInputImg.setText("nop"); //En attendant la sauvegarde
                }
            }
            else {
                labelInputImg.setText("nop"); //En attendant la sauvegarde
            }

        };

        // when enter is pressed
        userInput.setOnAction(event);

        imageView.setFitHeight(100);
        imageView.setFitWidth(100);

        TilePane root = new TilePane();
        //root.getChildren().add(btn);
        // add elements
        root.getChildren().add(labelInputImg);
        root.getChildren().add(userInput);
        root.getChildren().add(labelDescription);
        root.getChildren().add(userInputDescription);
        root.getChildren().add(labelPercent);
        root.getChildren().add(userInputPercent);
        root.getChildren().add(labelFolder);
        root.getChildren().add(userInputFolder);
        root.getChildren().add(labelResult);
        root.getChildren().add(imageView);
        root.getChildren().add(openButton);
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();

    }

    public static String[] startAnalysis(String args) {
        ImageDesc imgDesc = new ImageDesc();

        // recovery line in the shell
        //String filename = args.toString();

        // path of File
        //Path pahFile = PathFunctions.createPathFile(args);

        return imgDesc.imgtoByteArray(Paths.get(args));
        //return "";
    }

}
