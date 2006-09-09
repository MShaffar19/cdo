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
package org.eclipse.emf.cdo.server;


import org.eclipse.net4j.core.Channel;

import org.eclipse.emf.cdo.core.CDOProtocol;

import org.springframework.transaction.support.TransactionTemplate;

import java.util.Collection;


public interface ServerCDOProtocol extends CDOProtocol
{
  public Mapper getMapper();

  public TransactionTemplate getTransactionTemplate();

  public ServerCDOResProtocol getServerCDOResProtocol();

  public void fireRemovalNotification(Collection<Integer> rids);

  public void fireInvalidationNotification(Channel initiator, Collection<Long> changedObjectIds);
}
