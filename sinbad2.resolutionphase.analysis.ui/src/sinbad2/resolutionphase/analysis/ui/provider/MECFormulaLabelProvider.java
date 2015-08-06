package sinbad2.resolutionphase.analysis.ui.provider;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

import sinbad2.element.mec.MEC;

public class MECFormulaLabelProvider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		return null;
	}
	
	@Override
	public Image getImage(Object obj) {
		return ((MEC) obj).getFormula();
	}
}
