import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static String recoveryLineCommand() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Veuillez saisir le chemin de l'image :");
        String filename = sc.nextLine();
        sc.close();
        return filename;
    }

    public static void main(String[] args) {
        ImageDesc imgDesc = new ImageDesc();

        // recovery line in the shell
        String filename = recoveryLineCommand();

        // path of File
        Path pahFile = PathFunctions.createPathFile(filename);

        imgDesc.imgtoByteArray(pahFile);
    }
}
