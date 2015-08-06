package sinbad2.resolutionphase.analysis.ui.provider;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

import sinbad2.element.alternative.Alternative;

public class MECIdLabelProvider extends ColumnLabelProvider {
	
	@Override
	public String getText(Object obj) {
		return ((Alternative) obj).getId();
	}

	@Override
	public Image getImage(Object element) {
		return null;
	}
	
}
