package net.savagedev.imlib.gui.compat.wrapper;

import net.savagedev.imlib.gui.compat.ReflectionTitleUpdater;

public final class ContainerWrapper {
    static Class<?> CONTAINER_CLASS;

    static {
        try {
            CONTAINER_CLASS = Class.forName("net.minecraft.server." + ReflectionTitleUpdater.VERSION + ".Container");
        } catch (ClassNotFoundException ignored) {
            try {
                CONTAINER_CLASS = Class.forName("net.minecraft.world.inventory.Container");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    static ContainerWrapper newWrapper(Object container) {
        return new ContainerWrapper(container);
    }

    private final Object handle;

    private int windowId = -1;

    private ContainerWrapper(Object handle) {
        this.handle = handle;
        try {
            this.windowId = (int) this.handle.getClass().getField("windowId").get(this.handle);
        } catch (IllegalAccessException | NoSuchFieldException ignored) {
            try {
                this.windowId = (int) this.handle.getClass().getField("j").get(this.handle);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    public int getWindowId() {
        return this.windowId;
    }

    Object getHandle() {
        return this.handle;
    }
}
