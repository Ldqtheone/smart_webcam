import static org.bytedeco.opencv.global.opencv_imgproc.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import org.bytedeco.opencv.opencv_core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Class to apply filter
 */
public class Filters {

    /**
     * Method to apply color filter
     * @param imagePath path Input, file name
     * @throws FilterException return exception if image not found
     */
    public static void filterColor(String imagePath, String colorValue) throws FilterException {

        int colorFilter = switch (colorValue) {
            case "Orange" -> Imgproc.COLORMAP_HOT;
            case "Vert" -> Imgproc.COLORMAP_SUMMER;
            case "Bleu" -> Imgproc.COLORMAP_WINTER;
            case "Rose" -> Imgproc.COLORMAP_SPRING;
            case "Gris" -> Imgproc.COLORMAP_BONE;
            default -> 0;
        };

        Mat image = imread(imagePath);
        if (image == null)
            throw new FilterException("Image not found !");

        applyColorMap(image, image, colorFilter);
        String newPath = imagePath.replace(".jpg", "_filter.jpg");
        imwrite(newPath, image);
    }

    /**
     * Method to apply zeteam filter
     * @throws FilterException return exception if image not found
     */
    /*public static void frameFilter() throws FilterException {
        Mat background = imread('field.jpg');
        Mat overlay = imread('dice.png');

        Mat added_image = overlayImage(background,0.4,overlay,0.1,0);

        imwrite('combined.jpg', added_image);
    }*/
}