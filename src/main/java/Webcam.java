
import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.opencv_core.IplImage;
import java.io.File;

import static org.bytedeco.opencv.global.opencv_core.cvFlip;
import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvSaveImage;

import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bytedeco.javacv.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.indexer.*;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter.ToIplImage;

public class Webcam {

  //  static final int INTERVAL = 100;///you may use interval
     CanvasFrame canvas = new CanvasFrame("Web Cam");

    public void Test() {
        canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    }

    public static void webcam() {
        System.out.println("la");
            OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
            try {
                grabber.start();
            }
            catch (Exception e){

            }
            //opencv_core.IplImage grabbedImage = grabber.grab();

           // OpenCVFrameConverter converter =  new OpenCVFrameConverter();
            //converter.convert(grabber.grab());
        IplImage grabbedImage = null;
            OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
            try {
               grabbedImage = converter.convert(grabber.grab());
            }catch (Exception e){

            }

            CanvasFrame canvasFrame = new CanvasFrame("Video with JavaCV");
            canvasFrame.setCanvasSize(grabbedImage.width(), grabbedImage.height());
            grabber.setFrameRate(grabber.getFrameRate());

         /*   FFmpegFrameRecorder recorder = new FFmpegFrameRecorder("mytestvideo.mp4", grabber.getImageWidth(), grabber.getImageHeight());
            recorder.setFormat("mp4");
            recorder.setFrameRate(30);
            recorder.setVideoBitrate(10 * 1024 * 1024);
*/
            try {
             //   recorder.start();
            }
            catch (Exception e) {

            }
            Frame grabbedImage1;
            System.out.println("la");
            try {
            while (canvasFrame.isVisible() && (grabbedImage1 = grabber.grab()) != null) {
                canvasFrame.showImage(grabbedImage1);
               /* try {
                    recorder.record(grabbedImage1);
                }
                catch (Exception e) {

                }*/
            }}catch (Exception e) {

            }
            try {
              //  recorder.stop();
                grabber.stop();
            }
            catch (Exception e) {
                canvasFrame.dispose();
            }

      /*  OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        grabber.start();
        IplImage grabbedImage = grabber.grab();

        CanvasFrame canvasFrame = new CanvasFrame("Cam");
        canvasFrame.setCanvasSize(grabbedImage.width(), grabbedImage.height());

        System.out.println("framerate = " + grabber.getFrameRate());
        grabber.setFrameRate(grabber.getFrameRate());
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(FILENAME,  grabber.getImageWidth(),grabber.getImageHeight());

        recorder.setVideoCodec(13);
        recorder.setFormat("mp4");
        recorder.setPixelFormat(avutil.PIX_FMT_YUV420P);
        recorder.setFrameRate(30);
        recorder.setVideoBitrate(10 * 1024 * 1024);

        recorder.start();
        while (canvasFrame.isVisible() && (grabbedImage = grabber.grab()) != null) {
            canvasFrame.showImage(grabbedImage);
            recorder.record(grabbedImage);
        }
        recorder.stop();
        grabber.stop();
        canvasFrame.dispose();*/

       /* new File("images").mkdir();

        FrameGrabber grabber = new OpenCVFrameGrabber(0); // 1 for next camera
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        IplImage img;
        int i = 0;
        try {
            grabber.start();

            while (true) {
                Frame frame = grabber.grab();

                img = converter.convert(frame);

                //the grabbed frame will be flipped, re-flip to make it right
                cvFlip(img, img, 1);// l-r = 90_degrees_steps_anti_clockwise

                //save
                cvSaveImage("images" + File.separator + (i++) + "-aa.jpg", img);

                canvas.showImage(converter.convert(img));

                Thread.sleep(INTERVAL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

   /* public static void main() {
        Webcam gs = new Webcam();
        Thread th = new Thread(gs);
        th.start();
    }*/
}
