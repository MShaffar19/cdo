/**
 * Copyright (c) 2004 - 2009 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.internal.server.protocol;

import org.eclipse.emf.cdo.common.io.CDODataInput;
import org.eclipse.emf.cdo.common.io.CDODataOutput;
import org.eclipse.emf.cdo.common.protocol.CDOProtocolConstants;
import org.eclipse.emf.cdo.internal.server.bundle.OM;

import org.eclipse.net4j.util.om.trace.ContextTracer;

import org.eclipse.emf.ecore.EPackage;

import java.io.IOException;

/**
 * @author Eike Stepper
 */
public class LoadPackageIndication extends CDOReadIndication
{
  private static final ContextTracer PROTOCOL_TRACER = new ContextTracer(OM.DEBUG_PROTOCOL, LoadPackageIndication.class);

  private EPackage cdoPackage;

  private boolean onlyEcore;

  public LoadPackageIndication(CDOServerProtocol protocol)
  {
    super(protocol, CDOProtocolConstants.SIGNAL_LOAD_PACKAGES);
  }

  @Override
  protected void indicating(CDODataInput in) throws IOException
  {
    String packageURI = in.readCDOPackageURI();
    if (PROTOCOL_TRACER.isEnabled())
    {
      PROTOCOL_TRACER.format("Read packageURI: {0}", packageURI);
    }

    onlyEcore = in.readBoolean();
    if (PROTOCOL_TRACER.isEnabled())
    {
      PROTOCOL_TRACER.format("Read onlyEcore: {0}", onlyEcore);
    }

    cdoPackage = getRepository().getPackageRegistry().getEPackage(packageURI);
    if (cdoPackage == null)
    {
      throw new IllegalStateException("CDO package not found: " + packageURI);
    }
  }

  @Override
  protected void responding(CDODataOutput out) throws IOException
  {
    if (onlyEcore)
    {
      String ecore = cdoPackage.getEcore();
      if (PROTOCOL_TRACER.isEnabled())
      {
        PROTOCOL_TRACER.format("Writing ecore:\n{0}", ecore);
      }

      out.writeString(ecore);
    }
    else
    {
      if (PROTOCOL_TRACER.isEnabled())
      {
        PROTOCOL_TRACER.format("Writing package: {0}", cdoPackage);
      }

      out.writeEPackage(cdoPackage);
    }
  }
}
