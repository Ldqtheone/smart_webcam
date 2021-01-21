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

public class TensorFlowBuilder {

    private final Stage primaryStage;
    private File choosenImg = null;

    /**
     * Components
     */
    private JavaFxImageFileChooser fileImageChooser;
    private ImageView imageView;
    private TextField userInput;
    private TextField userInputDescription;
    private TextField userInputFolder;
    private TextField userInputPercent;

    /**
     * Labels
     */
    private Label labelInputImg;
    private Label labelDescription;
    private Label labelFolder;
    private Label labelPercent;
    private Label labelResult;

    /**
     * Buttons
     */
    private Button openButton;
    private Button submitButton;


    public TensorFlowBuilder(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    private void createWindow(){
        //Create the window
        this.primaryStage.setTitle("SmartCam 1.0");

        // Create the elements
        this.fileImageChooser = new JavaFxImageFileChooser(); //Custom FileChooser class
        this.userInput = new TextField(); //Text field for user input
        this.userInputDescription = new TextField(); //Text field for wanted description
        this.userInputFolder = new TextField(); //Text field for destination folder
        this.userInputPercent = new TextField(); //Text field for user input

        this.labelInputImg = new Label("Image name");
        this.labelDescription = new Label("Wanted description");
        this.labelFolder = new Label("Destination folder");
        this.labelPercent = new Label("Wanted percentage");
        this.labelResult = new Label("No result"); //Label for the futur result

        this.openButton = new Button("Comparer une image...");
        this.submitButton = new Button("Lancer la comparaison");

        this.imageView = new ImageView(); //Image for render

        this.startActionsButton();
    }

    private void startActionsButton(){

        this.openButton.setOnAction(
                event -> {
                    this.choosenImg = fileImageChooser.createFileChooser().showSaveDialog(this.primaryStage);
                }
        );

        // action event
        this.submitButton.setOnAction(
                event -> {
                    //Set the user image
                    this.imageView.setImage(new Image("file:\\"+this.choosenImg.toString()));

                    //get pathfile from user input
                    Path pathfile = PathFunctions.createPathFile(this.userInput.getText());

                    //Start analysis of the image
                    if(!this.userInputPercent.getText().matches("\\d+")) {
                        this.userInputPercent.setText("0");
                    }
                    //String[] tabLabelsUser = userInputDescription.getText().split(" ");
                    String[] resultTab = startAnalysis(this.choosenImg.toString());
                    this.labelResult.setText(resultTab[0]);
                    System.out.println(resultTab[2]);

                    //Check if the value are corresponding the wanted ones
                    if( (Integer.parseInt(this.userInputPercent.getText()) <= Float.parseFloat(resultTab[2]))){
                        if(this.userInputDescription.getText().equals("") || (this.userInputDescription.getText().contains(resultTab[1])) ) {
                            if(this.choosenImg != null) {
                                try {
                                    BufferedImage bufferedImage = ImageIO.read(this.choosenImg);
                                    ImageIO.write(bufferedImage, "jpg", new File(PathFunctions.getPicturePath() + "/" + this.userInputFolder.getText() + "/" + this.userInput.getText() + ".jpg"));
                                } catch (IOException ignored) {
                                }
                            }
                        }
                        else{
                            this.labelResult.setText("Description is not the same");
                        }
                    }
                    else {
                        this.labelResult.setText("Likely chance below expected");
                    }
                }
        );

        this.finalizeWindow();
    }

    private void finalizeWindow(){
        this.imageView.setFitHeight(100);
        this.imageView.setFitWidth(100);

        TilePane root = new TilePane();

        // add elements
        root.getChildren().add(this.labelInputImg);
        root.getChildren().add(this.userInput);
        root.getChildren().add(this.labelDescription);
        root.getChildren().add(this.userInputDescription);
        root.getChildren().add(this.labelPercent);
        root.getChildren().add(this.userInputPercent);
        root.getChildren().add(this.labelFolder);
        root.getChildren().add(this.userInputFolder);
        root.getChildren().add(this.labelResult);
        root.getChildren().add(this.imageView);
        root.getChildren().add(this.openButton);
        root.getChildren().add(this.submitButton);
        this.primaryStage.setScene(new Scene(root, 600, 600));
        this.primaryStage.show();
    }

    /**
     * Start TensorFlow Analysys
     * @param args
     * @return description find
     */
    private static String[] startAnalysis(String args) {
        ImageDesc imgDesc = new ImageDesc();

        //recovery line in the shell
        //String filename = args.toString();

        //path of File
        //Path pahFile = PathFunctions.createPathFile(args);

        return imgDesc.imgtoByteArray(Paths.get(args));
    }

    public void launchProgram(){
        this.createWindow();
    }
}
