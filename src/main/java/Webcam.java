import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;

import javafx.stage.Stage;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.opencv_core.IplImage;
import org.tensorflow.Tensor;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Executors;

import static java.lang.Float.parseFloat;
import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvSaveImage;


public class Webcam extends VBox {

    static TFUtils utils = new TFUtils();
    Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
    static boolean bool = true;

    private void exitButtonOnAction(ActionEvent event){
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }

    public Webcam() throws FrameGrabber.Exception {
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();

        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(600);
        imageView.setFitWidth(600);
        Label label = new Label();

        bool = true;

        Stage stage = new Stage();
        Button exitButton = new Button("sortir du mode camera");
        stage.setTitle("cam");
        Group root = new Group();

        Executors.newSingleThreadExecutor().execute(() -> {
            byte[] barr = null;
            IplImage img = null;
            Timer time = new Timer(); // Instantiate Timer Object
            ScheduledClassify scheduledTask;
            scheduledTask = null;
            if (bool)
                scheduledTask = new ScheduledClassify(barr, img);
            time.schedule(scheduledTask, 3000, 3000);
            while (bool) {
                Frame frame = null;
                try {
                    grabber.start();
                    frame = grabber.grabFrame();
                    img = converter.convert(frame);
                    barr = TFUtils.iplImageToByteArray(img);
                    scheduledTask.setParam(barr);
                    scheduledTask.setImg(img);
                    ScheduledClassify finalScheduledTask = scheduledTask;
                    Platform.runLater(() -> {
                        label.setText(String.format("BEST MATCH: %s %.2f%% likely%n", finalScheduledTask.getResultLabel(), finalScheduledTask.getResultPercent()));
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imageView.setImage(frameToImage(frame));
                imageView.setVisible(true);
                stage.setWidth(600);
                stage.setHeight(600);
            }
        });

        root.getChildren().add(imageView);

        label.setPadding(new Insets(500, 200, 50, 200));

        root.getChildren().addAll(label);
        root.getChildren().add(exitButton);

        exitButton.setOnAction(
                event -> {
                    try {
                        grabber.stop();
                        bool = false;
                        System.out.println("exit");
                        exitButtonOnAction(event);
                    } catch (FrameGrabber.Exception e) {
                        e.printStackTrace();
                    }
                }
        );
        Scene scene = new Scene(root);
        stage.sizeToScene();
        stage.setScene(scene);
        stage.show();

    }

    private WritableImage frameToImage(Frame frame) {
        BufferedImage bufferedImage = java2DFrameConverter.getBufferedImage(frame);
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

    public static class ScheduledClassify extends TimerTask {

        private IplImage img;
        byte[] param;
        private float resultPercent;
        private String resultLabel;
        private ImageDesc imageDesc;

        public ScheduledClassify(byte[] param, IplImage img) {
            this.param = param;
            this.resultLabel = "";
            this.resultPercent = 0.0f;
            this.img = img;
            this.imageDesc = new ImageDesc();
        }

        public ArrayList<Object> fluxWebcam(byte[] modelByte) {
            Path modelPath = PathFunctions.getLabelsPath();
            List<String> labels = ImageDesc.readAllLinesOrExit(modelPath);
            byte[] graphDef = null;
            try {
                graphDef = Files.readAllBytes(PathFunctions.getModelPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (Tensor image = utils.byteBufferToTensor(modelByte)) {
                System.out.println(graphDef);
                String[] resProbability;

                if (bool) {
                    resProbability = this.imageDesc.checkProbability(graphDef, image);
                    ArrayList<Object> result = new ArrayList<>();

                    result.add(resProbability[1]);
                    float res = parseFloat(resProbability[2]);
                    result.add(res);
                    return result;
                }

            }
            return null;
        }

        @Override
        public void run() {
            if (param != null) {
                ArrayList<Object> result = fluxWebcam(param);
                if (result != null) {
                    Date date = new Date();
                    Instant instant = date.toInstant();
                    resultLabel = (String) result.get(0);
                    resultPercent = (float) result.get(1);
                    System.out.println(resultLabel);
                    System.out.println(instant);
                    cvSaveImage(PathFunctions.getPicturePath() + "/camera/" + resultLabel + instant + ".jpg", img);
                }
            }
        }

        public float getResultPercent() {
            return resultPercent;
        }

        public String getResultLabel() {
            return resultLabel;
        }

        public void setParam(byte[] param) {
            this.param = param;
        }

        public void setImg(IplImage img) {
            this.img = img;
        }
    }
}



