package net.savagedev.imlib.gui.compat.wrapper;

import net.savagedev.imlib.gui.compat.packet.PacketFactory;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EntityPlayerWrapper {
    private final ContainerWrapper container;

    private Method updateInventoryMethod;
    private Method sendPacketMethod;

    private Object playerConnection;
    private Object entityPlayer;

    public static EntityPlayerWrapper newWrapper(Player player) {
        return new EntityPlayerWrapper(player);
    }

    private EntityPlayerWrapper(final Player player) {
        try {
            this.entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            this.playerConnection = this.entityPlayer.getClass().getField("playerConnection").get(this.entityPlayer);

            this.updateInventoryMethod = this.entityPlayer.getClass().getMethod("updateInventory", ContainerWrapper.CONTAINER_CLASS);
            this.sendPacketMethod = this.playerConnection.getClass().getMethod("sendPacket", PacketFactory.PACKET_CLASS);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        this.container = ContainerWrapper.newWrapper(this.getNmsContainer());
    }

    public void updateInventory() {
        try {
            this.updateInventoryMethod.invoke(this.entityPlayer, this.container.getHandle());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void sendPacket(Object packet) {
        try {
            this.sendPacketMethod.invoke(this.playerConnection, packet);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Object getNmsContainer() {
        try {
            return this.entityPlayer.getClass().getField("activeContainer").get(this.entityPlayer);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ContainerWrapper getActiveContainer() {
        return this.container;
    }
}
