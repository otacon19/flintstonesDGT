package sinbad2.element.ui.handler.criterion.modify;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;


public class ModifyCriterionInputDialog extends InputDialog {
	
	
	public ModifyCriterionInputDialog(Shell parentShell, String dialogTitle,
			String dialogMessage, String initialValue, ModifyCriterionInputValidator validator) {
		super(parentShell, dialogTitle, dialogMessage, initialValue, validator);
		
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = null;
		
		container =(Composite) super.createDialogArea(parent);
		
		return container;
	}
	
	@Override
	public String getValue() {
		return super.getValue().trim();
	}

}
