import javafx.application.Application;
import javafx.application.Platform;
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
import org.tensorflow.Tensor;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    public static String recoveryLineCommand() {
        //Scanner sc = new Scanner(System.in);
        System.out.println("Veuillez saisir le chemin de l'image :");
        String filename = "jack.png";
        //sc.close();
        return filename;
    }

    public static void main2(String[] args) {
        TFUtils utils = new TFUtils();

        // recovery line in the shell
        String filename = recoveryLineCommand();

        // path of File
        Path pahFile = PathFunctions.createPathFile(filename);

        imgDesc.imgtoByteArray(pahFile);
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
        TextField userInput = new TextField(); //Text field for user input
        Label labelResult = new Label("No result"); //Label for the futur result
        ImageView imageView = new ImageView(); //Image for render

        // action event
        EventHandler<ActionEvent> event = (ActionEvent e) -> {
            //Set the user image
            imageView.setImage(new Image(this.getClass().getResource("/tensorPics/"+userInput.getText()).toString()));
            //get pathfile from user input
            Path pathfile = createPathFile(userInput.getText());
            //Start analysis of the image
            imageAnalysis(pathfile);
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
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }
}
