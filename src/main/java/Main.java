import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main extends Application {

    /**
     * Function for command Line (Story 1)
     * @return
     */
    public static String recoveryLineCommand() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Veuillez saisir le chemin de l'image :");
        String filename = sc.nextLine();
        sc.close();
        return filename;
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Start method for JavaFX
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        TensorFlowBuilder tensorFlowBuilder = new TensorFlowBuilder(primaryStage);
        tensorFlowBuilder.launchProgram();
    }
}
