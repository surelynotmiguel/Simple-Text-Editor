package DTO;

import GUI.Global;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    public static void createFile(String filePath){
        //filePath += ".txt";
        File file = new File(filePath);
        try{
            if(file.createNewFile()){
                System.out.println("File successfully created!");
            }else{
                System.out.println("File already exists.");
            }
        } catch (Exception e) {
            Global.printErrorAndFinish("An error occurred while creating the file " + file.getName(), e);
        }
    }

    public static void createFile(String filePath, List<String> content, boolean writeToFile){
        //filePath += ".txt";
        File file = new File(filePath);
        try{
            if(file.createNewFile()){
                if(writeToFile){
                    writeToFile(filePath, content, false);
                }
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
        if(!file.exists()){ createFile(filePath, content, true); }

        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath, append))){
            for(String line : content){
                bufferedWriter.write(line + "\n");
            }
        } catch (Exception e){
            Global.printErrorAndFinish("An error occurred while writing to file " + file.getName(), e);
        }
    }


}
