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
package org.eclipse.net4j.internal.debug.views;

import org.eclipse.net4j.internal.debug.bundle.OM;
import org.eclipse.net4j.util.ReflectUtil;
import org.eclipse.net4j.util.collection.Pair;
import org.eclipse.net4j.util.event.EventUtil;
import org.eclipse.net4j.util.event.IEvent;
import org.eclipse.net4j.util.event.IListener;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @author Eike Stepper
 */
public class IntrospectorView extends ViewPart implements ISelectionListener, IDoubleClickListener, IListener
{
  private static final Object[] NO_ELEMENTS = {};

  private TableViewer currentViewer;

  private TableViewer objectViewer;

  private TableViewer iterableViewer;

  private TableViewer mapViewer;

  private Stack<Object> elements = new Stack<Object>();

  private Text classLabel;

  private Text objectLabel;

  private IAction backAction = new BackAction();

  private StackLayout stackLayout;

  private Composite stacked;

  public IntrospectorView()
  {
  }

  @Override
  public void dispose()
  {
    getSite().getPage().removeSelectionListener(this);
    super.dispose();
  }

  @Override
  public void createPartControl(Composite parent)
  {
    Color bg = parent.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
    Color gray = parent.getDisplay().getSystemColor(SWT.COLOR_DARK_BLUE);

    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(newGrid(1));

    Composite c = new Composite(composite, SWT.BORDER);
    c.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    c.setLayout(newGrid(2));

    classLabel = new Text(c, SWT.READ_ONLY);
    classLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
    classLabel.setBackground(bg);
    classLabel.setForeground(gray);

    objectLabel = new Text(c, SWT.READ_ONLY);
    objectLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    objectLabel.setBackground(bg);

    stackLayout = new StackLayout();
    stacked = new Composite(composite, SWT.NONE);
    stacked.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    stacked.setLayout(stackLayout);

    objectViewer = createViewer(stacked);
    createObjectColmuns();
    objectViewer.addDoubleClickListener(this);
    objectViewer.setContentProvider(new ObjectContentProvider());
    objectViewer.setLabelProvider(new ObjectLabelProvider());
    objectViewer.setSorter(new NameSorter());
    objectViewer.setInput(getViewSite());

    iterableViewer = createViewer(stacked);
    createIterableColmuns();
    iterableViewer.addDoubleClickListener(this);
    iterableViewer.setContentProvider(new IterableContentProvider());
    iterableViewer.setLabelProvider(new IterableLabelProvider());
    iterableViewer.setInput(getViewSite());

    mapViewer = createViewer(stacked);
    createMapColmuns();
    mapViewer.addDoubleClickListener(this);
    mapViewer.setContentProvider(new MapContentProvider());
    mapViewer.setLabelProvider(new MapLabelProvider());
    mapViewer.setSorter(new NameSorter());
    mapViewer.setInput(getViewSite());

    IActionBars bars = getViewSite().getActionBars();
    fillLocalPullDown(bars.getMenuManager());
    fillLocalToolBar(bars.getToolBarManager());
    getSite().getPage().addSelectionListener(this);
    setCurrentViewer(objectViewer);
  }

  private void setCurrentViewer(TableViewer viewer)
  {
    currentViewer = viewer;
    stackLayout.topControl = currentViewer.getControl();
    stacked.layout();
  }

  private TableViewer createViewer(Composite parent)
  {
    TableViewer viewer = new TableViewer(parent, SWT.FULL_SELECTION | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
    viewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    viewer.getTable().setHeaderVisible(true);
    viewer.getTable().setLinesVisible(true);
    return viewer;
  }

  public void refreshViewer()
  {
    try
    {
      currentViewer.getControl().getDisplay().asyncExec(new Runnable()
      {
        public void run()
        {
          try
          {
            currentViewer.refresh();
          }
          catch (RuntimeException ignore)
          {
          }
        }
      });
    }
    catch (RuntimeException ignore)
    {
    }
  }

  public void selectionChanged(IWorkbenchPart part, ISelection sel)
  {
    if (part == this)
    {
      return;
    }

    if (sel instanceof IStructuredSelection)
    {
      IStructuredSelection ssel = (IStructuredSelection)sel;
      elements.clear();
      setObject(ssel.getFirstElement());
    }
    else
    {
      setObject(null);
    }
  }

  public void doubleClick(DoubleClickEvent event)
  {
    ISelection sel = event.getSelection();
    if (sel instanceof IStructuredSelection)
    {
      IStructuredSelection ssel = (IStructuredSelection)sel;
      Object element = ssel.getFirstElement();
      if (currentViewer == objectViewer && element instanceof Pair)
      {
        Pair<Field, Object> pair = (Pair<Field, Object>)element;
        Field field = pair.getElement1();
        if (!field.getType().isPrimitive())
        {
          setObject(pair.getElement2());
        }
      }
      else if (currentViewer == mapViewer && element instanceof Map.Entry)
      {
        Map.Entry<?, ?> entry = (Map.Entry<?, ?>)element;
        setObject(entry.getValue());
      }
      else if (currentViewer == iterableViewer)
      {
        setObject(element);
      }
    }
  }

  /**
   * Passing the focus request to the viewer's control.
   */
  @Override
  public void setFocus()
  {
    try
    {
      currentViewer.getControl().setFocus();
    }
    catch (RuntimeException ignore)
    {
    }
  }

  public void notifyEvent(IEvent event)
  {
    refreshViewer();
  }

  private void setObject(Object object)
  {
    EventUtil.removeListener(object, this);
    if (object != null)
    {
      if (!elements.isEmpty())
      {
        Object element = elements.peek();
        if (element != object)
        {
          EventUtil.removeListener(element, this);
          elements.push(object);
        }
      }
      else
      {
        elements.push(object);
      }
    }

    if (object == null)
    {
      classLabel.setText("");
      objectLabel.setText("");
      currentViewer = objectViewer;
    }
    else
    {
      EventUtil.addListener(object, this);
      String className = object.getClass().getName();
      classLabel.setText(className);

      String value = object.toString();
      if (value.startsWith(className + "@"))
      {
        objectLabel.setText(value.substring(className.length()));
      }
      else
      {
        objectLabel.setText(value);
      }
    }

    classLabel.getParent().layout();
    backAction.setEnabled(elements.size() >= 2);

    if (object instanceof Map)
    {
      setCurrentViewer(mapViewer);
    }
    else if (object instanceof Iterable)
    {
      setCurrentViewer(iterableViewer);
    }
    else
    {
      setCurrentViewer(objectViewer);
    }

    refreshViewer();
  }

  private GridLayout newGrid(int numColumns)
  {
    GridLayout grid = new GridLayout(numColumns, false);
    grid.marginTop = 0;
    grid.marginLeft = 0;
    grid.marginRight = 0;
    grid.marginBottom = 0;
    grid.marginWidth = 0;
    grid.marginHeight = 0;
    grid.horizontalSpacing = 0;
    grid.verticalSpacing = 0;
    return grid;
  }

  private void createObjectColmuns()
  {
    Table table = objectViewer.getTable();
    String[] columnNames = { "Field", "Value", "Declared Type", "Concrete Type" };
    int[] columnWidths = { 200, 400, 300, 300 };
    createColumns(table, columnNames, columnWidths);
  }

  private void createMapColmuns()
  {
    Table table = mapViewer.getTable();
    String[] columnNames = { "Key", "Value", "Type" };
    int[] columnWidths = { 200, 400, 300 };
    createColumns(table, columnNames, columnWidths);
  }

  private void createIterableColmuns()
  {
    Table table = iterableViewer.getTable();
    String[] columnNames = { "Element", "Type" };
    int[] columnWidths = { 400, 300 };
    createColumns(table, columnNames, columnWidths);
  }

  private void createColumns(Table table, String[] columnNames, int[] columnWidths)
  {
    TableColumn[] columns = new TableColumn[columnNames.length];
    for (int i = 0; i < columns.length; i++)
    {
      TableColumn column = new TableColumn(table, SWT.LEFT, i);
      column.setText(columnNames[i]);
      column.setWidth(columnWidths[i]);
      column.setMoveable(true);
      column.setResizable(true);
    }
  }

  private void fillLocalPullDown(IMenuManager manager)
  {
  }

  private void fillLocalToolBar(IToolBarManager manager)
  {
    manager.add(backAction);
  }

  /**
   * @author Eike Stepper
   */
  class BackAction extends Action
  {
    private BackAction()
    {
      super("Back");
      ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
      setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_BACK));
      setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_BACK_DISABLED));
    }

    @Override
    public void run()
    {
      if (!elements.isEmpty())
      {
        elements.pop();
        if (!elements.isEmpty())
        {
          setObject(elements.peek());
        }
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  abstract class AbstractContentProvider implements IStructuredContentProvider
  {
    public void inputChanged(Viewer v, Object oldInput, Object newInput)
    {
    }

    public void dispose()
    {
    }
  }

  /**
   * @author Eike Stepper
   */
  abstract class AbstractLabelProvider extends LabelProvider implements ITableLabelProvider
  {
    @Override
    public String getText(Object element)
    {
      return getColumnText(element, 0);
    }

    public Image getColumnImage(Object obj, int index)
    {
      return null;
    }

    @Override
    public Image getImage(Object obj)
    {
      return null;
    }
  }

  /**
   * @author Eike Stepper
   */
  class ObjectContentProvider extends AbstractContentProvider
  {
    public Object[] getElements(Object parent)
    {
      if (!elements.isEmpty())
      {
        try
        {
          return ReflectUtil.dumpToArray(elements.peek());
        }
        catch (RuntimeException ex)
        {
          OM.LOG.error(ex);
        }
      }

      return NO_ELEMENTS;
    }
  }

  /**
   * @author Eike Stepper
   */
  class ObjectLabelProvider extends AbstractLabelProvider
  {
    public String getColumnText(Object obj, int index)
    {
      if (obj instanceof Pair)
      {
        try
        {
          Pair<Field, Object> pair = (Pair<Field, Object>)obj;
          Field field = pair.getElement1();
          Object value = pair.getElement2();
          switch (index)
          {
          case 0:
            return field.getName();
          case 1:
            return value == null ? "null" : value.toString();
          case 2:
            return field.getType().getName();
          case 3:
            return value == null ? "" : value.getClass().getName();
          }
        }
        catch (RuntimeException ex)
        {
          OM.LOG.error(ex);
        }
      }

      return "";
    }
  }

  /**
   * @author Eike Stepper
   */
  class IterableContentProvider extends AbstractContentProvider
  {
    public Object[] getElements(Object parent)
    {
      if (!elements.isEmpty())
      {
        Object element = elements.peek();
        if (element instanceof Iterable)
        {
          List<Object> result = new ArrayList<Object>();
          for (Object object : (Iterable<Object>)element)
          {
            result.add(object);
          }

          return result.toArray();
        }
      }

      return NO_ELEMENTS;
    }
  }

  /**
   * @author Eike Stepper
   */
  class IterableLabelProvider extends AbstractLabelProvider
  {
    public String getColumnText(Object obj, int index)
    {
      switch (index)
      {
      case 0:
        return obj == null ? "null" : obj.toString();
      case 1:
        return obj == null ? "" : obj.getClass().getName();
      }

      return "";
    }
  }

  /**
   * @author Eike Stepper
   */
  class MapContentProvider extends AbstractContentProvider
  {
    public Object[] getElements(Object parent)
    {
      if (!elements.isEmpty())
      {
        Object element = elements.peek();
        if (element instanceof Map)
        {
          return ((Map<?, ?>)element).entrySet().toArray();
        }
      }

      return NO_ELEMENTS;
    }
  }

  /**
   * @author Eike Stepper
   */
  class MapLabelProvider extends AbstractLabelProvider
  {
    public String getColumnText(Object obj, int index)
    {
      if (obj instanceof Map.Entry)
      {
        Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
        Object key = entry.getKey();
        Object value = entry.getValue();
        switch (index)
        {
        case 0:
          return key == null ? "null" : key.toString();
        case 1:
          return value == null ? "null" : value.toString();
        case 2:
          return value == null ? "" : value.getClass().getName();
        }
      }

      return "";
    }
  }

  /**
   * @author Eike Stepper
   */
  class NameSorter extends ViewerSorter
  {
  }
}