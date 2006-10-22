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

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;

/**
 * @author Eike Stepper
 */
public class ClientTCPConnectorImpl extends AbstractTCPConnector
{
  private String host;

  private int port = DEFAULT_PORT;

  public ClientTCPConnectorImpl()
  {
  }

  public String getHost()
  {
    return host;
  }

  public void setHost(String host)
  {
    this.host = host;
  }

  public int getPort()
  {
    return port;
  }

  public void setPort(int port)
  {
    this.port = port;
  }

  public Type getType()
  {
    return Type.CLIENT;
  }

  @Override
  public String toString()
  {
    return "ClientTCPConnector[" + host + ":" + port + "]";
  }

  @Override
  protected void onAccessBeforeActivate() throws Exception
  {
    super.onAccessBeforeActivate();
    if (host == null || host.length() == 0)
    {
      throw new IllegalArgumentException("host == null || host.length() == 0");
    }
  }

  @Override
  protected void onActivate() throws Exception
  {
    super.onActivate();
    SelectionKey selKey = getSelectionKey();
    selKey.interestOps(selKey.interestOps() | SelectionKey.OP_CONNECT);

    InetAddress addr = InetAddress.getByName(host);
    InetSocketAddress sAddr = new InetSocketAddress(addr, port);
    getSocketChannel().connect(sAddr);
  }
}
