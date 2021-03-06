/*
 * Copyright (c) 2010-2012, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Martin Fluegge - initial API and implementation
 */
package org.eclipse.emf.cdo.dawn.editors;

import org.eclipse.emf.cdo.dawn.spi.IDawnUIElement;

import org.eclipse.ui.IEditorPart;

/**
 * @author Martin Fluegge
 */
public interface IDawnEditor extends IDawnUIElement, IEditorPart
{
  public String getContributorID();
}
