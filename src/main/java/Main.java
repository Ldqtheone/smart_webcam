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
        System.out.println(tab[0]);
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
        System.out.println(pathfile);

        // convert picture in a byte
        byte[] tabByte = null;
        try {
            tabByte = Files.readAllBytes(pathfile);

        } catch (IOException e) {
            e.printStackTrace();
        }
       // if (tabByte != null) {
      //      Tensor input = utils.byteBufferToTensor(tabByte);
         //   System.out.println(input);
       // }
    }
}
