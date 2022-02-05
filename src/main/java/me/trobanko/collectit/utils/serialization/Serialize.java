package me.trobanko.collectit.utils.serialization;

import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.util.Base64;

public class Serialize {

    public static <T> String bukkitSerialize(T t){
        String encodedObject = null;
        
        try{
            ByteArrayOutputStream io = new ByteArrayOutputStream();
            BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);
            os.writeObject(t);
            os.flush();
            
            byte[] serializeObject = io.toByteArray();
            
            encodedObject = Base64.getEncoder().encodeToString(serializeObject);
        } catch (IOException e){
            e.printStackTrace();
        }
        
        return encodedObject;
    }

    public static <T> String normalSerialize(T t){
        String encodedObject = null;

        try{
            ByteArrayOutputStream io = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(io);
            os.writeObject(t);
            os.flush();

            byte[] serializeObject = io.toByteArray();

            encodedObject = Base64.getEncoder().encodeToString(serializeObject);
        } catch (IOException e){
            e.printStackTrace();
        }

        return encodedObject;
    }

    public static <T> T bukkitDeserialize(String object){
        byte[] deSerializedByte =  Base64.getDecoder().decode(object);

        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(deSerializedByte);
            BukkitObjectInputStream objectInputStream = new BukkitObjectInputStream(inputStream);

            return (T) objectInputStream.readObject();

        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T normalDeserialize(String object){
        byte[] deSerializedByte =  Base64.getDecoder().decode(object);

        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(deSerializedByte);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            return (T) objectInputStream.readObject();

        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }

        return null;
    }
}
