package net.savagedev.imlib.gui;

import net.minecraft.server.v1_14_R1.ChatMessage;
import net.minecraft.server.v1_14_R1.Container;
import net.minecraft.server.v1_14_R1.ContainerChest;
import net.minecraft.server.v1_14_R1.Containers;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import net.minecraft.server.v1_14_R1.PacketPlayOutOpenWindow;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TitleUpdater implements IMTitleUpdater {
    @Override
    public void update(Player player, String title) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        Container container = entityPlayer.activeContainer;

        entityPlayer.playerConnection.a(new PacketPlayOutOpenWindow(container.windowId, this.getContainer(player.getOpenInventory().getTopInventory()), new ChatMessage(title)), future -> entityPlayer.updateInventory(container));
    }

    private Containers<ContainerChest> getContainer(Inventory inventory) {
        switch (inventory.getSize()) {
            case 9:
                return Containers.GENERIC_9X1;
            case 18:
                return Containers.GENERIC_9X2;
            case 27:
                return Containers.GENERIC_9X3;
            case 36:
                return Containers.GENERIC_9X4;
            case 45:
                return Containers.GENERIC_9X5;
            case 54:
                return Containers.GENERIC_9X6;
        }
        return Containers.GENERIC_9X6;
    }
}
