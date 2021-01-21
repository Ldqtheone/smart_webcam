import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
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
    private Label labelFilter;

    /**
     * Buttons
     */
    private Button openButton;
    private Button submitButton;
    private ChoiceBox choiceFilter;
    private Button frameFilter;


    public TensorFlowBuilder(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    /**
     * Create the JavaFX window
     */
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
        this.labelFilter = new Label("Select a filter");

        this.openButton = new Button("Comparer une image...");
        this.submitButton = new Button("Lancer la comparaison");
        this.choiceFilter = new ChoiceBox(FXCollections.observableArrayList(
                "Orange" , "Vert", "Bleu", "Rose", " Gris"
        ));
        this.frameFilter = new Button("Ajouter un cadre");

        this.imageView = new ImageView(); //Image for render

        this.startActionsButton();
    }

    /**
     * Start event with press selected button
     */
    private void startActionsButton(){

        /** J'appuie sur le choix du cadre */
        this.frameFilter.setOnAction(
                event -> {
                    if(this.choosenImg != null) {
                        try {
                            Filters.frameFilter(this.choosenImg.toString());
                            System.out.println("3 : " + this.choosenImg);
                            this.choosenImg = new File(this.choosenImg.toString().replace(".jpg", "_frame.png"));
                            System.out.println("4 : " + this.choosenImg);
                        } catch (FilterException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        System.out.println("Aucune image n'a été séléctionnée");
                    }
                }
        );

        /** J'ouvre le finder */
        this.openButton.setOnAction(
                event -> {
                    this.choosenImg = fileImageChooser.createFileChooser().showSaveDialog(this.primaryStage);
                }
        );

       /** J'envoie mon formulaire et l'image */
        this.submitButton.setOnAction(
                event -> {

                    if(this.choiceFilter.getValue() != null) {
                        try {
                            Filters.filterColor(this.choosenImg.toString(), this.choiceFilter.getValue().toString());
                            this.choosenImg = new File(this.choosenImg.toString().replace(".jpg", "_filter.jpg"));
                        } catch (FilterException e) {
                            e.printStackTrace();
                        }
                    }

                    //Set the user image
                    System.out.println(this.choosenImg.toString());
                    this.imageView.setImage(new Image("file:\\"+this.choosenImg.toString()));

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
                                    String format = this.choosenImg.toString().matches(".jpg") ? "jpg" : "png";
                                    ImageIO.write(bufferedImage, format, new File(PathFunctions.getPicturePath() + "/" + this.userInputFolder.getText() + "/" + this.userInput.getText() + "." + format));
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

    /**
     * Add all components to root
     */
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
        root.getChildren().add(this.labelFilter);
        root.getChildren().add(this.choiceFilter);
        root.getChildren().add(this.frameFilter);
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
        return imgDesc.imgtoByteArray(Paths.get(args));
    }


    public void launchProgram(){
        this.createWindow();
    }
}
