/*
 * Copyright (c) 2013, 2019 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.internal.db.ddl;

import org.eclipse.net4j.db.ddl.IDBField;
import org.eclipse.net4j.db.ddl.IDBIndex;
import org.eclipse.net4j.db.ddl.IDBIndexField;
import org.eclipse.net4j.db.ddl.IDBSchema;
import org.eclipse.net4j.db.ddl.IDBSchemaElement;
import org.eclipse.net4j.db.ddl.IDBSchemaVisitor;
import org.eclipse.net4j.spi.db.ddl.InternalDBIndex;
import org.eclipse.net4j.spi.db.ddl.InternalDBIndexField;
import org.eclipse.net4j.spi.db.ddl.InternalDBSchema;

import java.util.List;

/**
 * @author Eike Stepper
 */
public class DBIndexField extends DBSchemaElement implements InternalDBIndexField
{
  private static final long serialVersionUID = 1L;

  private IDBIndex index;

  private IDBField field;

  private int position;

  public DBIndexField(IDBIndex index, IDBField field, int position)
  {
    super(field.getName());
    this.index = index;
    this.field = field;
    this.position = position;
  }

  /**
   * Constructor for deserialization.
   */
  protected DBIndexField()
  {
  }

  @Override
  public IDBIndexField getWrapper()
  {
    return (IDBIndexField)super.getWrapper();
  }

  @Override
  public SchemaElementType getSchemaElementType()
  {
    return SchemaElementType.INDEX_FIELD;
  }

  @Override
  public IDBIndex getIndex()
  {
    return index;
  }

  @Override
  public IDBIndex getParent()
  {
    return getIndex();
  }

  @Override
  public IDBField getField()
  {
    return field;
  }

  @Override
  public int getPosition()
  {
    return position;
  }

  @Override
  public void setPosition(int position)
  {
    assertUnlocked();
    this.position = position;
  }

  @Override
  public IDBSchema getSchema()
  {
    return field.getSchema();
  }

  @Override
  public String getFullName()
  {
    return field.getFullName();
  }

  @Override
  public void remove()
  {
    ((InternalDBIndex)index).removeIndexField(this);
  }

  @Override
  protected void collectElements(List<IDBSchemaElement> elements)
  {
    // Do nothing
  }

  @Override
  protected void doAccept(IDBSchemaVisitor visitor)
  {
    visitor.visit(this);
  }

  private void assertUnlocked()
  {
    ((InternalDBSchema)index.getTable().getSchema()).assertUnlocked();
  }
}
