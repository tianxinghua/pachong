package com.shangpin.iog.lungo.util;

import java.util.LinkedList;

public class Queue
{
  private LinkedList<String> queue;

  public Queue()
  {
    this.queue = new LinkedList(); }

  public void enQueue(String url) {
    this.queue.add(url);
  }

  public String deQueue() {
    return ((String)this.queue.removeFirst());
  }

  public boolean isQueueEmpty() {
    return this.queue.isEmpty();
  }

  public boolean contians(String url) {
    return this.queue.contains(url);
  }
}