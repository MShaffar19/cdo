/*
 * Copyright (c) 2013, 2015, 2016, 2019 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.db.jdbc;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @author Eike Stepper
 */
public abstract class DelegatingConnection implements Connection
{
  // protected static int COUNT;

  private final Connection delegate;

  public DelegatingConnection(Connection delegate)
  {
    this.delegate = delegate;
    // System.out.println("++ Open connections: " + ++COUNT);
    // ReflectUtil.printStackTrace();
  }

  public final Connection getDelegate()
  {
    return delegate;
  }

  @Override
  public abstract PreparedStatement prepareStatement(String sql) throws SQLException;

  @Override
  public abstract PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException;

  @Override
  public abstract PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException;

  @Override
  public abstract PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException;

  @Override
  public abstract PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException;

  @Override
  public abstract PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException;

  @Override
  public Statement createStatement() throws SQLException
  {
    return delegate.createStatement();
  }

  @Override
  public CallableStatement prepareCall(String sql) throws SQLException
  {
    return delegate.prepareCall(sql);
  }

  @Override
  public String nativeSQL(String sql) throws SQLException
  {
    return delegate.nativeSQL(sql);
  }

  @Override
  public void setAutoCommit(boolean autoCommit) throws SQLException
  {
    delegate.setAutoCommit(autoCommit);
  }

  @Override
  public boolean getAutoCommit() throws SQLException
  {
    return delegate.getAutoCommit();
  }

  @Override
  public void commit() throws SQLException
  {
    if (!delegate.getAutoCommit())
    {
      delegate.commit();
    }
  }

  @Override
  public void rollback() throws SQLException
  {
    if (!delegate.getAutoCommit())
    {
      delegate.rollback();
    }
  }

  @Override
  public void close() throws SQLException
  {
    delegate.close();
    // System.out.println("-- Open connections: " + --COUNT);
  }

  @Override
  public boolean isClosed() throws SQLException
  {
    return delegate.isClosed();
  }

  @Override
  public DatabaseMetaData getMetaData() throws SQLException
  {
    return delegate.getMetaData();
  }

  @Override
  public void setReadOnly(boolean readOnly) throws SQLException
  {
    delegate.setReadOnly(readOnly);
  }

  @Override
  public boolean isReadOnly() throws SQLException
  {
    return delegate.isReadOnly();
  }

  @Override
  public void setCatalog(String catalog) throws SQLException
  {
    delegate.setCatalog(catalog);
  }

  @Override
  public String getCatalog() throws SQLException
  {
    return delegate.getCatalog();
  }

  @Override
  public void setTransactionIsolation(int level) throws SQLException
  {
    delegate.setTransactionIsolation(level);
  }

  @Override
  public int getTransactionIsolation() throws SQLException
  {
    return delegate.getTransactionIsolation();
  }

  @Override
  public SQLWarning getWarnings() throws SQLException
  {
    return delegate.getWarnings();
  }

  @Override
  public void clearWarnings() throws SQLException
  {
    delegate.clearWarnings();
  }

  @Override
  public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException
  {
    return delegate.createStatement(resultSetType, resultSetConcurrency);
  }

  @Override
  public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException
  {
    return delegate.prepareCall(sql, resultSetType, resultSetConcurrency);
  }

  @Override
  public Map<String, Class<?>> getTypeMap() throws SQLException
  {
    return delegate.getTypeMap();
  }

  @Override
  public void setTypeMap(Map<String, Class<?>> map) throws SQLException
  {
    delegate.setTypeMap(map);
  }

  @Override
  public void setHoldability(int holdability) throws SQLException
  {
    delegate.setHoldability(holdability);
  }

  @Override
  public int getHoldability() throws SQLException
  {
    return delegate.getHoldability();
  }

  @Override
  public Savepoint setSavepoint() throws SQLException
  {
    return delegate.setSavepoint();
  }

  @Override
  public Savepoint setSavepoint(String name) throws SQLException
  {
    return delegate.setSavepoint(name);
  }

  @Override
  public void rollback(Savepoint savepoint) throws SQLException
  {
    delegate.rollback(savepoint);
  }

  @Override
  public void releaseSavepoint(Savepoint savepoint) throws SQLException
  {
    delegate.releaseSavepoint(savepoint);
  }

  @Override
  public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
  {
    return delegate.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
  }

  @Override
  public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
  {
    return delegate.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
  }

  /**
   * Since JDK 1.6.
   */
  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException
  {
    return delegate.unwrap(iface);
  }

  /**
   * Since JDK 1.6.
   */
  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException
  {
    return delegate.isWrapperFor(iface);
  }

  /**
   * Since JDK 1.6.
   */
  @Override
  public Clob createClob() throws SQLException
  {
    return delegate.createClob();
  }

  /**
   * Since JDK 1.6.
   */
  @Override
  public Blob createBlob() throws SQLException
  {
    return delegate.createBlob();
  }

  /**
   * Since JDK 1.6.
   */
  @Override
  public java.sql.NClob createNClob() throws SQLException
  {
    return delegate.createNClob();
  }

  /**
   * Since JDK 1.6.
   */
  @Override
  public java.sql.SQLXML createSQLXML() throws SQLException
  {
    return delegate.createSQLXML();
  }

  /**
   * Since JDK 1.6.
   */
  @Override
  public boolean isValid(int timeout) throws SQLException
  {
    return delegate.isValid(timeout);
  }

  /**
   * Since JDK 1.6.
   */
  @Override
  public void setClientInfo(String name, String value) throws java.sql.SQLClientInfoException
  {
    delegate.setClientInfo(name, value);
  }

  /**
   * Since JDK 1.6.
   */
  @Override
  public void setClientInfo(Properties properties) throws java.sql.SQLClientInfoException
  {
    delegate.setClientInfo(properties);
  }

  /**
   * Since JDK 1.6.
   */
  @Override
  public String getClientInfo(String name) throws SQLException
  {
    return delegate.getClientInfo(name);
  }

  /**
   * Since JDK 1.6.
   */
  @Override
  public Properties getClientInfo() throws SQLException
  {
    return delegate.getClientInfo();
  }

  /**
   * Since JDK 1.6.
   */
  @Override
  public Array createArrayOf(String typeName, Object[] elements) throws SQLException
  {
    return delegate.createArrayOf(typeName, elements);
  }

  /**
   * Since JDK 1.6.
   */
  @Override
  public Struct createStruct(String typeName, Object[] attributes) throws SQLException
  {
    return delegate.createStruct(typeName, attributes);
  }

  /**
   * Since JDK 1.7.
   */
  @Override
  public void setSchema(String schema) throws SQLException
  {
    delegate.setSchema(schema);
  }

  /**
   * Since JDK 1.7.
   */
  @Override
  public String getSchema() throws SQLException
  {
    return delegate.getSchema();
  }

  /**
   * Since JDK 1.7.
   */
  @Override
  public void abort(Executor executor) throws SQLException
  {
    delegate.abort(executor);
  }

  /**
   * Since JDK 1.7.
   */
  @Override
  public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException
  {
    delegate.setNetworkTimeout(executor, milliseconds);
  }

  /**
   * Since JDK 1.7.
   */
  @Override
  public int getNetworkTimeout() throws SQLException
  {
    return delegate.getNetworkTimeout();
  }

  /**
   * @author Eike Stepper
   */
  public static class Default extends DelegatingConnection
  {
    public Default(Connection delegate)
    {
      super(delegate);
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException
    {
      return getDelegate().prepareStatement(sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException
    {
      return getDelegate().prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
    {
      return getDelegate().prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException
    {
      return getDelegate().prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException
    {
      return getDelegate().prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException
    {
      return getDelegate().prepareStatement(sql, columnNames);
    }
  }
}
