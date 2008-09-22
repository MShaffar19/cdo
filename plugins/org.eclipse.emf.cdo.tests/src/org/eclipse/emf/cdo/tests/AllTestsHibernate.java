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
package org.eclipse.emf.cdo.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Eike Stepper
 */
public class AllTestsHibernate extends AllTestsAllConfigs
{
  public static Test suite()
  {
    return new AllTestsHibernate().getTestSuite("CDO Tests (Hibernate)");
  }

  @Override
  protected void initConfigSuites(TestSuite parent)
  {
    initConfigSuite(parent, COMBINED, HIBERNATE, TCP, NATIVE);
  }
}
