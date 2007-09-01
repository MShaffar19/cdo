/***************************************************************************
 * Copyright (c) 2004 - 2007 Eike Stepper, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 **************************************************************************/
package org.eclipse.net4j.jms.internal.server;

import org.eclipse.net4j.IChannel;
import org.eclipse.net4j.internal.jms.MessageImpl;
import org.eclipse.net4j.jms.internal.server.bundle.OM;
import org.eclipse.net4j.jms.internal.server.protocol.JMSServerMessageRequest;
import org.eclipse.net4j.jms.server.IStoreTransaction;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Eike Stepper
 */
public class ServerConsumer
{
  private ServerSession session;

  private long id;

  private ServerDestination destination;

  private String messageSelector;

  private boolean noLocal;

  private boolean durable;

  private ConcurrentMap<String, MessageImpl> messages = new ConcurrentHashMap<String, MessageImpl>();

  public ServerConsumer(long id, ServerDestination destination, String messageSelector, boolean noLocal, boolean durable)
  {
    this.id = id;
    this.destination = destination;
    this.messageSelector = messageSelector;
    this.noLocal = noLocal;
    this.durable = durable;
  }

  public ServerSession getSession()
  {
    return session;
  }

  public void setSession(ServerSession session)
  {
    this.session = session;
  }

  public long getID()
  {
    return id;
  }

  public ServerDestination getDestination()
  {
    return destination;
  }

  public String getMessageSelector()
  {
    return messageSelector;
  }

  public boolean isNoLocal()
  {
    return noLocal;
  }

  public IChannel getChannel()
  {
    return session.getConnection().getProtocol().getChannel();
  }

  public boolean isDurable()
  {
    return durable;
  }

  public boolean handleClientMessage(IStoreTransaction transaction, MessageImpl message)
  {
    try
    {
      String messageID = message.getJMSMessageID();
      synchronized (messages)
      {
        messages.put(messageID, message);
      }

      new JMSServerMessageRequest(getChannel(), session.getID(), id, message).send();
      transaction.messageSent(message, id);
      return true;
    }
    catch (Exception ex)
    {
      OM.LOG.error(ex);
      return false;
    }
  }

  public void handleAcknowledge(IStoreTransaction transaction)
  {
    synchronized (messages)
    {
      if (messages.isEmpty())
      {
        return;
      }

      for (MessageImpl message : messages.values())
      {
        transaction.messageAcknowledged(message, id);
        System.out.println("\nMessage acknowledged: " + message.getJMSMessageID() + "  (consumer=" + id + ")\n");
      }

      messages.clear();
    }
  }

  public void handleRecover(IStoreTransaction transaction)
  {
    synchronized (messages)
    {
      if (messages.isEmpty())
      {
        return;
      }

      for (MessageImpl message : messages.values())
      {
        System.out.println("\nRecovering message: " + message.getJMSMessageID() + "  (consumer=" + id + ")\n");
        session.getConnection().getServer().addWork(message);
      }
    }
  }
}
