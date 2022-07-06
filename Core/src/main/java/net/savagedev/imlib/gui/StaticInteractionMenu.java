package net.savagedev.imlib.gui;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class StaticInteractionMenu extends AbstractInteractionMenu {
    public StaticInteractionMenu(@Nonnull String title, int rows, @Nonnull BiConsumer<InteractionMenu, Player> builder, Consumer<Player> openAction, Consumer<Player> closeAction) {
        super(title, rows, builder, openAction, closeAction);
    }

    public StaticInteractionMenu(@Nonnull String title, int rows, @Nonnull BiConsumer<InteractionMenu, Player> builder) {
        super(title, rows, builder);
    }
}
