package com.company;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class FileManager implements Map<String, UserRecord> {

    File recordsFolder;

    public FileManager(File rf) {
         recordsFolder = rf;
         if(!recordsFolder.exists()){
             if(!recordsFolder.mkdir()){
                 System.out.println("FAILED");
             }
         }
    }

    @Override
    public int size() {
        return recordsFolder.listFiles().length;

    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        File[] records = recordsFolder.listFiles();
        for(int i = 0; i < records.length; i++){
            if (records[i].getName().equals(key)) return true;
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for(String key: keySet()){
            if(get(key).equals(value)) return true;
        }
        return false;
    }

    @Override
    public UserRecord get(Object key) {
        File[] records = recordsFolder.listFiles();
        for(int i = 0; i < records.length; i++){
            if (records[i].getName().equals(key)) {
                //read file
                try {
                    System.out.println(records[i].getName());
                    FileReader fileReader = new FileReader(records[i]);
                    String contents = "";
                    int letter = fileReader.read();
                    while(letter != -1){
                        contents += HandlerSupporter.intToChar(letter);
                        letter = fileReader.read();
                    }
                    //convert to user record
                    return stringToRecord(contents);

                }
                catch (Exception e){
                    System.out.println("Error retrieving file");
                }

            }
        }
        return null;
    }

    @Override
    public UserRecord put(String key, UserRecord value) {
        UserRecord old = get(key);
        if(containsKey(key)){
            remove(key);
        }
        File file = new File(recordsFolder.getPath() + File.separator + key);
        try {
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            String contents = recordToString(value);
            writer.write(contents);
            writer.close();
        }catch (Exception e){

        }



        return old;
    }

    @Override
    public UserRecord remove(Object key) {
        UserRecord old = get(key);
        File[] records = recordsFolder.listFiles();
        for(int i = 0; i < records.length; i++){
            if (records[i].getName().equals(key)) {
                records[i].delete();
            }
        }
        return old;
    }

    @Override
    public void putAll(Map<? extends String, ? extends UserRecord> m) {

        for(String key :m.keySet()){
            put(key, m.get(key));
        }

    }

    @Override
    public void clear() {

        File[] records = recordsFolder.listFiles();
        for(int i = 0; i < records.length; i++){
           records[i].delete();
        }



    }

    @Override
    public Set<String> keySet() {

        Set<String> keys = new HashSet<String>();
        File[] records = recordsFolder.listFiles();
        for(int i = 0; i < records.length; i++){
            keys.add(records[i].getName());
        }

        return keys;
    }

    @Override
    public Collection<UserRecord> values() {
        Collection<UserRecord> v = new LinkedList<>();
        for(String k : keySet()){
            v.add(get(k));
        }
        return v;
    }

    @Override
    public Set<Entry<String, UserRecord>> entrySet() {
        Set<Entry<String, UserRecord>> es = new HashSet<>();
        //for(String k : keySet()){
        //es.add(new get(k));
        //}
        return null;
    }

    public static UserRecord stringToRecord(String contents){
        String[] fields = contents.split("#");
        String[] vector = fields[4].split("&");
        String[] ihalf = vector[0].split("@");
        double[] interests = new double[ihalf.length];
        for(int j = 0; j < ihalf.length; j++){
            interests[j] = Double.parseDouble(ihalf[j]);
        }
        String[] ahalf = vector[1].split("@");
        double[] aptitude = new double[ahalf.length];
        for(int j = 0; j < ahalf.length; j++){
            aptitude[j] = Double.parseDouble(ahalf[j]);
        }
        return new UserRecord(fields[0], fields[1], fields[2], fields[3].equals("Y"), new ProfileVector(interests, aptitude), fields[5].equals("Y"));

    }

    public static String recordToString(UserRecord value){
        String contents = "";
        contents += value.username + "#";
        contents += value.password + "#";
        contents += value.description + "#";
        if(value.isEmployer) contents += 'Y'; else contents += "N";
        contents += "#";
        for(int i = 0; i < value.profile.interests.length; i++){
            contents += value.profile.interests[i];
            if(i < value.profile.interests.length - 1){
                contents += "@";
            }
        }
        contents += "&";
        for(int i = 0; i < value.profile.aptitude.length; i++){
            contents += value.profile.aptitude[i];
            if(i < value.profile.aptitude.length - 1){
                contents += "@";
            }
        }
        contents += "#";
        if(value.isCompleted) contents += 'Y'; else contents += "N";
        return contents;
    }
}
