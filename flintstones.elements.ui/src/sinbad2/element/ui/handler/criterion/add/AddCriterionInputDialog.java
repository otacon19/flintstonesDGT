package sinbad2.element.ui.handler.criterion.add;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import sinbad2.element.criterion.Criterion;

public class AddCriterionInputDialog extends InputDialog {
	
	public AddCriterionInputDialog(Shell parentShell, String dialogTitle,
			String dialogMessage, String initialValue, AddCriterionInputValidator validator, Criterion parent) {
		super(parentShell, dialogTitle, dialogMessage, initialValue, validator);
		
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = null;
		container = (Composite) super.createDialogArea(parent);
		
		return container;
	}
	
	@Override
	public String getValue() {
		return super.getValue().trim();
	}
}
