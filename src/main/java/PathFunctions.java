import java.nio.file.Path;
import java.nio.file.Paths;

public class PathFunctions {

    public static Path getLabelsPath(){
        Path currentWorkingDir = Paths.get("").toAbsolutePath();

        String file = currentWorkingDir + "/inception5h/labels.txt";
        return Paths.get(file);
    }

    public static Path getModelPath(){
        Path currentWorkingDir = Paths.get("").toAbsolutePath();

        String file = currentWorkingDir + "/inception5h/tensorflow_inception_graph.pb";
        return Paths.get(file);
    }

    public static Path createPathFile(String filename) {
        Path currentWorkingDir = Paths.get("").toAbsolutePath();
        String file = currentWorkingDir + "/inception5h/tensorPics/" + filename + ".jpg";
        return Paths.get(file);
    }
}
