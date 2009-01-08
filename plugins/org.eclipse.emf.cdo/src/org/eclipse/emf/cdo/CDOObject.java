/***************************************************************************
 * Copyright (c) 2004 - 2008 Eike Stepper, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 **************************************************************************/
package org.eclipse.emf.cdo;

import org.eclipse.emf.cdo.common.id.CDOID;
import org.eclipse.emf.cdo.common.id.CDOIDTemp;
import org.eclipse.emf.cdo.common.model.CDOClass;
import org.eclipse.emf.cdo.common.revision.CDORevision;
import org.eclipse.emf.cdo.eresource.CDOResource;

import org.eclipse.emf.internal.cdo.InternalCDOObject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * A specialalized subinterface of {@link EObject} that is exposed by all CDO objects and allows access to special CDO
 * properties and features of those objects.
 * <p>
 * Note that, by contract, every instance of CDOObject can also be cast to {@link InternalCDOObject}.
 * 
 * @author Eike Stepper
 */
public interface CDOObject extends EObject
{
  /**
   * Returns the <em>technical</em> object identifier of this object, or <code>null</code> if the {@link #cdoState()
   * state} of this object is {@link CDOState#TRANSIENT TRANSIENT} or {@link CDOState#INVALID INVALID}.
   * <p>
   * If the state of this object is {@link CDOState#NEW NEW} the returned CDOID instance can be cast to
   * {@link CDOIDTemp} and is unique in the scope of the associated {@link #cdoView() transaction}. In all other states
   * a non-<code>null</code> return value uniquely identifies a persistent object in the scope of the whole repository.
   * 
   * @see #cdoState()
   */
  public CDOID cdoID();

  /**
   * Returns the immutable {@link CDOClass class} of this object. This method must only be called if this object is
   * properly associated with a {@link CDOView view}, i.e. if {@link #cdoView()} does not return <code>null</code> .
   * 
   * @return Never <code>null</code>.
   */
  public CDOClass cdoClass();

  /**
   * Returns the local {@link CDOState state} of this object.
   */
  public CDOState cdoState();

  /**
   * Returns <code>true</code> if this object contains local changes that are conflicting with recognized remote
   * changes, <code>false</code> otherwise.
   * <p>
   * This method is a convenience method to determine whether the {@link #cdoState() state} of this object is either
   * {@link CDOState.CONFLICT CONFLICT} or {@link CDOState.INVALID_CONFLICT INVALID_CONFLICT}.
   * 
   * @since 2.0
   */
  public boolean cdoConflict();

  /**
   * Returns <code>true</code> if this object is considered as locally invalid (TODO Simon: please briefly explain what
   * this state means) , <code>false</code> otherwise.
   * <p>
   * This method is a convenience method to determine whether the {@link #cdoState() state} of this object is either
   * {@link CDOState.INVALID INVALID} or {@link CDOState.INVALID_CONFLICT INVALID_CONFLICT}.
   * 
   * @since 2.0
   */
  public boolean cdoInvalid();

  /**
   * Returns the {@link CDOView view} this object is associated with, or <code>null</code> if this object is not
   * associated with a view. This view manages all aspects of this object and cahces it as long as required.
   */
  public CDOView cdoView();

  /**
   * Returns the {@link CDORevision revision} of this object, or <code>null</code> if this object does currently not
   * have a revision. The revision is used to store all modeled data of this object, together with some technical data
   * required by the framework.
   */
  public CDORevision cdoRevision();

  /**
   * Returns the {@link CDOResource resource} of this object, no matter where this object is located in the containment
   * tree of that resource, or <code>null</code> if this object is not contained in a CDO resource.
   * <p>
   * This method may not return <code>null</code> return for objects that have no {@link #cdoDirectResource() direct
   * resource}. Please note that, depending on the containment depth of this object, the evaluation of the resource can
   * be a costly operation.
   * 
   * @see #cdoDirectResource()
   */
  public CDOResource cdoResource();

  /**
   * Returns the directly containing {@link CDOResource resource} of this object, or <code>null</code> if this object is
   * not an element of the {@link Resource#getContents() contents} list of any CDO resource.
   * <p>
   * Please note that, independend of the containment depth of this object, the evaluation of the direct resource is an
   * operation with a constant cost.
   * 
   * @since 2.0
   */
  public CDOResource cdoDirectResource();

  /**
   * Returns the read lock associated with this object.
   * 
   * @return Never <code>null</code>.
   * @since 2.0
   */
  public CDOLock cdoReadLock();

  /**
   * Returns the write lock associated with this object.
   * 
   * @return Never <code>null</code>.
   * @since 2.0
   */
  public CDOLock cdoWriteLock();

  /**
   * TODO: JavaDoc
   */
  public void cdoReload();
}
