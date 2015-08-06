package sinbad2.resolutionphase.analysis.ui.provider;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

import sinbad2.element.ProblemElementsManager;
import sinbad2.element.ProblemElementsSet;
import sinbad2.element.alternative.Alternative;


public class MECContentProvider implements IStructuredContentProvider {
	
	private ProblemElementsManager _elementManager;
	private ProblemElementsSet _elementSet;
	private List<Alternative> _alternatives;
	
	
	public MECContentProvider() {
		_alternatives = null;
	}
	
	public MECContentProvider(TableViewer tableViewer) {
		
		_elementManager = ProblemElementsManager.getInstance();
		_elementSet = _elementManager.getActiveElementSet();
		_alternatives = _elementSet.getAlternatives();
	}
	
	@Override
	public void dispose() {}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

	

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(Object inputElement) {
		return ((List<Alternative>)inputElement).toArray();
	}
	
	public Object getInput() {
		return _alternatives;
	}
}