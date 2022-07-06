package net.savagedev.imlib.gui;

import net.minecraft.server.v1_7_R4.Container;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PacketPlayOutOpenWindow;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class TitleUpdater implements IMTitleUpdater {
    @Override
    public void update(Player user, String title) {
        EntityPlayer player = ((CraftPlayer) user).getHandle();
        Container container = player.activeContainer;

        player.playerConnection.sendPacket(new PacketPlayOutOpenWindow(container.windowId, 0, title, user.getOpenInventory().getTopInventory().getSize(), false));
        player.updateInventory(container);
    }
}
