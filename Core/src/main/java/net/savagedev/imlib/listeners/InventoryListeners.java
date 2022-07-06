package net.savagedev.imlib.listeners;

import net.savagedev.imlib.gui.AbstractInteractionMenu;
import net.savagedev.imlib.gui.InteractionMenu;
import net.savagedev.imlib.gui.holder.InteractionMenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;

public class InventoryListeners implements Listener {
    @EventHandler
    public void on(final InventoryClickEvent e) {
        final Inventory inventory = e.getClickedInventory();
        if (inventory == null) {
            return;
        }

        InventoryHolder holder = inventory.getHolder();
        if (holder instanceof InteractionMenuHolder) {
            Consumer<InventoryClickEvent> action = ((InteractionMenuHolder) holder).getMenu().getAction(e.getSlot());
            if (action != null) {
                action.accept(e);
            }
        }

        holder = e.getWhoClicked().getOpenInventory().getTopInventory().getHolder();
        if (holder instanceof InteractionMenuHolder) {
            Consumer<InventoryClickEvent> action = ((InteractionMenuHolder) holder).getMenu().getDefaultAction();
            if (action != null) {
                action.accept(e);
            }
        }
    }

    @EventHandler
    public void on(final InventoryCloseEvent e) {
        final InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof InteractionMenuHolder) {
            final InteractionMenu menu = ((InteractionMenuHolder) holder).getMenu();
            if (menu instanceof AbstractInteractionMenu) {
                final Lock lock = ((AbstractInteractionMenu) menu).getLock();
                lock.lock();
                try {
                    menu.close((Player) e.getPlayer(), false);
                } finally {
                    lock.unlock();
                }
            } else {
                menu.close((Player) e.getPlayer(), false);
            }
        }
    }
}
