import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class JavaFxImageFileChooser {

    private final Desktop desktop = Desktop.getDesktop();

    public JavaFxImageFileChooser(){
        this.createFileChooser();
    }

    /**
     * Class custom FileChooser
     * @return fileChooser
     */
    public FileChooser createFileChooser(){
        FileChooser fileChooser = new FileChooser();
        configuringFileChooser(fileChooser);

        return fileChooser;
    }

    /**
     * Configure FileChooser and assign only Jpg extension
     * @param fileChooser
     */
    private void configuringFileChooser(FileChooser fileChooser) {
        // Set title for FileChooser
        fileChooser.setTitle("Select Pictures");

        // Set Initial Directory
        // fileChooser.setInitialDirectory(new File(String.valueOf(PathFunctions.getPicturePath())));

        // Add Extension Filters
        fileChooser.getExtensionFilters().addAll(//
                new FileChooser.ExtensionFilter("JPG", "*.jpg")
        );//
    }

    /**
     * Open selected img
     * @param file
     */
    public void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ignored) {
        }
    }
}
