/*
 * Copyright (c) 2013 Eike Stepper (Berlin, Germany), CEA LIST, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Christian W. Damus (CEA LIST) - initial API and implementation
 */
package org.eclipse.net4j.util.ui.security;

import org.eclipse.net4j.util.internal.ui.messages.Messages;
import org.eclipse.net4j.util.security.IPasswordCredentials;
import org.eclipse.net4j.util.security.IPasswordCredentialsUpdate;
import org.eclipse.net4j.util.security.PasswordCredentialsUpdate;
import org.eclipse.net4j.util.ui.UIUtil;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @since 3.4
 */
public class CredentialsUpdateDialog extends CredentialsDialog
{
  private static final String TITLE = Messages.getString("CredentialsUpdateDialog_0"); //$NON-NLS-1$

  private static final String MESSAGE = Messages.getString("CredentialsUpdateDialog_1"); //$NON-NLS-1$

  private static final int HEIGHT = 275;

  private Text newPasswordControl;

  private Text repeatNewPasswordControl;

  public CredentialsUpdateDialog(Shell shell)
  {
    this(shell, null);
  }

  public CredentialsUpdateDialog(Shell shell, String realm)
  {
    super(shell, realm, TITLE, MESSAGE);
  }

  @Override
  protected void configureShell(Shell newShell, int width, int height)
  {
    super.configureShell(newShell, width, HEIGHT);
  }

  @Override
  public IPasswordCredentialsUpdate getCredentials()
  {
    return (IPasswordCredentialsUpdate)super.getCredentials();
  }

  @Override
  protected IPasswordCredentials createCredentials(String userID, char[] password)
  {
    String newPassword = newPasswordControl.getText();
    return new PasswordCredentialsUpdate(userID, password, newPassword.toCharArray());
  }

  @Override
  protected Composite createCredentialsArea(Composite parent)
  {
    Composite result = super.createCredentialsArea(parent);

    ModifyListener newPasswordListener = new ModifyListener()
    {

      public void modifyText(ModifyEvent e)
      {
        validateNewPassword();
      }
    };

    new Label(result, SWT.NONE).setText(Messages.getString("CredentialsUpdateDialog_2")); //$NON-NLS-1$
    newPasswordControl = new Text(result, SWT.BORDER | SWT.PASSWORD);
    newPasswordControl.setLayoutData(UIUtil.createGridData(true, false));
    newPasswordControl.addModifyListener(newPasswordListener);

    new Label(result, SWT.NONE).setText(Messages.getString("CredentialsUpdateDialog_3")); //$NON-NLS-1$
    repeatNewPasswordControl = new Text(result, SWT.BORDER | SWT.PASSWORD);
    repeatNewPasswordControl.setLayoutData(UIUtil.createGridData(true, false));
    repeatNewPasswordControl.addModifyListener(newPasswordListener);

    return result;
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    super.createButtonsForButtonBar(parent);

    getButton(IDialogConstants.OK_ID).setEnabled(false);
  }

  void validateNewPassword()
  {
    String newPassword = newPasswordControl.getText().trim();
    if (newPassword.length() == 0)
    {
      error(Messages.getString("CredentialsUpdateDialog_4")); //$NON-NLS-1$
      return;
    }

    String verify = repeatNewPasswordControl.getText().trim();
    if (verify.length() == 0)
    {
      error(null);
      getButton(IDialogConstants.OK_ID).setEnabled(false);
      return;
    }

    if (!verify.equals(newPassword))
    {
      error(Messages.getString("CredentialsUpdateDialog_5")); //$NON-NLS-1$
      return;
    }

    error(null);
  }

  void error(String message)
  {
    setErrorMessage(message);
    getButton(IDialogConstants.OK_ID).setEnabled(message == null);
  }

}