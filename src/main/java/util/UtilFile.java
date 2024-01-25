package util;

import java.io.*;
import java.net.URISyntaxException;

public class UtilFile {

    private static final File USERDATA_OUTPUT_FOLDER = new File(System.getProperty("user.home"), "GOL3_UserData");

    public static void main(String[] args) throws IOException, URISyntaxException {
        String filename = "newFile2.txt";
        writeUserDataFile(new byte[] {(byte)0}, filename);
        readUserDataFile(filename);
    }

    public static File[] getUserDataFiles() {
        if (USERDATA_OUTPUT_FOLDER.exists()) {
            System.out.println(USERDATA_OUTPUT_FOLDER + " already exists");
        } else if (USERDATA_OUTPUT_FOLDER.mkdirs()) {
            System.out.println(USERDATA_OUTPUT_FOLDER + " was created");
        } else {
            System.out.println(USERDATA_OUTPUT_FOLDER + " was not created");
        }

        return USERDATA_OUTPUT_FOLDER.listFiles();
    }

    public static InputStream readUserDataFile(String filename) throws FileNotFoundException {
        File inputFile = new File(USERDATA_OUTPUT_FOLDER, filename);
        System.out.println("inputFile:" + inputFile.getAbsolutePath());
        if (!inputFile.getParentFile().exists()) {
            System.err.println("parent folder DNE");
            System.exit(1);
        }
        FileInputStream fis = new FileInputStream(inputFile);
        return fis;
    }


    public static void writeUserDataFile(byte[] bytes, String filename) throws IOException, URISyntaxException {



        if (USERDATA_OUTPUT_FOLDER.exists()) {
            System.out.println(USERDATA_OUTPUT_FOLDER + " already exists");
        } else if (USERDATA_OUTPUT_FOLDER.mkdirs()) {
            System.out.println(USERDATA_OUTPUT_FOLDER + " was created");
        } else {
            System.out.println(USERDATA_OUTPUT_FOLDER + " was not created");
        }

        File outputFile = new File(USERDATA_OUTPUT_FOLDER, filename);
        System.out.println("outputFile:" + outputFile.getAbsolutePath());
        if (!outputFile.getParentFile().exists()) {
            System.err.println("parent folder DNE");
            System.exit(1);
        }

        FileOutputStream fos = new FileOutputStream(outputFile);
        fos.write(bytes);
    }
}
