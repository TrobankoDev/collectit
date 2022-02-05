package me.trobanko.collectit.database;

import me.trobanko.collectit.CollectIt;
import me.trobanko.collectit.collectorUtils.Collector;
import me.trobanko.collectit.utils.serialization.Serialize;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

import java.sql.*;
import java.util.HashMap;

public class Database {

    public static Connection getConnection(){
        Connection connection = null;
        try {
             connection = DriverManager.getConnection(CollectIt.getDatabasePath());
        } catch (SQLException exception){
            exception.printStackTrace();
        }
        
        return connection;
    }

    public static void initDatabase(){

        Connection connection = getConnection();
        PreparedStatement preparedStatement;
        try{
            preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS CollectorsData(CollectorID varchar, OwnerID varchar, Location varchar, " +
                    "BlockData varchar, CollectorLvl int, Items varchar, isPlaced bit)");
            preparedStatement.execute();

            connection.close();
        } catch (SQLException exception){
            exception.printStackTrace();
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static boolean createCollector(String CollectorID, String OwnerID, Location location, BlockData blockData, int CollectorLevel, boolean isPlaced){
        Connection connection = getConnection();
        PreparedStatement preparedStatement;

        try {
            preparedStatement = connection.prepareStatement("INSERT INTO CollectorsData(CollectorID, OwnerID, location, blockData, CollectorLvl, isPlaced) " +
                    "VALUES (?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, CollectorID);
            preparedStatement.setString(2, OwnerID);
            preparedStatement.setString(3, Serialize.bukkitSerialize(location));
            preparedStatement.setString(4, blockData.getAsString());
            preparedStatement.setInt(5, CollectorLevel);
            preparedStatement.setBoolean(6, isPlaced);
            preparedStatement.execute();
            connection.close();
            return true;
        }catch (SQLException e){
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }

    public static void addCollectors(){
        Connection connection = getConnection();
        PreparedStatement preparedStatement;

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM CollectorsData");

            ResultSet rows = preparedStatement.executeQuery();
            while(rows.next()){
                String CollectorID = rows.getString("CollectorID");
                String OwnerID = rows.getString("OwnerID");
                Location location = Serialize.bukkitDeserialize(rows.getString("location"));
                BlockData blockData = Bukkit.createBlockData(rows.getString("blockData"));
                int collectorLevel = rows.getInt("CollectorLvl");
                boolean isPlaced = rows.getBoolean("isPlaced");
                if(rows.getString("Items") == null){
                    CollectIt.getCollectors().add(new Collector(CollectorID, OwnerID, location, blockData, collectorLevel, isPlaced));
                }else{
                    HashMap<String, Integer> storedItems = Serialize.normalDeserialize(rows.getString("Items"));
                    CollectIt.getCollectors().add(new Collector(CollectorID, OwnerID, location, blockData, collectorLevel, isPlaced ,storedItems));
                }
            }

            System.out.println("Loaded Collectors!");
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }

    public static void updateContainersTimer(){

        Bukkit.getScheduler().runTaskTimer(CollectIt.getPlugin(), () ->
        {
            Connection connection = getConnection();
            PreparedStatement statement;
            for (Collector c : CollectIt.getCollectors()) {
                if(!c.isPlaced()) break;
                try {
                    statement = connection.prepareStatement("UPDATE CollectorsData SET Items = ? WHERE CollectorID = ?");
                    statement.setString(1, Serialize.normalSerialize(c.getStoredItems()));
                    statement.setString(2, c.getCollectorUUID());
                    statement.execute();
                    connection.close();
                } catch (SQLException e) {
                    try {
                        connection.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    e.printStackTrace();
                }


            }
        }, 0L, 20*60*5L);


    }

    public static void updateContainers(){

        Connection connection = getConnection();
        PreparedStatement statement;
        for (Collector c : CollectIt.getCollectors()) {
            try {
                statement = connection.prepareStatement("UPDATE CollectorsData SET Items = ? WHERE CollectorID = ?");
                statement.setString(1, Serialize.normalSerialize(c.getStoredItems()));
                statement.setString(2, c.getCollectorUUID());
                statement.execute();
                connection.close();
            } catch (SQLException e) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            }


        }


    }

    public static void setNotPlaced(Collector c){
        Connection connection = getConnection();
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement("UPDATE CollectorsData SET isPlaced = ? WHERE CollectorID = ?");
            statement.setBoolean(1, false);
            statement.setString(2, c.getCollectorUUID());
            statement.execute();
            connection.close();
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public static void setPlaced(Collector c){
        Connection connection = getConnection();
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement("UPDATE CollectorsData SET isPlaced = ?, OwnerID = ?, location = ? WHERE CollectorID = ?");
            statement.setBoolean(1, true);
            statement.setString(2, c.getOwnerUUID());
            statement.setString(3, Serialize.bukkitSerialize(c.getLocation()));
            statement.setString(4, c.getCollectorUUID());
            statement.execute();
            connection.close();
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

}
