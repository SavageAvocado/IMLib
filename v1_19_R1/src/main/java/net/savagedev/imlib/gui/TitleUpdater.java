package net.savagedev.imlib.gui;

import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.inventory.Container;
import net.minecraft.world.inventory.ContainerChest;
import net.minecraft.world.inventory.Containers;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TitleUpdater implements IMTitleUpdater {
    @Override
    public void update(Player player, String title) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        Container container = entityPlayer.bU;

        entityPlayer.b.a(new PacketPlayOutOpenWindow(container.j, this.getContainer(player.getOpenInventory().getTopInventory()), IChatBaseComponent.a(title)), future ->
                entityPlayer.a(container)
        );
    }

    private Containers<ContainerChest> getContainer(Inventory inventory) {
        switch (inventory.getSize()) {
            case 9:
                return Containers.a;
            case 18:
                return Containers.b;
            case 27:
                return Containers.c;
            case 36:
                return Containers.d;
            case 45:
                return Containers.e;
            case 54:
                return Containers.f;
        }
        return Containers.f;
    }
}
