/*
 * Copyright (c) 2004 - 2012 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Christian W. Damus (CEA) - initial API and implementation
 */
package org.eclipse.emf.cdo.tests.legacy.model5.impl;

import org.eclipse.emf.cdo.tests.legacy.model5.Model5Package;
import org.eclipse.emf.cdo.tests.model5.Child;
import org.eclipse.emf.cdo.tests.model5.Parent;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Parent</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.cdo.tests.legacy.model5.impl.ParentImpl#getChildren <em>Children</em>}</li>
 *   <li>{@link org.eclipse.emf.cdo.tests.legacy.model5.impl.ParentImpl#getFavourite <em>Favourite</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ParentImpl extends EObjectImpl implements Parent
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final String copyright = "Copyright (c) 2004 - 2012 Eike Stepper (Berlin, Germany) and others.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n   Eike Stepper - initial API and implementation";

  /**
   * The cached value of the '{@link #getChildren() <em>Children</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getChildren()
   * @generated
   * @ordered
   */
  protected EList<Child> children;

  /**
   * The cached value of the '{@link #getFavourite() <em>Favourite</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFavourite()
   * @generated
   * @ordered
   */
  protected Child favourite;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ParentImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return Model5Package.eINSTANCE.getParent();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Child> getChildren()
  {
    if (children == null)
    {
      children = new EObjectContainmentWithInverseEList<Child>(Child.class, this, Model5Package.PARENT__CHILDREN,
          Model5Package.CHILD__PARENT);
    }
    return children;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Child getFavourite()
  {
    if (favourite != null && favourite.eIsProxy())
    {
      InternalEObject oldFavourite = (InternalEObject)favourite;
      favourite = (Child)eResolveProxy(oldFavourite);
      if (favourite != oldFavourite)
      {
        if (eNotificationRequired())
        {
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, Model5Package.PARENT__FAVOURITE, oldFavourite,
              favourite));
        }
      }
    }
    return favourite;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Child basicGetFavourite()
  {
    return favourite;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetFavourite(Child newFavourite, NotificationChain msgs)
  {
    Child oldFavourite = favourite;
    favourite = newFavourite;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Model5Package.PARENT__FAVOURITE,
          oldFavourite, newFavourite);
      if (msgs == null)
      {
        msgs = notification;
      }
      else
      {
        msgs.add(notification);
      }
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setFavourite(Child newFavourite)
  {
    if (newFavourite != favourite)
    {
      NotificationChain msgs = null;
      if (favourite != null)
      {
        msgs = ((InternalEObject)favourite).eInverseRemove(this, Model5Package.CHILD__PREFERRED_BY, Child.class, msgs);
      }
      if (newFavourite != null)
      {
        msgs = ((InternalEObject)newFavourite).eInverseAdd(this, Model5Package.CHILD__PREFERRED_BY, Child.class, msgs);
      }
      msgs = basicSetFavourite(newFavourite, msgs);
      if (msgs != null)
      {
        msgs.dispatch();
      }
    }
    else if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, Model5Package.PARENT__FAVOURITE, newFavourite, newFavourite));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
    case Model5Package.PARENT__CHILDREN:
      return ((InternalEList<InternalEObject>)(InternalEList<?>)getChildren()).basicAdd(otherEnd, msgs);
    case Model5Package.PARENT__FAVOURITE:
      if (favourite != null)
      {
        msgs = ((InternalEObject)favourite).eInverseRemove(this, Model5Package.CHILD__PREFERRED_BY, Child.class, msgs);
      }
      return basicSetFavourite((Child)otherEnd, msgs);
    }
    return super.eInverseAdd(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
    case Model5Package.PARENT__CHILDREN:
      return ((InternalEList<?>)getChildren()).basicRemove(otherEnd, msgs);
    case Model5Package.PARENT__FAVOURITE:
      return basicSetFavourite(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
    case Model5Package.PARENT__CHILDREN:
      return getChildren();
    case Model5Package.PARENT__FAVOURITE:
      if (resolve)
      {
        return getFavourite();
      }
      return basicGetFavourite();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
    case Model5Package.PARENT__CHILDREN:
      getChildren().clear();
      getChildren().addAll((Collection<? extends Child>)newValue);
      return;
    case Model5Package.PARENT__FAVOURITE:
      setFavourite((Child)newValue);
      return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
    case Model5Package.PARENT__CHILDREN:
      getChildren().clear();
      return;
    case Model5Package.PARENT__FAVOURITE:
      setFavourite((Child)null);
      return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
    case Model5Package.PARENT__CHILDREN:
      return children != null && !children.isEmpty();
    case Model5Package.PARENT__FAVOURITE:
      return favourite != null;
    }
    return super.eIsSet(featureID);
  }

} // ParentImpl
