
import java.nio.ByteBuffer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.IplImage;

import static org.bytedeco.opencv.global.opencv_core.cvFlip;
import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvSaveImage;

import org.bytedeco.javacv.OpenCVFrameConverter;

import java.awt.image.BufferedImage;

import org.bytedeco.opencv.opencv_core.Mat;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2BGRA;

public abstract class Webcam  extends Application {

    static ImageView videoView = new ImageView();

    static OpenCVFrameConverter<Mat> javaCVConv = new OpenCVFrameConverter.ToMat();
    static Mat javaCVMat = new Mat();
    static ByteBuffer buffer;
    static WritablePixelFormat<ByteBuffer> formatByte = PixelFormat.getByteBgraPreInstance();

   public static ImageView webcam(Stage primary)  {
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        try {
            grabber.start();
        } catch (Exception e) {
            System.out.println("grabber problem");
        }
        System.out.println("commence treatment");
        IplImage grabbedImage = null;
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        try {
            grabbedImage = converter.convert(grabber.grab());
        } catch (Exception e) {
            System.out.println("problem of conversion image");
        }
        System.out.println(grabbedImage);


        Frame grabbedImage1;

        try {
            grabbedImage1 = grabber.grab();
            System.out.println(grabbedImage1);
            Java2DFrameConverter converterBuff = new Java2DFrameConverter();
            BufferedImage bufferedImage = converterBuff.convert(grabbedImage1);
            System.out.println(bufferedImage);
               /* try {
                    ImageIO.write(bufferedImage, "jpg", new File(Paths.get("").toAbsolutePath() + "/build/ressources/main/tensorPics/test.jpg"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }*/

            int w = grabbedImage1.imageWidth;
            int h = grabbedImage1.imageHeight;

            Mat mat = javaCVConv.convert(grabbedImage1);
            opencv_imgproc.cvtColor(mat, javaCVMat, COLOR_BGR2BGRA);
            if (buffer == null) {
                buffer = javaCVMat.createBuffer();
            }
            System.out.println(buffer);
            PixelBuffer<ByteBuffer> pb = new PixelBuffer<ByteBuffer>(w, h, buffer, formatByte);
            final WritableImage wi = new WritableImage(pb);
            videoView = new ImageView(wi);
            return videoView;
            /*TilePane cam = new TilePane();
            primary.setScene(new Scene(cam, 600, 600));
            primary.show();*/

            /*Platform.runLater(() -> videoView.setImage(wi));
            videoView.setImage(wi);
            System.out.println("la");
            System.out.println(wi);*/
            //return videoView;
               /* WritableImage writableImage = new WritableImage(600, 600);
                WritableImage writableImageBuff = SwingFXUtils.toFXImage(bufferedImage, writableImage);*/

                /*while ((grabbedImage1 = grabber.grab()) != null) {


                }*/
        } catch (Exception e) {
            System.out.println("problem of display image");
        }
        try {
            grabber.stop();
            ;
        } catch (Exception e) {
            System.out.println("problem stop");
        }
        return null;
    }

    /*@Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("dans start");
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        try {
            grabber.start();
        } catch (Exception e) {
            System.out.println("grabber problem");
        }
        System.out.println("commence treatment");
        IplImage grabbedImage = null;
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        try {
            grabbedImage = converter.convert(grabber.grab());
        } catch (Exception e) {
            System.out.println("problem of conversion image");
        }
        System.out.println(grabbedImage);


        Frame grabbedImage1;

        try {
            grabbedImage1 = grabber.grab();
            System.out.println(grabbedImage1);
            Java2DFrameConverter converterBuff = new Java2DFrameConverter();
            BufferedImage bufferedImage = converterBuff.convert(grabbedImage1);
            System.out.println(bufferedImage);
               *//* try {
                    ImageIO.write(bufferedImage, "jpg", new File(Paths.get("").toAbsolutePath() + "/build/ressources/main/tensorPics/test.jpg"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }*//*

            int w = grabbedImage1.imageWidth;
            int h = grabbedImage1.imageHeight;

            Mat mat = javaCVConv.convert(grabbedImage1);
            opencv_imgproc.cvtColor(mat, javaCVMat, COLOR_BGR2BGRA);
            if (buffer == null) {
                buffer = javaCVMat.createBuffer();
            }
            System.out.println(buffer);
            PixelBuffer<ByteBuffer> pb = new PixelBuffer<ByteBuffer>(w, h, buffer, formatByte);
            final WritableImage wi = new WritableImage(pb);
            videoView = new ImageView(wi);
            Platform.runLater(() -> videoView.setImage(wi));
            videoView.setImage(wi);
            System.out.println("la");
            System.out.println(wi);
               *//* WritableImage writableImage = new WritableImage(600, 600);
                WritableImage writableImageBuff = SwingFXUtils.toFXImage(bufferedImage, writableImage);*//*

                *//*while ((grabbedImage1 = grabber.grab()) != null) {


                }*//*

            Group root = new Group();
            Scene scene = new Scene(root);
            scene.setFill(Color.BLACK);
            HBox box = new HBox();

            root.getChildren().add(box);

            primaryStage.setTitle("ImageView");
            primaryStage.setWidth(415);
            primaryStage.setHeight(200);
            primaryStage.setScene(scene);
            primaryStage.sizeToScene();
            primaryStage.show();

        } catch (Exception e) {
            System.out.println("problem of display image");
        }
        try {
            grabber.stop();
            ;
        } catch (Exception e) {
            System.out.println("problem stop");
        }
    }*/

    public static void main(String[] args) {
        Application.launch(args);
    }
}
