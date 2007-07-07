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
package org.eclipse.emf.cdo.internal.server;

import org.eclipse.emf.cdo.internal.protocol.revision.CDORevisionImpl;
import org.eclipse.emf.cdo.protocol.CDOID;
import org.eclipse.emf.cdo.server.ITransaction;

import org.eclipse.net4j.internal.util.store.StoreTransaction;
import org.eclipse.net4j.util.store.IStoreManager;

/**
 * @author Eike Stepper
 */
public class EmptyStoreTransaction extends StoreTransaction implements ITransaction
{
  public EmptyStoreTransaction(IStoreManager<ITransaction> storeManager)
  {
    super(storeManager);
  }

  public CDOID loadResourceID(String path)
  {
    return null;
  }

  public String loadResourcePath(CDOID id)
  {
    return null;
  }

  public CDORevisionImpl loadRevision(CDOID id, long timeStamp)
  {
    return null;
  }

  public CDORevisionImpl loadRevision(CDOID id)
  {
    return null;
  }
}