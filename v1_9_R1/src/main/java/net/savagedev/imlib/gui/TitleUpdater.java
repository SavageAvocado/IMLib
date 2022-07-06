package net.savagedev.imlib.gui;

import net.minecraft.server.v1_9_R1.ChatMessage;
import net.minecraft.server.v1_9_R1.Container;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.PacketPlayOutOpenWindow;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class TitleUpdater implements IMTitleUpdater {
    @Override
    public void update(Player user, String title) {
        EntityPlayer player = ((CraftPlayer) user).getHandle();
        Container container = player.activeContainer;

        player.playerConnection.networkManager.sendPacket(new PacketPlayOutOpenWindow(container.windowId, "minecraft:chest", new ChatMessage(title), user.getOpenInventory().getTopInventory().getSize()), future -> player.updateInventory(container));
    }
}
