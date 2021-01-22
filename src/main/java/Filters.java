import static org.bytedeco.opencv.global.opencv_imgproc.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import org.bytedeco.opencv.opencv_core.Mat;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
     * Method to add Frame filter
     * @param imgToFilter
     * @throws FilterException
     * @throws IOException
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
        System.out.println(imgToFilter);

        String newPath = imgToFilter;

        try {
            newPath = newPath.replace(".jpg", "_frame.png");
            newPath = newPath.replace("_stamp.png", "_stamp_frame.png");
        }catch (Exception e){
            System.out.println(e);
        }

        System.out.println(newPath);

        ImageIO.write(combined, "PNG", new File(newPath));
    }

    /**
     * Method to add Stamp filter
     * @param imgToFilter
     * @param xUser
     * @param yUser
     * @throws FilterException
     * @throws IOException
     */
    public static void stampFilter(String imgToFilter, BufferedImage overlay, int xUser, int yUser) throws FilterException, IOException{

        BufferedImage image = ImageIO.read(new File(imgToFilter));

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
        System.out.println(imgToFilter);

        String newPath = imgToFilter;

        try {
            newPath = newPath.replace(".jpg", "_stamp.png");
            newPath = newPath.replace("_frame.png", "_stamp_frame.png");
        }catch (Exception e){
            System.out.println(e);
        }

        System.out.println(newPath);


        ImageIO.write(combined, "PNG", new File(newPath));
    }
}