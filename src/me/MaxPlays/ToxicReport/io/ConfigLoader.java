package me.MaxPlays.ToxicReport.io;

import com.google.common.io.ByteStreams;
import me.MaxPlays.ToxicReport.ToxicReport;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;

/**
 * Created by Max_Plays on 12.08.2017.
 */
public class ConfigLoader extends ToxicReport{

    public void load(File dataFolder){
        try {
            if(!dataFolder.exists())
                dataFolder.mkdir();
            File file = new File(dataFolder, "config.yml");
            if(!file.exists()){
                file.createNewFile();
                try(InputStream is = getResourceAsStream("config.yml"); OutputStream os = new FileOutputStream(file)){
                    ByteStreams.copy(is, os);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            ToxicReport.setConfig(ConfigurationProvider.getProvider(YamlConfiguration.class).load(file));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
