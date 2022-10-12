package us.xylight.pvp.util;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import us.xylight.pvp.XyPVP;

import java.io.File;
import java.io.IOException;

public class ConfigUtil {
    private File file;
    private FileConfiguration config;

    public ConfigUtil(String path) {
        this.file = new File(XyPVP.getInstance().getDataFolder(), path);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            XyPVP.getInstance().saveResource(path, false);
        }

        this.config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public boolean save() {
        try {
            this.config.save(this.file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public File getFile() {
        return this.file;
    }

    public FileConfiguration getConfig() {
        return this.config;
    }
}
