package net.savagedev.imlib;

import net.savagedev.imlib.gui.IMTitleUpdater;
import net.savagedev.imlib.gui.compat.ReflectionTitleUpdater;
import net.savagedev.imlib.gui.holder.InteractionMenuHolder;
import net.savagedev.imlib.listeners.InventoryListeners;
import net.savagedev.imlib.modules.IMLibModuleLoader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.logging.Level;

@SuppressWarnings("unused")
public class IMLib {
    private static IMTitleUpdater titleUpdater;
    private static boolean enabled = false;
    private static IMLib instance;

    public static IMLib getInstance() {
        return instance;
    }

    private JavaPlugin plugin;

    public IMLib(final JavaPlugin plugin) {
        if (enabled) {
            return;
        }

        this.plugin = plugin;

        final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        final Path path = Paths.get("plugins", "PlayerListGUI", "compat", "CompatModule_" + version + ".jar").toAbsolutePath();

        if (Files.notExists(path)) {
            if (Files.exists(path.getParent())) {
                this.plugin.getLogger().log(Level.INFO, "New server version detected. Deleting old compat module...");
                try (DirectoryStream<Path> paths = Files.newDirectoryStream(path.getParent(), "*.jar")) {
                    for (Path child : paths) {
                        Files.deleteIfExists(child);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Files.createDirectories(path.getParent());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            this.plugin.getLogger().log(Level.INFO, "Attempting to download updated compat module for Bukkit/Spigot " + version + "...");
            try {
                final HttpsURLConnection connection = (HttpsURLConnection) new URL("https://www.savagedev.net/playerlistgui/modules/" + version + ".jar").openConnection();
                connection.setRequestProperty("User-Agent", this.plugin.getDescription().getName() + "/" + this.plugin.getDescription().getVersion());

                try (InputStream input = connection.getInputStream()) {
                    Files.copy(input, path);
                }
            } catch (IOException e) {
                this.plugin.getLogger().log(Level.WARNING, "Failed to download compat module! Unsupported server version? Attempting to use internal reflection module.");
                titleUpdater = new ReflectionTitleUpdater();
            }
        }

        if (titleUpdater == null) {
            this.plugin.getLogger().log(Level.INFO, "Attempting to load compat module...");
            try (IMLibModuleLoader loader = new IMLibModuleLoader(new URL[]{path.toUri().toURL()}, this.getClass().getClassLoader())) {
                titleUpdater = loader.getTitleUpdater();
            } catch (IOException e) {
                this.plugin.getLogger().log(Level.SEVERE, "Failed to load compat module: ", e);
            }
        }

        this.plugin.getServer().getPluginManager().registerEvents(new InventoryListeners(), plugin);
        instance = this;
        enabled = true;
    }

    public void closeAll() {
        Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
        if (players.isEmpty()) {
            return;
        }
        for (Player player : players) {
            final InventoryHolder holder = player.getOpenInventory().getTopInventory().getHolder();
            if (holder instanceof InteractionMenuHolder) {
                ((InteractionMenuHolder) holder).getMenu().close(player);
            }
        }
    }

    public void cancelTask(int id) {
        this.plugin.getServer().getScheduler().cancelTask(id);
    }

    public int scheduleAsync(Runnable runnable, long delay, long period) {
        return (this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(this.plugin, runnable, delay, period)).getTaskId();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public IMTitleUpdater getTitleUpdater() {
        return titleUpdater;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}
