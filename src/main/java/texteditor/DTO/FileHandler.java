package texteditor.DTO;

import texteditor.GUI.Global;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import com.groupdocs.viewer.Viewer;
import com.groupdocs.viewer.results.FileInfo;


public class FileHandler {
    public static void createDefaultWorkSpaceFolder(){
        File defaultWorkspace = new File(getDefaultWorkSpaceFolderPath());
        System.out.println("Default workspace folder: " + defaultWorkspace.getAbsolutePath());

        if (!defaultWorkspace.exists()) {
            if(defaultWorkspace.mkdirs()){
                createFile(defaultWorkspace.getAbsolutePath() + "\\peekaboo", true);
                System.out.println("Default successfully workspace created at " + defaultWorkspace.getAbsolutePath());
            } else {
                System.err.println("Failed to create default workspace at " + defaultWorkspace.getAbsolutePath());
            }
        } else{
            System.out.println("Default workspace already exists at " + defaultWorkspace.getAbsolutePath());
        }
    }

    @SuppressWarnings("deprecation")
    public static String getDefaultWorkSpaceFolderPath(){
        String defaultWorkSpaceFolderPath = null;
        String operatingSystem = getOperatingSystem();

        if (operatingSystem.equals("windows")) {
            try {
                Process process = Runtime.getRuntime().exec("reg query \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders\" /v personal");
                process.waitFor();

                InputStream in = process.getInputStream();
                byte[] bytes = new byte[in.available()];
                int a = in.read(bytes);
                System.out.println("Bytes read: " + a);
                in.close();

                defaultWorkSpaceFolderPath = new String(bytes).split("\\s\\s+")[4] + "\\default-workspace";
            } catch (Throwable t) {
                Global.printErrorAndFinish("An error occurred while getting the user's documents folder.", new Exception(t));
            }
        } else {
            defaultWorkSpaceFolderPath = System.getProperty("user.home") + "/Documents/default-workspace/";
        }

        return defaultWorkSpaceFolderPath;
    }

    private static String getOperatingSystem(){
        String operatingSystem = System.getProperty("os.name").toLowerCase();
        if(operatingSystem.contains("win")){ return "windows"; }
        if(operatingSystem.contains("mac")){ return "mac"; }
        if(operatingSystem.contains("nix") || operatingSystem.contains("nux")){ return "linux"; }
        return "other";
    }

    public static void createFile(String filePath, boolean createControlFile){
        File file = new File(filePath);
        try{
            if(file.createNewFile()){
                if(createControlFile){
                    Path path = Paths.get(file.getAbsolutePath());
                    Files.setAttribute(path, "dos:hidden", true);
                }
                System.out.println(createControlFile ? "File successfully created!" : "Control file successfully created!");
            }else{
                System.out.println("File already exists.");
            }
        } catch (Exception e) {
            Global.printErrorAndFinish("An error occurred while creating the file " + file.getName(), e);
        }
    }

    public static void createFile(String filePath, List<String> content){
        File file = new File(filePath);
        try{
            if(file.createNewFile()){
                writeToFile(filePath, content, false);
                System.out.println("File successfully created!");
            }else{
                System.out.println("File already exists.");
            }
        } catch (Exception e) {
            Global.printErrorAndFinish("An error occurred while creating the file " + file.getName(), e);
        }
    }

    public static List<String> readFromFile(File file){
        List<String> fileContent =  new ArrayList<>();

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file.getAbsolutePath()))){
            String line;

            while((line = bufferedReader.readLine()) != null){
                fileContent.add(line);
            }
        } catch(Exception e){
            Global.printErrorAndFinish("An error occurred while reading from file " + file.getName(), e);
        }

        return fileContent;
    }

    public static void writeToFile(String filePath, List<String> content, boolean append){
        File file = new File(filePath);
        if(!file.exists()){ createFile(filePath, content); }

        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath, append))){
            for(String line : content){
                bufferedWriter.write(line + "\n");
            }
        } catch (Exception e){
            Global.printErrorAndFinish("An error occurred while writing to file " + file.getName(), e);
        }
    }

    public static void deleteFile(String filePath){
        File file = new File(filePath);

        if(file.delete()){
            System.out.println("File deleted successfully.");
        } else{
            System.err.println("Failed to delete file.");
        }
    }

    public static void deleteDirectory(File directory){
        File[] files = directory.listFiles();

        if(files != null){
            for(File file : files){
                if(file.isDirectory()){
                    deleteDirectory(file);
                } else{
                    deleteFile(file.getAbsolutePath());
                }
            }
        }

        if(directory.delete()){
            System.out.println("Directory deleted successfully.");
        } else{
            System.err.println("Failed to delete directory.");
        }
    }

    private static class Encryptor {
        public static boolean verifyFileEncryption(File file){
            try(Viewer viewer = new Viewer(String.valueOf(file))){
                FileInfo fileInfo = viewer.getFileInfo();

                return (fileInfo.isEncrypted());
            } catch(Exception e){
                System.err.println("Error verifying file encryption.\nMessage: " + e.getMessage());
            }

            return false;
        }

        public static byte[] encryptFile(File file){
            try{
                SecretKey myDesKey = KeyGenerator.getInstance("DES").generateKey();
                Cipher cipher = Cipher.getInstance("DES");

                cipher.init(Cipher.ENCRYPT_MODE, myDesKey);

                return cipher.doFinal(Files.readAllBytes(file.toPath()));
            } catch(Exception e){
                System.err.println("Error encrypting file.\nMessage: " + e.getMessage());
            }

            return null;
        }

        public static List<String> decryptFile(String filePath){
            try{
                SecretKey myDesKey = KeyGenerator.getInstance("DES").generateKey();
                Cipher desCipher = Cipher.getInstance("DES");

                desCipher.init(Cipher.DECRYPT_MODE, myDesKey);
                byte[] decryptedFileBytes = desCipher.doFinal(Files.readAllBytes(new File(filePath).toPath()));

                return (List.of(new String(decryptedFileBytes).split("\n")));
            } catch(Exception e){
                System.err.println("Error decrypting file.\nMessage: " + e.getMessage());
            }

            System.out.println("File is null.");
            return null;
        }
    }
}
