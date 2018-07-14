package bthulu.commons.combine.concurrent;

import java.util.concurrent.Future;

/**
 *  @author gejian at 2018/7/6 2:33
 */
public class StoppableRunner implements StoppableRunnable {
	private final Runnable runnable;

	private boolean stopSignal = false;
	private Future future;

	public StoppableRunner(Runnable runnable) {
		this.runnable = runnable;
	}

	@Override
	public void stopSafe() {
		stopSignal = true;
	}

	@Override
	public void stopSafe(Future future) {
		stopSignal = true;
		this.future = future;
	}

	@Override
	public void run() {
		if (stopSignal) {
			Future tmp = future;
			future = null;
			tmp.cancel(true);
			stopSignal = false;
			return;
		}
		runnable.run();
	}
}
