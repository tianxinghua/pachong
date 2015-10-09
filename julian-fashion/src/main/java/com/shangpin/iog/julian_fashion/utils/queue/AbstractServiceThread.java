package com.shangpin.iog.julian_fashion.utils.queue;

public abstract class AbstractServiceThread extends Thread {
	private int delay;
	private boolean enabled;
	private boolean canceled;

	public AbstractServiceThread(String name, int delay, boolean enabled) {
		setName(name);
		setDelay(delay);
		if (enabled) {
			enable();
		} else {
			disable();
		}
		this.canceled = false;
		start();
	}

	public int getDelay() {
		return this.delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void enable() {
		this.enabled = true;
	}

	public void disable() {
		this.enabled = false;
	}

	public boolean isCanceled() {
		return this.canceled;
	}

	public void cancel() {
		this.canceled = true;
		interrupt();
		try {
			join();
		} catch (InterruptedException e) {
		}
	}

	public void run() {
		while (!isCanceled()) {
			try {
				if (isEnabled()) {
					process();
					sleep(getDelay());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				if (isCanceled()) {
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected abstract void process() throws Exception;
}
