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
package org.eclipse.net4j.util.stream;

import org.eclipse.net4j.transport.Channel;

/**
 * @author Eike Stepper
 */
public class ChannelInputStream extends BufferInputStream
{
  private Channel channel;

  private long millisBeforeTimeout = DEFAULT_MILLIS_BEFORE_TIMEOUT;

  private long millisInterruptCheck = DEFAULT_MILLIS_INTERRUPT_CHECK;

  public ChannelInputStream(Channel channel)
  {
    this(channel, DEFAULT_MILLIS_BEFORE_TIMEOUT);
  }

  public ChannelInputStream(Channel channel, long millisBeforeTimeout)
  {
    this.channel = channel;
    channel.setReceiveHandler(this);
    this.millisBeforeTimeout = millisBeforeTimeout;
    millisInterruptCheck = DEFAULT_MILLIS_INTERRUPT_CHECK;
  }

  public Channel getChannel()
  {
    return channel;
  }

  public long getMillisBeforeTimeout()
  {
    return millisBeforeTimeout;
  }

  public void setMillisBeforeTimeout(long millisBeforeTimeout)
  {
    this.millisBeforeTimeout = millisBeforeTimeout;
  }

  public long getMillisInterruptCheck()
  {
    return millisInterruptCheck;
  }

  public void setMillisInterruptCheck(long millisInterruptCheck)
  {
    this.millisInterruptCheck = millisInterruptCheck;
  }

  @Override
  public String toString()
  {
    return "ChannelInputStream[" + channel + "]";
  }
}
