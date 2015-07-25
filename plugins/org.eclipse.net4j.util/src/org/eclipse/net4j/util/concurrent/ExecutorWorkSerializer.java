/*
 * Copyright (c) 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.util.concurrent;

import org.eclipse.net4j.util.lifecycle.Lifecycle;
import org.eclipse.net4j.util.lifecycle.LifecycleUtil;
import org.eclipse.net4j.util.om.log.OMLogger;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executor;

/**
 * @author Eike Stepper
 * @since 3.6
 */
public class ExecutorWorkSerializer extends Lifecycle implements IWorkSerializer
{
  private final Queue<Runnable> workQueue = new LinkedList<Runnable>();

  private Executor executor;

  private volatile boolean working;

  private volatile boolean disposed;

  public ExecutorWorkSerializer()
  {
  }

  public ExecutorWorkSerializer(Executor executor)
  {
    this.executor = executor;
  }

  public Executor getExecutor()
  {
    return executor;
  }

  public void setExecutor(Executor executor)
  {
    checkInactive();
    this.executor = executor;
  }

  public synchronized boolean addWork(Runnable runnable)
  {
    if (disposed)
    {
      return false;
    }

    if (!working && isActive())
    {
      startWork(runnable);
    }
    else
    {
      workQueue.add(runnable);
    }

    return true;
  }

  public synchronized void dispose()
  {
    LifecycleUtil.deactivate(this, OMLogger.Level.DEBUG);
  }

  @Override
  public String toString()
  {
    return ExecutorWorkSerializer.class.getSimpleName();
  }

  protected void handleException(Runnable runnable, Throwable ex)
  {
  }

  protected void noWork()
  {
  }

  private void startWork(final Runnable runnable)
  {
    working = true;
    if (!disposed)
    {
      executor.execute(new RunnableWithName()
      {
        public String getName()
        {
          if (runnable instanceof RunnableWithName)
          {
            return ((RunnableWithName)runnable).getName();
          }

          return null;
        }

        public void run()
        {
          try
          {
            runnable.run();
          }
          catch (Throwable ex)
          {
            try
            {
              handleException(runnable, ex);
            }
            catch (Throwable ignore)
            {
              //$FALL-THROUGH$
            }
          }

          workDone();
        }
      });
    }
  }

  private synchronized void workDone()
  {
    Runnable runnable = workQueue.poll();
    if (runnable != null)
    {
      startWork(runnable);
    }
    else
    {
      noWork();
      working = false;
    }
  }

  @Override
  protected void doBeforeActivate() throws Exception
  {
    super.doBeforeActivate();
    checkState(executor, "executor");
  }

  @Override
  protected void doActivate() throws Exception
  {
    super.doActivate();
    workDone();
  }

  @Override
  protected void doDeactivate() throws Exception
  {
    disposed = true;
    working = false;
    workQueue.clear();

    super.doDeactivate();
  }
}