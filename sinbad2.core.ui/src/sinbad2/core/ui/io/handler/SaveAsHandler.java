package sinbad2.core.ui.io.handler;

import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import sinbad2.core.ui.nls.Messages;
import sinbad2.core.workspace.Workspace;
import sinbad2.core.workspace.WorkspaceContentPersistenceException;

public class SaveAsHandler extends AbstractHandler {
	
public final String ID = "flintstones.core.ui.io.save.as"; //$NON-NLS-1$
	
	private static final String[] FILTER_NAMES = { "Flintstones files (*.flintstones)" }; //$NON-NLS-1$
	private static final String[] FILTER_EXTS = { "*.flintstones" }; //$NON-NLS-1$
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		Workspace workspace = Workspace.getWorkspace();
		
		FileDialog dlg = new FileDialog(shell, SWT.SAVE);
		dlg.setFilterNames(FILTER_NAMES);
		dlg.setFilterExtensions(FILTER_EXTS);
		String fn = dlg.open();
		
		if(fn != null) {
			sinbad2.core.io.handler.SaveHandler handler = null;
			try {
				try {
					handler = new sinbad2.core.io.handler.SaveHandler(fn);
					handler.execute(event);
					if(!fn.equals(workspace.getFileName())) {
						workspace.setFileName(fn);
					}
				} catch(ExecutionException e) {
					String cause = e.getMessage();
					String description = null;
					
					if(IOException.class.getSimpleName().equals(cause)) {
						description = Messages.SaveAsHandler_File_not_saving_permissions;
					} else if(WorkspaceContentPersistenceException.class.getSimpleName()
							.equals(cause)) {
						description = Messages.SaveAsHandler_File_not_saving_wrong;
					} else {
						description = Messages.SaveAsHandler_File_not_saving;
					}
					
					MessageDialog.openError(PlatformUI.getWorkbench().
							getActiveWorkbenchWindow().getShell(), 
							Messages.SaveAsHandler_File_not_saving, description);
				}
			} catch (IllegalArgumentException iae) {
				MessageDialog.openError(shell, Messages.SaveAsHandler_Invalid_file_name, Messages.SaveAsHandler_File_name_not_valid);
			}
		}
		
		return null;
	
	}
	
	
}
