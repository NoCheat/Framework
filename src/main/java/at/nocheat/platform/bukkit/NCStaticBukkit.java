package at.nocheat.platform.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * Maybe: A static access getter for API.
 * 
 * Note: Bukkit parts should have an own project / module.
 * 
 * @author mc_dev
 * 
 */
public class NCStaticBukkit {
    public static Plugin getPlugin() {
        return Bukkit.getPluginManager().getPlugin("NoCheat");
    }
}
