import static org.bytedeco.opencv.global.opencv_imgproc.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import org.bytedeco.opencv.opencv_core.Mat;
import org.opencv.core.Core;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        int colorFilter;
        switch (colorValue) {
            case "Orange":
                colorFilter = Imgproc.COLORMAP_HOT;
                break;
            case "Vert":
                colorFilter = Imgproc.COLORMAP_SUMMER;
                break;
            case "Bleu":
                colorFilter = Imgproc.COLORMAP_WINTER;
                break;
            case "Rose":
                colorFilter = Imgproc.COLORMAP_SPRING;
                break;
            case "Gris":
                colorFilter = Imgproc.COLORMAP_BONE;
                break;
            default:
                colorFilter = 0;
                break;
        }

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
    public static void frameFilter(String imgToFilter) throws FilterException, IOException {

        BufferedImage image = ImageIO.read(new File(imgToFilter));
        BufferedImage overlay = ImageIO.read(new File(PathFunctions.getPicturePath().toString() + "/filters/cadreTransparent.png"));

        // create the new image, canvas size is the max. of both image sizes
        int w = Math.max(image.getWidth(), overlay.getWidth());
        int h = Math.max(image.getHeight(), overlay.getHeight());
        BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        // paint both images, preserving the alpha channels
        Graphics g = combined.getGraphics();
        g.drawImage(image, (overlay.getWidth() / 2) - (image.getWidth() / 2), (overlay.getHeight() / 2) - (image.getHeight() / 2), null);
        g.drawImage(overlay, 0, 0, null);

        g.dispose();

        // Save as new image
        String newPath = imgToFilter.replace(".jpg", ".png");
        newPath = imgToFilter.replace(".png", "_frame.png");

        ImageIO.write(combined, "PNG", new File(newPath));
    }

    public static void stampFilter(String imgToFilter, BufferedImage overlay, int xUser, int yUser) throws FilterException, IOException{

        BufferedImage image = ImageIO.read(new File(imgToFilter));
        //BufferedImage overlay = choosenStampp;

        // create the new image, canvas size is the max. of both image sizes
        int w = Math.max(image.getWidth(), overlay.getWidth());
        int h = Math.max(image.getHeight(), overlay.getHeight());
        BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        // paint both images, preserving the alpha channels
        Graphics g = combined.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.drawImage(overlay, xUser, yUser, null); //Add the stamp at the wanted x and y

        g.dispose();

        // Save as new image
        System.out.println("1 : " + imgToFilter);

        String newPath = imgToFilter.replace(".jpg", ".png");
        newPath = imgToFilter.replace(".png", "_stamp.png");


        System.out.println("2 : " + newPath);

        ImageIO.write(combined, "PNG", new File(newPath));
    }
}