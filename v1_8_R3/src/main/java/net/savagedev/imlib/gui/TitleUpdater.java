package net.savagedev.imlib.gui;

import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.Container;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class TitleUpdater implements IMTitleUpdater {
    @Override
    public void update(Player user, String title) {
        EntityPlayer player = ((CraftPlayer) user).getHandle();
        Container container = player.activeContainer;

        player.playerConnection.networkManager.a(new PacketPlayOutOpenWindow(container.windowId, "minecraft:chest", new ChatMessage(title), user.getOpenInventory().getTopInventory().getSize()), future -> player.updateInventory(container));
    }
}
