package net.savagedev.imlib.scheduler;

import javax.annotation.Nonnull;

public interface Scheduler {
    void scheduleAsync(@Nonnull final Runnable runnable, long delay, long period);

    void cancel();
}
