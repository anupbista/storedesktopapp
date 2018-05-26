package utils;

import java.io.*;
import java.util.Properties;

public class Utils {

    private static File getPropFile(){
        String basePath = Utils.class.getResource("../").getFile();
        File propertiesFile = new File(basePath, File.separator+ "prop.properties");
        if( !propertiesFile.exists()){
            try {
                propertiesFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return propertiesFile;
    }

    private static Properties getProp(){
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(getPropFile()));
            return properties;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String readPropValue(String key){
        Properties prop = getProp();
        return (String) prop.get(key);
    }

    public static void updatePropValue(String key, String value){
        Properties prop = getProp();
        prop.put(key, value);
        try {
            prop.store(new FileOutputStream(getPropFile()),null);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
