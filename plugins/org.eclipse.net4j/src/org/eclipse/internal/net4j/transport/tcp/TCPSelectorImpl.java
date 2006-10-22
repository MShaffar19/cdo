/***************************************************************************
 * Copyright (c) 2004, 2005, 2006 Eike Stepper, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 **************************************************************************/
package org.eclipse.internal.net4j.transport.tcp;

import org.eclipse.net4j.transport.tcp.TCPSelector;
import org.eclipse.net4j.transport.tcp.TCPSelectorListener;
import org.eclipse.net4j.util.lifecycle.AbstractLifecycle;

import java.io.IOException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author Eike Stepper
 */
public class TCPSelectorImpl extends AbstractLifecycle implements TCPSelector, Runnable
{
  private static final long SELECT_TIMEOUT = 100;

  private Selector selector;

  private Thread thread;

  public TCPSelectorImpl()
  {
  }

  public SelectionKey register(ServerSocketChannel channel, TCPSelectorListener.Passive listener)
      throws ClosedChannelException
  {
    if (listener == null)
    {
      throw new IllegalArgumentException("listener == null");
    }

    System.out.println(toString() + ": Registering " + TCPUtil.toString(channel));
    return channel.register(selector, SelectionKey.OP_ACCEPT, listener);
  }

  public SelectionKey register(SocketChannel channel, TCPSelectorListener.Active listener)
      throws ClosedChannelException
  {
    if (listener == null)
    {
      throw new IllegalArgumentException("listener == null");
    }

    System.out.println(toString() + ": Registering " + TCPUtil.toString(channel));
    return channel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ, listener);
  }

  public void run()
  {
    while (isActive())
    {
      if (Thread.interrupted())
      {
        deactivate();
        break;
      }

      try
      {
        if (selector.select(SELECT_TIMEOUT) > 0)
        {
          Iterator<SelectionKey> it = selector.selectedKeys().iterator();
          while (it.hasNext())
          {
            SelectionKey selKey = it.next();
            it.remove();

            try
            {
              handleSelection(selKey);
            }
            catch (CancelledKeyException ignore)
            {
              ; // Do nothing
            }
            catch (Exception ex)
            {
              ex.printStackTrace();
              selKey.cancel();
            }
          }
        }
      }
      catch (ClosedSelectorException ex)
      {
        break;
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
        deactivate();
        break;
      }
    }
  }

  @Override
  public String toString()
  {
    return "TCPSelector";
  }

  protected void handleSelection(SelectionKey selKey) throws IOException
  {
    SelectableChannel channel = selKey.channel();
    if (channel instanceof ServerSocketChannel)
    {
      ServerSocketChannel ssChannel = (ServerSocketChannel)selKey.channel();
      TCPSelectorListener.Passive listener = (TCPSelectorListener.Passive)selKey.attachment();

      if (selKey.isAcceptable())
      {
        System.out.println(toString() + ": Accepting " + TCPUtil.toString(ssChannel));
        listener.handleAccept(this, ssChannel);
      }
    }
    else if (channel instanceof SocketChannel)
    {
      SocketChannel sChannel = (SocketChannel)channel;
      TCPSelectorListener.Active listener = (TCPSelectorListener.Active)selKey.attachment();

      if (selKey.isConnectable())
      {
        System.out.println(toString() + ": Connecting " + TCPUtil.toString(sChannel));
        listener.handleConnect(this, sChannel);
      }

      if (selKey.isReadable())
      {
        System.out.println(toString() + ": Reading " + TCPUtil.toString(sChannel));
        listener.handleRead(this, sChannel);
      }

      if (selKey.isWritable())
      {
        System.out.println(toString() + ": Writing " + TCPUtil.toString(sChannel));
        listener.handleWrite(this, sChannel);
      }
    }
  }

  @Override
  protected void onActivate() throws Exception
  {
    selector = Selector.open();
    thread = new Thread(this);
    thread.setDaemon(true);
    thread.start();
  }

  @Override
  protected void onDeactivate() throws Exception
  {
    Exception exception = null;

    try
    {
      thread.join(2 * SELECT_TIMEOUT);
    }
    catch (RuntimeException ex)
    {
      if (exception == null)
      {
        exception = ex;
      }
    }
    finally
    {
      thread = null;
    }

    try
    {
      selector.close();
    }
    catch (Exception ex)
    {
      if (exception == null)
      {
        exception = ex;
      }
    }
    finally
    {
      selector = null;
    }

    if (exception != null)
    {
      throw exception;
    }
  }
}
