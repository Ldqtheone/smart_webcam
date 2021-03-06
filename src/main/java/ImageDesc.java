import org.tensorflow.Tensor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ImageDesc {

    static TFUtils utils = new TFUtils();
    static List<String> labels = readAllLinesOrExit(PathFunctions.getLabelsPath());

    /**
     * Class Image Description
     */
    public ImageDesc(){
    }

    /**
     * Read all line of selected file
     * @param path
     * @return List<String>
     */
    public static List<String> readAllLinesOrExit(Path path) {
        try {
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Failed to read [" + path + "]: " + e.getMessage());
            System.exit(0);
        }
        return null;
    }

    /**
     * Loop for searching the max index of the List
     * @param probabilities
     * @return integer
     */
    public int maxIndex(float[][] probabilities) {
        int best = 0;
        for (int i = 1; i < probabilities[0].length; ++i) {
            if (probabilities[0][i] > probabilities[0][best]) {
                best = i;
            }
        }
        return best;
    }

    /**
     * Check the probability of the tensor converted in Float[]
     * @param modelByte
     * @param input
     * @return description of best match
     */
    public String[] checkProbability(byte[] modelByte, Tensor input){
        Tensor model = utils.executeModelFromByteArray(modelByte, input);
        float[][] probability = new float[1][(int) model.shape()[1]];

        model.copyTo(probability);

        int bestLabelIdx = maxIndex(probability);

        System.out.printf("BEST MATCH: %s (%.2f%% likely)%n",
                labels.get(bestLabelIdx),
                probability[0][bestLabelIdx] * 100f);

        return new String[]{"BEST MATCH: " + labels.get(bestLabelIdx) + " (" + probability[0][bestLabelIdx] * 100f + "% likely)", labels.get(bestLabelIdx), Float.toString(probability[0][bestLabelIdx] * 100f)};
    }

    /**
     * Convert Img and Model file to Tab Byte
     * @param pathFile
     * @return
     */
    public String[] imgtoByteArray(Path pathFile){
        // convert picture in a byte
        byte[] tabByte = null;

        try {
            tabByte = Files.readAllBytes(pathFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (tabByte != null) {
            Tensor input = utils.byteBufferToTensor(tabByte);

            byte[] modelByte = null;
            Path pathModel = PathFunctions.getModelPath();

            try {
                modelByte = Files.readAllBytes(pathModel);
            }catch (IOException e){
                e.printStackTrace();
            }

            if(modelByte != null){
                return this.checkProbability(modelByte, input);

            }
        }
        return new String[]{"no match","",""};
    }


}
