package sinbad2.resolutionphase.analysis.ui.provider;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

import sinbad2.element.ProblemElementsManager;
import sinbad2.element.ProblemElementsSet;
import sinbad2.element.mec.MEC;


public class MECContentProvider implements IStructuredContentProvider {
	
	private ProblemElementsManager _elementManager;
	private ProblemElementsSet _elementSet;
	private List<MEC> _mecs;
	
	
	public MECContentProvider() {
		_mecs = null;
	}
	
	public MECContentProvider(TableViewer tableViewer) {
		_elementManager = ProblemElementsManager.getInstance();
		_elementSet = _elementManager.getActiveElementSet();
		_mecs = _elementSet.getMecs();
	}
	
	@Override
	public void dispose() {}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(Object inputElement) {
		return ((List<MEC>)inputElement).toArray();
	}
	
	public Object getInput() {
		return _mecs;
	}
}