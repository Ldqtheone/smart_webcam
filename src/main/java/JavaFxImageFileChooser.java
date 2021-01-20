import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class JavaFxImageFileChooser {

    private final Desktop desktop = Desktop.getDesktop();

    public JavaFxImageFileChooser(){
        this.createFileChooser();
    }

    public FileChooser createFileChooser(){
        FileChooser fileChooser = new FileChooser();
        configuringFileChooser(fileChooser);

        return fileChooser;
    }

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

    public void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ignored) {
        }
    }
}
