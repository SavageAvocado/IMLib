package net.savagedev.imlib.gui.compat.packet;

import net.savagedev.imlib.gui.compat.ReflectionTitleUpdater;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PacketFactory {
    private static Constructor<?> PACKET_OPEN_WINDOW_CONSTRUCTOR;
    private static Constructor<?> CHAT_MESSAGE_CONSTRUCTOR;

    public static Class<?> PACKET_CLASS;
    private static Class<?> CONTAINERS;

    static {
        try {
            PACKET_CLASS = Class.forName("net.minecraft.server." + ReflectionTitleUpdater.VERSION + ".Packet");
            final Class<?> packetOpenWindowClass = Class.forName("net.minecraft.server." + ReflectionTitleUpdater.VERSION + ".PacketPlayOutOpenWindow");
            try {
                PACKET_OPEN_WINDOW_CONSTRUCTOR = packetOpenWindowClass.getConstructor(int.class, int.class, String.class, int.class, boolean.class); // 1.7 PacketPlayOutOpenWindow constructor.
            } catch (NoSuchMethodException ignored) {
                final Class<?> iChatBaseComponent = Class.forName("net.minecraft.server." + ReflectionTitleUpdater.VERSION + ".IChatBaseComponent");
                try {
                    CHAT_MESSAGE_CONSTRUCTOR = Class.forName("net.minecraft.server." + ReflectionTitleUpdater.VERSION + ".ChatMessage").getConstructor(String.class, Object[].class);
                    PACKET_OPEN_WINDOW_CONSTRUCTOR = packetOpenWindowClass.getConstructor(int.class, String.class, iChatBaseComponent, int.class); // 1.8-1.13.3 PacketPlayOutOpenWindow constructor.
                } catch (NoSuchMethodException ignored1) {
                    CONTAINERS = Class.forName("net.minecraft.server." + ReflectionTitleUpdater.VERSION + ".Containers");
                    try {
                        PACKET_OPEN_WINDOW_CONSTRUCTOR = packetOpenWindowClass.getConstructor(int.class, CONTAINERS, iChatBaseComponent); // 1.14-present PacketPlayOutOpenWindow constructor.
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Object newPacketOpenWindow(int windowId, int size, String title) {
        try {
            return PACKET_OPEN_WINDOW_CONSTRUCTOR.newInstance(windowId, 0, title, size, false);
        } catch (IllegalArgumentException ignored) {
            try {
                return PACKET_OPEN_WINDOW_CONSTRUCTOR.newInstance(windowId, "minecraft:chest", newChatMessage(title), size);
            } catch (IllegalArgumentException ignored1) {
                try {
                    return PACKET_OPEN_WINDOW_CONSTRUCTOR.newInstance(windowId, getContainer(size), newChatMessage(title));
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchFieldException e) {
                    e.printStackTrace();
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Object newChatMessage(String text) {
        try {
            return CHAT_MESSAGE_CONSTRUCTOR.newInstance(text, new Object[]{});
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Object getContainer(int size) throws NoSuchFieldException, IllegalAccessException {
        switch (size) {
            case 9:
                return CONTAINERS.getField("GENERIC_9X1").get(null);
            case 18:
                return CONTAINERS.getField("GENERIC_9X2").get(null);
            case 27:
                return CONTAINERS.getField("GENERIC_9X3").get(null);
            case 36:
                return CONTAINERS.getField("GENERIC_9X4").get(null);
            case 45:
                return CONTAINERS.getField("GENERIC_9X5").get(null);
            case 54:
                return CONTAINERS.getField("GENERIC_9X6").get(null);
        }
        return CONTAINERS.getField("GENERIC_9X6").get(null);
    }
}
