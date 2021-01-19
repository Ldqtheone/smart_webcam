import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("Veuillez saisir le chemin de l'image :");
        String filename = sc.nextLine();
        System.out.println("Vous avez saisi le chemin : " + filename);
        sc.close();
        // path
        Path currentWorkingDir = Paths.get("").toAbsolutePath();
        System.out.println(currentWorkingDir + "/inception5h/tensorPics/" + filename);
    }
}
