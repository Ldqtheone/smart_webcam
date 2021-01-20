import org.tensorflow.Tensor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ImageDesc {

    TFUtils utils;
    List<String> labels;

    public ImageDesc(){
        utils = new TFUtils();
        labels = readAllLinesOrExit(PathFunctions.getLabelsPath());
    }

    public static List<String> readAllLinesOrExit(Path path) {
        try {
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Failed to read [" + path + "]: " + e.getMessage());
            System.exit(0);
        }
        return null;
    }

    private int maxIndex(float[][] probabilities) {
        int best = 0;
        for (int i = 1; i < probabilities[0].length; ++i) {
            if (probabilities[0][i] > probabilities[0][best]) {
                best = i;
            }
        }
        return best;
    }

    private String[] checkProbability(byte[] modelByte,Tensor input){
        Tensor model = utils.executeModelFromByteArray(modelByte, input);
        float[][] probability = new float[1][(int) model.shape()[1]];

        model.copyTo(probability);

        int bestLabelIdx = this.maxIndex(probability);

        System.out.printf("BEST MATCH: %s (%.2f%% likely)%n",
                labels.get(bestLabelIdx),
                probability[0][bestLabelIdx] * 100f);

        //String[] result =

        return new String[]{"BEST MATCH: " + labels.get(bestLabelIdx) + " (" + probability[0][bestLabelIdx] * 100f + "% likely)", labels.get(bestLabelIdx), Float.toString(probability[0][bestLabelIdx] * 100f)};
    }

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
