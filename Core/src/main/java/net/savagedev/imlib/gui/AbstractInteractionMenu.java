package net.savagedev.imlib.gui;

import net.savagedev.imlib.IMLib;
import net.savagedev.imlib.gui.holder.InteractionMenuHolder;
import net.savagedev.imlib.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class AbstractInteractionMenu implements InteractionMenu {
    private final Map<Integer, Consumer<InventoryClickEvent>> actions = new HashMap<>();
    private final Inventory inventory;

    @Nonnull
    private final BiConsumer<InteractionMenu, Player> builder;

    private final Lock lock = new ReentrantLock();

    private Consumer<InventoryClickEvent> defaultAction;
    private Consumer<InventoryClickEvent> globalAction;

    private Consumer<Player> closeAction;
    private Consumer<Player> openAction;

    private String title;

    AbstractInteractionMenu(@Nonnull final String title, final int rows, @Nonnull final BiConsumer<InteractionMenu, Player> builder, Consumer<Player> openAction, Consumer<Player> closeAction) {
        this.inventory = Bukkit.createInventory(new InteractionMenuHolder(this), rows * 9, MessageUtils.color(title));
        this.closeAction = closeAction;
        this.openAction = openAction;
        this.builder = builder;
    }

    AbstractInteractionMenu(@Nonnull final String title, final int rows, @Nonnull final BiConsumer<InteractionMenu, Player> builder) {
        this(title, rows, builder, null, null);
    }

    @Override
    public void open(@Nonnull final Player player) {
        if (this.openAction != null) {
            this.openAction.accept(player);
        }
        player.openInventory(this.inventory);
    }

    @Override
    public void close(@Nonnull final Player player) {
        this.close(player, true);
    }

    @Override
    public void close(@Nonnull final Player player, boolean closeBukkitInventory) {
        if (this.closeAction != null) {
            closeAction.accept(player);
        }
        if (closeBukkitInventory) {
            player.closeInventory();
        }
    }

    @Override
    public void setTitle(@Nonnull final String title) {
        this.title = title;
        this.inventory.getViewers().stream().filter(entity -> entity instanceof Player).map(entity -> (Player) entity).forEach(player -> IMLib.getInstance().getTitleUpdater().update(player, MessageUtils.color(this.title)));
    }

    @Override
    public void setItem(final int slot, @Nonnull final ItemStack item, @Nonnull final Consumer<InventoryClickEvent> action) {
        this.actions.putIfAbsent(slot, action);
        this.inventory.setItem(slot, item);
    }

    @Override
    public void setItem(final int slot, @Nonnull final ItemStack item) {
        this.inventory.setItem(slot, item);
    }

    @Override
    public void setDefaultAction(Consumer<InventoryClickEvent> e) {
        this.defaultAction = e;
    }

    public void setGlobalAction(Consumer<InventoryClickEvent> e) {
        this.globalAction = e;
    }

    @Override
    public void refresh() {
        this.lock.lock();
        try {
            this.getInventory().getViewers().stream()
                    .filter(entity -> entity instanceof Player)
                    .map(entity -> (Player) entity)
                    .forEach(player ->
                            this.builder.accept(this, player)
                    );
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void clear() {
        this.inventory.clear();
        this.actions.clear();
    }

    @Override
    public Consumer<InventoryClickEvent> getAction(final int slot) {
        if (!this.actions.containsKey(slot)) {
            return this.defaultAction;
        }
        return this.actions.get(slot);
    }

    @Override
    public Consumer<InventoryClickEvent> getDefaultAction() {
        return this.globalAction;
    }

    @Override
    @Nonnull
    public Inventory getInventory() {
        return this.inventory;
    }

    @Override
    @Nonnull
    public String getTitle() {
        return this.title;
    }

    public Lock getLock() {
        return this.lock;
    }
}
