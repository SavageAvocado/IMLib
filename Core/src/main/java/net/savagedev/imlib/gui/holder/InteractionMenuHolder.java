package net.savagedev.imlib.gui.holder;

import net.savagedev.imlib.gui.InteractionMenu;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import javax.annotation.Nonnull;

public class InteractionMenuHolder implements InventoryHolder {
    private final InteractionMenu menu;

    public InteractionMenuHolder(final InteractionMenu menu) {
        this.menu = menu;
    }

    @Override @Nonnull
    public Inventory getInventory() {
        return this.menu.getInventory();
    }

    @Nonnull
    public InteractionMenu getMenu() {
        return this.menu;
    }
}
