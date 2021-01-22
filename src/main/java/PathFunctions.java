import java.nio.file.Path;
import java.nio.file.Paths;

public class PathFunctions {

    /**
     * Get the Labels file path
     * @return
     */
    public static Path getLabelsPath(){
        Path currentWorkingDir = Paths.get("").toAbsolutePath();

        String file = currentWorkingDir + "/inception5h/labels.txt";
        return Paths.get(file);
    }

    /**
     * Get the model file path
     * @return
     */
    public static Path getModelPath(){
        Path currentWorkingDir = Paths.get("").toAbsolutePath();

        String file = currentWorkingDir + "/inception5h/tensorflow_inception_graph.pb";
        return Paths.get(file);
    }

    /**
     * Get the path of current img selected
     * @param filename
     * @return
     */
    public static Path createPathFile(String filename) {
        Path currentWorkingDir = Paths.get("").toAbsolutePath();
        String file = currentWorkingDir + "/src/main/resources/tensorPics/" + filename + ".jpg";
        return Paths.get(file);
    }

    /**
     * Get the root resources path
     * @return
     */
    public static Path getPicturePath() {
        Path currentWorkingDir = Paths.get("").toAbsolutePath();
        String file = currentWorkingDir + "/src/main/resources";
        return Paths.get(file);
    }
}
