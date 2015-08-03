package sinbad2.element.criterion.operation;

import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import sinbad2.core.undoable.UndoableOperation;
import sinbad2.element.ProblemElementsSet;
import sinbad2.element.criterion.Criterion;

public class RemoveMultipleCriteriaOperation extends UndoableOperation {
	
	private ProblemElementsSet _elementSet;
	private List<Criterion> _removeCriteria;

	public RemoveMultipleCriteriaOperation(String label, List<Criterion> removeCriteria, ProblemElementsSet elementSet) {
		super(label);
		
		_elementSet = elementSet;
		_removeCriteria = removeCriteria;
	}

	@Override
	public IStatus executeOperation(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return redo(monitor, info);
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {

		_elementSet.removeMultipleCriteria(_removeCriteria, _inUndoRedo);
				
		return Status.OK_STATUS;
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		
		_elementSet.addMultipleCriteria(_removeCriteria, _inUndoRedo);

		return Status.OK_STATUS;

	}

}
