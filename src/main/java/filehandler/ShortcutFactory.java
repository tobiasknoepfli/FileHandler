package filehandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class ShortcutFactory {

    public static void createDesktopShortcut(String source, String linkName) throws FileNotFoundException {
        String linkPath = System.getProperty("user.home") + "/Desktop/" + linkName;
        createShortcut(source, linkPath);
    }

    public static void createShortcut(String source, String linkPath) throws FileNotFoundException {
        File sourceFile = new File(source);
        if (!sourceFile.exists()) {
            throw new FileNotFoundException("The Path: " + sourceFile.getAbsolutePath() + " does not exist!");
        }
        try {
            source = sourceFile.getAbsolutePath();

            String vbsCode = String.format(
                    "Set wsObj = WScript.CreateObject(\"WScript.shell\")%n"
                            + "scPath = \"%s\"%n"
                            + "Set scObj = wsObj.CreateShortcut(scPath)%n"
                            + "\tscObj.TargetPath = \"%s\"%n"
                            + "scObj.Save%n",
                    linkPath, source
            );

            newVBS(vbsCode);
        } catch (IOException | InterruptedException e) {
            System.err.println("Could not create and run VBS!");
            e.printStackTrace();
        }
    }


    public static void createDesktopShortcut(String source, String linkName, String iconPath) throws FileNotFoundException {
        String linkPath = System.getProperty("user.home") + "/Desktop/" + linkName;
        createShortcut(source, linkPath, iconPath);
    }

    public static void createShortcut(String source, String linkPath, String iconPath) throws FileNotFoundException {
        File sourceFile = new File(source);
        File iconFile = new File(iconPath);
        if (!sourceFile.exists())
            throw new FileNotFoundException("The Path: " + sourceFile.getAbsolutePath() + " does not exist!");
        if (!iconFile.exists())
            throw new FileNotFoundException("The Path: " + iconFile.getAbsolutePath() + " does not exist!");
        try {
            source = sourceFile.getAbsolutePath();
            iconPath = iconFile.getAbsolutePath();

            String vbsCode = String.format(
                    "Set wsObj = WScript.CreateObject(\"WScript.shell\")%n"
                            + "scPath = \"%s\"%n"
                            + "Set scObj = wsObj.CreateShortcut(scPath)%n"
                            + "\tscObj.TargetPath = \"%s\"%n"
                            + "\tscObj.IconLocation = \"%s\"%n"
                            + "scObj.Save%n",
                    linkPath, source, iconPath
            );

            newVBS(vbsCode);
        } catch (IOException | InterruptedException e) {
            System.err.println("Could not create and run VBS!");
            e.printStackTrace();
        }
    }

    /*
     * Creates a VBS file with the passed code and runs it, deleting it after the run has completed
     */
    private static void newVBS(String code) throws IOException, InterruptedException {
        File script = File.createTempFile("scvbs", ".vbs"); // File where script will be created

        // Writes to script file
        FileWriter writer = new FileWriter(script);
        writer.write(code);
        writer.close();

        Process p = Runtime.getRuntime().exec("wscript \"" + script.getAbsolutePath() + "\""); // executes vbs code via cmd
        p.waitFor(); // waits for process to finish
        if (!script.delete()) { // deletes script
            System.err.println("Warning Failed to delete temporary VBS File at: \"" + script.getAbsolutePath() + "\"");
        }
    }
}