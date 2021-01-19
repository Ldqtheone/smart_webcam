import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("Veuillez saisir le chemin de l'image :");
        String str = sc.nextLine();
        System.out.println("Vous avez saisi le chemin : " + str);
        sc.close();
    }
}
