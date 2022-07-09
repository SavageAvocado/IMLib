package net.savagedev.imlib.gui.compat.packet;

import net.savagedev.imlib.gui.compat.ReflectionTitleUpdater;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PacketFactory {
    private static Constructor<?> PACKET_OPEN_WINDOW_CONSTRUCTOR;
    private static Constructor<?> CHAT_MESSAGE_CONSTRUCTOR;

    public static Class<?> PACKET_CLASS;

    private static Class<?> CONTAINERS;

    private static Method SERIALIZE_CHAT_MESSAGE_METHOD;

    static {
        try {
            PACKET_CLASS = Class.forName("net.minecraft.server." + ReflectionTitleUpdater.VERSION + ".Packet");
        } catch (ClassNotFoundException ignored) {
            try {
                PACKET_CLASS = Class.forName("net.minecraft.network.protocol.Packet");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        Class<?> packetOpenWindowClass = null;
        try {
            packetOpenWindowClass = Class.forName("net.minecraft.server." + ReflectionTitleUpdater.VERSION + ".PacketPlayOutOpenWindow");
        } catch (ClassNotFoundException ignored) {
            try {
                packetOpenWindowClass = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutOpenWindow");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        assert packetOpenWindowClass != null;

        Class<?> iChatBaseComponentClass = null;
        try {
            iChatBaseComponentClass = Class.forName("net.minecraft.server." + ReflectionTitleUpdater.VERSION + ".IChatBaseComponent");
        } catch (ClassNotFoundException ignored) {
            try {
                iChatBaseComponentClass = Class.forName("net.minecraft.network.chat.IChatBaseComponent");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        assert iChatBaseComponentClass != null;

        Class<?> chatMessageClass = null;
        try {
            chatMessageClass = Class.forName("net.minecraft.server." + ReflectionTitleUpdater.VERSION + ".ChatMessage");
        } catch (ClassNotFoundException ignored) {
            try {
                chatMessageClass = Class.forName("net.minecraft.network.chat.ChatMessage");
            } catch (ClassNotFoundException ignore) {
                try {
                    SERIALIZE_CHAT_MESSAGE_METHOD = iChatBaseComponentClass.getMethod("a", String.class);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            assert chatMessageClass != null;
            CHAT_MESSAGE_CONSTRUCTOR = chatMessageClass.getConstructor(String.class, Object[].class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            CONTAINERS = Class.forName("net.minecraft.server." + ReflectionTitleUpdater.VERSION + ".Containers");
        } catch (ClassNotFoundException ignored) {
            try {
                CONTAINERS = Class.forName("net.minecraft.world.inventory.Containers");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        try {
            PACKET_OPEN_WINDOW_CONSTRUCTOR = packetOpenWindowClass.getConstructor(int.class, int.class, String.class, int.class, boolean.class); // 1.7 PacketPlayOutOpenWindow constructor.
        } catch (NoSuchMethodException ignored) {
            try {
                PACKET_OPEN_WINDOW_CONSTRUCTOR = packetOpenWindowClass.getConstructor(int.class, String.class, iChatBaseComponentClass, int.class); // 1.8-1.13.2 PacketPlayOutOpenWindow constructor.
            } catch (NoSuchMethodException ignore) {
                try {
                    PACKET_OPEN_WINDOW_CONSTRUCTOR = packetOpenWindowClass.getConstructor(int.class, CONTAINERS, iChatBaseComponentClass); // 1.14-present PacketPlayOutOpenWindow constructor.
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Object newPacketOpenWindow(int windowId, int size, String title) {
        try {
            return PACKET_OPEN_WINDOW_CONSTRUCTOR.newInstance(windowId, 0, title, size, false);
        } catch (IllegalArgumentException ignored) {
            try {
                return PACKET_OPEN_WINDOW_CONSTRUCTOR.newInstance(windowId, "minecraft:chest", newChatMessage(title), size);
            } catch (IllegalArgumentException ignore) {
                try {
                    return PACKET_OPEN_WINDOW_CONSTRUCTOR.newInstance(windowId, getContainer(size), newChatMessage(title));
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
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
        if (CHAT_MESSAGE_CONSTRUCTOR == null) {
            try {
                return SERIALIZE_CHAT_MESSAGE_METHOD.invoke(null, text);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            return CHAT_MESSAGE_CONSTRUCTOR.newInstance(text, new Object[]{});
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Object getContainer(int size) throws IllegalAccessException {
        switch (size) {
            case 9:
                try {
                    return CONTAINERS.getField("GENERIC_9X1").get(null);
                } catch (NoSuchFieldException ignored) {
                    try {
                        return CONTAINERS.getField("a").get(null);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
            case 18:
                try {
                    return CONTAINERS.getField("GENERIC_9X2").get(null);
                } catch (NoSuchFieldException ignored) {
                    try {
                        return CONTAINERS.getField("b").get(null);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
            case 27:
                try {
                    return CONTAINERS.getField("GENERIC_9X3").get(null);
                } catch (NoSuchFieldException ignored) {
                    try {
                        return CONTAINERS.getField("c").get(null);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
            case 36:
                try {
                    return CONTAINERS.getField("GENERIC_9X4").get(null);
                } catch (NoSuchFieldException ignored) {
                    try {
                        return CONTAINERS.getField("d").get(null);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
            case 45:
                try {
                    return CONTAINERS.getField("GENERIC_9X5").get(null);
                } catch (NoSuchFieldException ignored) {
                    try {
                        return CONTAINERS.getField("e").get(null);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
            case 54:
                try {
                    return CONTAINERS.getField("GENERIC_9X6").get(null);
                } catch (NoSuchFieldException ignored) {
                    try {
                        return CONTAINERS.getField("f").get(null);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
        }
        throw new IllegalArgumentException();
    }
}
