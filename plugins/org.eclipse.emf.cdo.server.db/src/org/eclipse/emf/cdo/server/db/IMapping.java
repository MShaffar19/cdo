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
package org.eclipse.emf.cdo.server.db;

import org.eclipse.emf.cdo.internal.protocol.revision.CDORevisionImpl;
import org.eclipse.emf.cdo.protocol.model.CDOClass;
import org.eclipse.emf.cdo.server.IStoreWriter;

import org.eclipse.net4j.db.IDBTable;

import java.util.Set;

/**
 * @author Eike Stepper
 */
public interface IMapping
{
  public IMappingStrategy getMappingStrategy();

  public CDOClass getCDOClass();

  public Set<IDBTable> getAffectedTables();

  public void writeRevision(IStoreWriter writer, CDORevisionImpl revision);
}
