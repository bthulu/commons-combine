package pondcat.commons.combine.concurrent;

import java.util.concurrent.Future;

/**
 *  @author gejian at 2018/7/5 14:11
 */
public interface StoppableRunnable extends Runnable {
	void stopSafe();

	void stopSafe(Future future);
}
