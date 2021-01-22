import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import org.bytedeco.javacv.FrameGrabber;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class TensorFlowBuilder {

    private final Stage primaryStage;
    private File choosenImg = null;
    private File choosenStamp = null;

    /**
     * Components
     */
    private JavaFxImageFileChooser fileImageChooser;
    private ImageView imageView;
    private TextField userInput;
    private TextField userInputDescription;
    private TextField userInputFolder;
    private TextField userInputPercent;
    private TextField userInputX;
    private TextField userInputY;

    /**
     * Labels
     */
    private Label labelInputImg;
    private Label labelDescription;
    private Label labelFolder;
    private Label labelPercent;
    private Label labelResult;
    private Label labelFilter;
    private Label labelX;
    private Label labelY;

    /**
     * Buttons
     */
    private Button openButton;
    private Button openStampButton;
    private Button submitButton;
    private ChoiceBox choiceFilter;
    private Button frameFilter;
    private Button stampFilter;
    private Button webcamButton;


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
        this.userInputX = new TextField(); //Text field for wanted x for stamp
        this.userInputY = new TextField(); //Text field for wanted y for stamp

        this.labelInputImg = new Label("Image name");
        this.labelDescription = new Label("Wanted description");
        this.labelFolder = new Label("Destination folder");
        this.labelPercent = new Label("Wanted percentage");
        this.labelResult = new Label("No result"); //Label for the futur result
        this.labelFilter = new Label("Select a filter");
        this.labelX = new Label("X");
        this.labelY = new Label("Y");

        this.openButton = new Button("Comparer une image...");
        this.openStampButton = new Button("Choisir un tampon...");
        this.submitButton = new Button("Lancer la comparaison");
        this.webcamButton = new Button("Démarrer un webcam");
        this.choiceFilter = new ChoiceBox(FXCollections.observableArrayList(
                "Orange" , "Vert", "Bleu", "Rose", " Gris"
        ));
        this.frameFilter = new Button("Ajouter un cadre");
        this.stampFilter = new Button("Ajouter un tampon");


        this.imageView = new ImageView(); //Image for render

        this.startActionsButton();
    }

    /**
     * Start event with press selected button
     */
    private void startActionsButton(){

        /** J'appuie sur le bouton de ma caméra */
        this.webcamButton.setOnAction(
                event -> {
                    try {
                        new Webcam();
                    } catch (FrameGrabber.Exception e) {
                        e.printStackTrace();
                    }
                }
        );

        /** J'appuie sur le choix du cadre */
        this.frameFilter.setOnAction(
                event -> {
                    if(this.choosenImg != null) {
                        try {
                            Filters.frameFilter(this.choosenImg.toString());
                            this.choosenImg = new File(this.choosenImg.toString().replace(".jpg", "_frame.png"));
                            this.choosenImg = new File(this.choosenImg.toString().replace("_stamp.png", "_stamp_frame.png"));
                        } catch (FilterException | IOException e) {
                            e.printStackTrace();
                        }

                        this.imageView.setImage(new Image("file:\\"+this.choosenImg.toString()));

                    }
                    else{
                        System.out.println("Aucune image n'a été séléctionnée");
                    }
                }
        );

        /** J'appuie sur le choix du tampon */
        this.stampFilter.setOnAction(
                event -> {
                    if(this.choosenImg != null) {
                        try {
                            if(!this.userInputX.getText().matches("\\d+")) {
                                this.userInputX.setText("0");
                            }
                            if(!this.userInputY.getText().matches("\\d+")) {
                                this.userInputY.setText("0");
                            }

                            BufferedImage overlay;
                            if(this.choosenStamp == null){
                                overlay = ImageIO.read(new File(PathFunctions.getPicturePath().toString() + "/filters/stamp.png"));
                            }else {
                                overlay = overlay = ImageIO.read(new File(this.choosenStamp.toString()));
                            }

                            Filters.stampFilter(this.choosenImg.toString(), overlay, Integer.parseInt(this.userInputX.getText()), Integer.parseInt(this.userInputY.getText()));
                            this.choosenImg = new File(this.choosenImg.toString().replace(".jpg", "_stamp.png"));
                            this.choosenImg = new File(this.choosenImg.toString().replace("_frame.png", "_stamp_frame.png"));
                        } catch (FilterException | IOException e) {
                            e.printStackTrace();
                        }

                        this.imageView.setImage(new Image("file:\\"+this.choosenImg.toString()));

                    }
                    else{
                        System.out.println("Aucune image n'a été séléctionnée");
                    }
                }
        );

        //tamponFilter

        /** J'ouvre le finder pour choisir une image */
        this.openButton.setOnAction(
                event -> {
                    this.choosenImg = fileImageChooser.createFileChooser().showSaveDialog(this.primaryStage);
                    this.imageView.setImage(new Image("file:\\"+this.choosenImg.toString()));
                }
        );

        /** J'ouvre le finder popur choisir un tampon */
        this.openStampButton.setOnAction(
                event -> {
                    this.choosenStamp = fileImageChooser.createFileChooser().showSaveDialog(this.primaryStage);
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
        root.getChildren().add(this.labelX);
        root.getChildren().add(this.userInputX);
        root.getChildren().add(this.labelY);
        root.getChildren().add(this.userInputY);
        root.getChildren().add(this.labelResult);
        root.getChildren().add(this.imageView);
        root.getChildren().add(this.labelFilter);
        root.getChildren().add(this.choiceFilter);
        root.getChildren().add(this.frameFilter);
        root.getChildren().add(this.stampFilter);
        root.getChildren().add(this.openButton);
        root.getChildren().add(this.submitButton);
        root.getChildren().add(this.webcamButton);
        root.getChildren().add(this.openStampButton);
        this.primaryStage.setScene(new Scene(root, 700, 600));
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
