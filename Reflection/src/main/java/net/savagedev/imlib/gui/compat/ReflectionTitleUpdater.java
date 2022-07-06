package net.savagedev.imlib.gui.compat;

import net.savagedev.imlib.gui.IMTitleUpdater;
import net.savagedev.imlib.gui.compat.packet.PacketFactory;
import net.savagedev.imlib.gui.compat.wrapper.EntityPlayerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ReflectionTitleUpdater implements IMTitleUpdater {
    public static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    @Override
    public void update(Player player, String title) {
        final EntityPlayerWrapper entityPlayer = EntityPlayerWrapper.newWrapper(player);
        entityPlayer.sendPacket(PacketFactory.newPacketOpenWindow(entityPlayer.getActiveContainer().getWindowId(), player.getInventory().getSize(), title));
        entityPlayer.updateInventory();
    }
}
