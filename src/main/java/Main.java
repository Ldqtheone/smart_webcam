import org.tensorflow.Tensor;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static Path createPathFile(String filename) {
        Path currentWorkingDir = Paths.get("").toAbsolutePath();
        String[] tab = filename.split("\\.");
        if (tab[tab.length - 1].equals("jpg")) {
            String file = currentWorkingDir + "/inception5h/tensorPics/" + filename;
            Path pathfile = Paths.get(file);
            return pathfile;
        }
        return null;
    }

    public static String recoveryLineCommand() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Veuillez saisir le chemin de l'image :");
        String filename = sc.nextLine();
        sc.close();
        return filename;
    }

    public static void main(String[] args) {
        TFUtils utils = new TFUtils();

        // recovery line in the shell
        String filename = recoveryLineCommand();

        // path of File
        Path pathfile = createPathFile(filename);
        if (pathfile == null)
            return ;

        // convert picture in a byte
        byte[] tabByte = null;
        byte[] tabByteRef = null;
        try {
            tabByte = Files.readAllBytes(pathfile);
            tabByteRef = Files.readAllBytes(Path.of(Paths.get("").toAbsolutePath() + "/inception5h/tensorflow_inception_graph.pb"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (tabByte != null && tabByteRef != null) {
            Tensor input = utils.byteBufferToTensor(tabByte);
            Tensor model = utils.executeModelFromByteArray(tabByteRef, input);
            float[][] floats = new float[1][1008];
            model.copyTo(floats);
            System.out.println(floats[0][0]);


            float max = 0.00000000000000F;
            int line = 0;
            for (int i = 0; i < 1001; i++) {
                if (floats[0][i] > max) {
                    max = floats[0][i];
                    line = i + 1;
                }
            }
            System.out.println(max);
            System.out.println(line);
            //   System.out.println(utils.executeSavedModel(String.valueOf(pathfile), input));
        }
    }
}
