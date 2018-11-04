package com.y3tu.tool.cron.task;

/**
 * {@link Runnable} 的 {@link Task}包装
 *
 * @author Looly
 */
public class RunnableTask implements Task {
    private Runnable runnable;

    public RunnableTask(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void execute() {
        runnable.run();
    }
}
