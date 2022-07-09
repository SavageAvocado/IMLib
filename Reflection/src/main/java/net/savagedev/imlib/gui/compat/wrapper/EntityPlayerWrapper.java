package net.savagedev.imlib.gui.compat.wrapper;

import net.savagedev.imlib.gui.compat.ReflectionTitleUpdater;
import net.savagedev.imlib.gui.compat.packet.PacketFactory;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class EntityPlayerWrapper {
    private static final Map<String, String> OBFUSCATED_ACTIVE_CONTAINER_MAPPINGS = new HashMap<>();

    static {
        OBFUSCATED_ACTIVE_CONTAINER_MAPPINGS.put("v1_17_R1", "bV");
        OBFUSCATED_ACTIVE_CONTAINER_MAPPINGS.put("v1_18_R1", "bW");
        OBFUSCATED_ACTIVE_CONTAINER_MAPPINGS.put("v1_18_R2", "bV");
        OBFUSCATED_ACTIVE_CONTAINER_MAPPINGS.put("v1_19_R1", "bU");
    }

    private final ContainerWrapper container;

    private Field activeContainerField;

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
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            this.playerConnection = this.entityPlayer.getClass().getField("playerConnection").get(this.entityPlayer);
        } catch (NoSuchFieldException ignored) {
            try {
                this.playerConnection = this.entityPlayer.getClass().getField("b").get(this.entityPlayer);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        try {
            this.activeContainerField = this.entityPlayer.getClass().getField("activeContainer");
        } catch (NoSuchFieldException ignored) {
            try {
                this.activeContainerField = this.entityPlayer.getClass().getField(OBFUSCATED_ACTIVE_CONTAINER_MAPPINGS.get(ReflectionTitleUpdater.VERSION));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        try {
            this.updateInventoryMethod = this.entityPlayer.getClass().getMethod("updateInventory", ContainerWrapper.CONTAINER_CLASS);
        } catch (NoSuchMethodException ignored) {
            try {
                this.updateInventoryMethod = this.entityPlayer.getClass().getMethod("initMenu", ContainerWrapper.CONTAINER_CLASS);
            } catch (NoSuchMethodException ignore) {
                try {
                    this.updateInventoryMethod = this.entityPlayer.getClass().getMethod("a", ContainerWrapper.CONTAINER_CLASS);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            this.sendPacketMethod = this.playerConnection.getClass().getMethod("sendPacket", PacketFactory.PACKET_CLASS);
        } catch (NoSuchMethodException ignored) {
            try {
                this.sendPacketMethod = this.playerConnection.getClass().getMethod("a", PacketFactory.PACKET_CLASS);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
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
            return this.activeContainerField.get(this.entityPlayer);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ContainerWrapper getActiveContainer() {
        return this.container;
    }
}
