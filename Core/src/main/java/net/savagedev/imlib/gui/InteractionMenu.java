package net.savagedev.imlib.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public interface InteractionMenu {
    /**
     * Opens the InteractionMenu for the provided {@link Player}.
     *
     * @param player - The player who will see the GUI.
     */
    void open(@Nonnull Player player);

    /**
     * Closes the InteractionMenu for the provided {@link Player}.
     *
     * @param player               - The player who the GUI will be closed for.
     * @param closeBukkitInventory - If this is true, it will also close the Bukkit {@link Inventory}. If it is false, it will keep the {@link Inventory} open.
     */
    void close(@Nonnull Player player, boolean closeBukkitInventory);

    /**
     * Will close the InteractionMenu for the provided {@link Player}.
     *
     * @param player - The player who the GUI will be closed for.
     */
    void close(@Nonnull Player player);

    /**
     * Sets and updates the InteractionMenu's title.
     *
     * @param title - The new title.
     */
    void setTitle(@Nonnull String title);

    /**
     * Sets the item a specific slot will contain with a click action.
     *
     * @param slot - The slot the {@link ItemStack} will go in.
     * @param item - The {@link ItemStack} that will be displayed in the slot.
     * @param e    - The click action.
     */
    void setItem(int slot, @Nonnull ItemStack item, Consumer<InventoryClickEvent> e);

    /**
     * Sets the {@link ItemStack} for a specific slot.
     *
     * @param slot - The slot the {@link ItemStack} will go in.
     * @param item - The {@link ItemStack} that will be displayed in the slot.
     */
    void setItem(int slot, @Nonnull ItemStack item);

    /**
     * Sets the action that will happen by default when a player clicks an unpopulated slot in the InteractionMenu.
     *
     * @param e - The action that will happen.
     */
    void setDefaultAction(Consumer<InventoryClickEvent> e);

    /**
     * Sets the action that will happen by default when a player clicks an unpopulated slot anywhere in the {@link org.bukkit.inventory.InventoryView}.
     *
     * @param e - The action that will happen on click.
     */
    void setGlobalAction(Consumer<InventoryClickEvent> e);

    /**
     * Updates the InteractionMenu's title, and {@link Inventory}
     */
    void refresh();

    /**
     * Clears the inventory, and all slot associated actions.
     */
    void clear();

    /**
     * Gets the action associated with a certain slot.
     *
     * @param slot - THe slot you want to get the click action of.
     * @return - {@link Consumer<InventoryClickEvent>} The slot's action.
     */
    Consumer<InventoryClickEvent> getAction(int slot);

    /**
     * Gets the InteractionMenu's global action.
     *
     * @return - {@link Consumer<InventoryClickEvent>} The global action.
     */
    Consumer<InventoryClickEvent> getDefaultAction();

    /**
     * Gets the Bukkit {@link Inventory} of the InteractionMenu.
     *
     * @return - {@link Consumer<InventoryClickEvent>} The Bukkit Inventory of the InteractionMenu.
     */
    @Nonnull
    Inventory getInventory();

    /**
     * Gets the InteractionMenu's title.
     *
     * @return - {@link String} The InteractionMenu's title.
     */
    @Nonnull
    String getTitle();
}
