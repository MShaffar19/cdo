/*
 * Copyright (c) 2004 - 2012 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.transfer.ui.widgets;

import org.eclipse.emf.cdo.transfer.CDOTransfer;
import org.eclipse.emf.cdo.transfer.CDOTransferMapping;
import org.eclipse.emf.cdo.transfer.ui.TransferContentProvider;
import org.eclipse.emf.cdo.transfer.ui.TransferLabelProvider;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

/**
 * @author Eike Stepper
 * @since 4.2
 */
public class TransferTreeComposite extends Composite
{
  private CDOTransfer transfer;

  private TreeViewer treeViewer;

  public TransferTreeComposite(Composite parent, int style, CDOTransfer transfer)
  {
    super(parent, style);
    this.transfer = transfer;

    setLayout(new FillLayout(SWT.VERTICAL));
    treeViewer = new TreeViewer(this, SWT.NONE);

    Tree tree = treeViewer.getTree();
    tree.setLinesVisible(true);
    tree.setHeaderVisible(true);

    TreeViewerColumn sourceViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
    TreeColumn sourceColumn = sourceViewerColumn.getColumn();
    sourceColumn.setWidth(350);
    sourceColumn.setText("Source");

    TreeViewerColumn typeViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
    TreeColumn typeColumn = typeViewerColumn.getColumn();
    typeColumn.setWidth(100);
    typeColumn.setText("Type");

    TreeViewerColumn targetViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
    TreeColumn targetColumn = targetViewerColumn.getColumn();
    targetColumn.setWidth(450);
    targetColumn.setText("Target");

    TreeViewerColumn statusViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
    TreeColumn statusColumn = statusViewerColumn.getColumn();
    statusColumn.setWidth(100);
    statusColumn.setText("Status");

    treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
    treeViewer.setContentProvider(new TransferContentProvider());
    treeViewer.setLabelProvider(new TransferLabelProvider(transfer));
    treeViewer.setInput(transfer.getRootMapping());
  }

  public CDOTransfer getTransfer()
  {
    return transfer;
  }

  public TreeViewer getViewer()
  {
    return treeViewer;
  }

  public CDOTransferMapping getSelectedMapping()
  {
    IStructuredSelection selection = (IStructuredSelection)treeViewer.getSelection();
    return (CDOTransferMapping)selection.getFirstElement();
  }

  @Override
  public boolean setFocus()
  {
    return treeViewer.getTree().setFocus();
  }
}
