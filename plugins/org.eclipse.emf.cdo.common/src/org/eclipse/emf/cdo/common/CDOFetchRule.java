/**
 * Copyright (c) 2004 - 2009 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Simon McDuff - initial API and implementation
 *    Eike Stepper - maintenance
 */
package org.eclipse.emf.cdo.common;

import org.eclipse.emf.cdo.common.io.CDODataInput;
import org.eclipse.emf.cdo.common.io.CDODataOutput;
import org.eclipse.emf.cdo.common.model.CDOPackageRegistry;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon McDuff
 * @since 2.0
 */
public final class CDOFetchRule
{
  private EClass cdoClass;

  private List<EStructuralFeature> features = new ArrayList<EStructuralFeature>(0);

  public CDOFetchRule(EClass cdoClass)
  {
    this.cdoClass = cdoClass;
  }

  public CDOFetchRule(CDODataInput in, CDOPackageRegistry packageManager) throws IOException
  {
    cdoClass = (EClass)in.readCDOClassifierRefAndResolve();
    int size = in.readInt();
    for (int i = 0; i < size; i++)
    {
      int featureID = in.readInt();
      EStructuralFeature feature = cdoClass.getEStructuralFeature(featureID);
      features.add(feature);
    }
  }

  public void write(CDODataOutput out) throws IOException
  {
    out.writeCDOClassifierRef(cdoClass);
    out.writeInt(features.size());
    for (EStructuralFeature feature : features)
    {
      out.writeInt(feature.getFeatureID());
    }
  }

  public EClass getEClass()
  {
    return cdoClass;
  }

  public List<EStructuralFeature> getFeatures()
  {
    return features;
  }

  public void addFeature(EStructuralFeature feature)
  {
    features.add(feature);
  }

  public void removeFeature(EStructuralFeature feature)
  {
    features.remove(feature);

  }

  public boolean isEmpty()
  {
    return features.isEmpty();
  }
}
