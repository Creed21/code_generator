package v3.template;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.List;

public class Template {
    private List<Placeholder> placeholders;
    private String templateDefinition;
    private List<File> templateFile;


    public Template() {}

    public List<Placeholder> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(List<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public String getTemplateDefinition() {
        return templateDefinition;
    }

    public void setTemplateDefinition(String templateDefinition) {
        this.templateDefinition = templateDefinition;
    }

    // todo implement getting file
    public List<File> getTemplateFiles() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "JPG & GIF Images", "jpg", "gif");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " +
                    chooser.getSelectedFile().getName());
            return List.of(chooser.getSelectedFiles());
        }
        return templateFile;
    }

    public void setTemplateFile(List<File> templateFile) {
        this.templateFile = templateFile;
    }
}
