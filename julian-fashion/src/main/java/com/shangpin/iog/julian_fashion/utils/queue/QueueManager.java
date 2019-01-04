package com.shangpin.iog.julian_fashion.utils.queue;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class QueueManager {
	
	private static final int DEFAULT_DELAY = 1;
	
	private DelayQueue<DelayTask> delayQueue;
	private DelayQueueManager delayQueueManager;
	
	public ExecutorService threadPool = Executors.newFixedThreadPool(10);

	public QueueManager(String queueName) {
		setDelayQueue(new DelayQueue<DelayTask>());
		setDelayQueueManager(new DelayQueueManager(queueName, DEFAULT_DELAY));
	}

	public QueueManager(String queueName, int delay) {
		setDelayQueue(new DelayQueue<DelayTask>());
		setDelayQueueManager(new DelayQueueManager(queueName, delay));
	}
	
	public QueueManager(String queueName, int delay, int workerCount) {
		setDelayQueue(new DelayQueue<DelayTask>());
		setDelayQueueManager(new DelayQueueManager(queueName, delay));
		threadPool = Executors.newFixedThreadPool(workerCount);
	}

	public void cancel() {
		getDelayQueueManager().cancel();
		threadPool.shutdown();
	}

	public boolean delayTask(DelayTask delayTask) {
		return getDelayQueue().add(delayTask);
	}

	public int getDelayQueueLoad() {
		ThreadPoolExecutor executor = (ThreadPoolExecutor)threadPool;
		return getDelayQueue().size() + executor.getQueue().size();
	}

	public void clearDelayQueue() {
		getDelayQueue().clear();
	}

	protected void setDelayQueue(DelayQueue<DelayTask> delayQueue) {
		this.delayQueue = delayQueue;
	}

	protected DelayQueue<DelayTask> getDelayQueue() {
		return this.delayQueue;
	}

	public DelayQueueManager getDelayQueueManager() {
		return this.delayQueueManager;
	}

	protected void setDelayQueueManager(DelayQueueManager delayQueueManager) {
		this.delayQueueManager = delayQueueManager;
	}

	public class DelayQueueManager extends AbstractServiceThread {
		public DelayQueueManager(String name, int delay) {
			super(name, delay, true);
		}

		public void process() throws Exception {
			final DelayTask delayTask = (DelayTask) QueueManager.this.getDelayQueue().take();
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						long startTime = delayTask.getAt().getTime();
						delayTask.getTask().doTask(startTime);
					} catch (Exception e) {

					}
				}
			});
		}
	}
}