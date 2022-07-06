package net.savagedev.imlib.gui;

import net.savagedev.imlib.IMLib;
import net.savagedev.imlib.scheduler.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class DynamicInteractionMenu extends AbstractInteractionMenu {
    private final Scheduler scheduler;

    public DynamicInteractionMenu(@Nonnull String title, int rows, @Nonnull BiConsumer<InteractionMenu, Player> builder, Consumer<Player> openAction, Consumer<Player> closeAction, final long period, @Nonnull final Scheduler scheduler) {
        super(title, rows, builder, openAction, closeAction);
        (
                this.scheduler = scheduler
        ).scheduleAsync(super::refresh, 0L, period);
    }

    public DynamicInteractionMenu(@Nonnull String title, int rows, @Nonnull BiConsumer<InteractionMenu, Player> builder, Consumer<Player> openAction, Consumer<Player> closeAction, final long period) {
        this(title, rows, builder, openAction, closeAction, period, new Scheduler() {
            private final BukkitScheduler scheduler = Bukkit.getScheduler();

            private int taskId = -1;

            @Override
            public void scheduleAsync(@Nonnull Runnable runnable, long delay, long period) {
                if (this.taskId != -1) {
                    throw new IllegalStateException("You must cancel the original task before scheduling a new one!");
                }
                this.taskId = (
                        this.scheduler.runTaskTimerAsynchronously(IMLib.getInstance().getPlugin(), runnable, delay, period)
                ).getTaskId();
            }

            @Override
            public void cancel() {
                if (this.taskId == -1) {
                    throw new IllegalStateException("No task timer was started!");
                }
                this.scheduler.cancelTask(this.taskId);
                this.taskId = -1;
            }
        });
    }

    public DynamicInteractionMenu(@Nonnull String title, int rows, @Nonnull BiConsumer<InteractionMenu, Player> builder, final long period) {
        this(title, rows, builder, null, null, period);
    }

    @Override
    public void close(@Nonnull final Player player) {
        this.scheduler.cancel();
        super.close(player);
    }
}
