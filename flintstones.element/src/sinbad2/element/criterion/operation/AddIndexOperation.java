package sinbad2.element.criterion.operation;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import sinbad2.core.undoable.UndoableOperation;
import sinbad2.element.ProblemElementsSet;
import sinbad2.element.criterion.listener.CriteriaChangeEvent;
import sinbad2.element.criterion.listener.ECriteriaChange;

public class AddIndexOperation extends UndoableOperation {

	private ProblemElementsSet _elementSet;
	private String _id;

	public AddIndexOperation(String label, String id, ProblemElementsSet elementSet) {
		super(label);
		
		_elementSet = elementSet;
		_id = id;
		
	}

	@Override
	public IStatus executeOperation(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return redo(monitor, info);
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {

		_elementSet.notifyCriteriaChanges(new CriteriaChangeEvent(ECriteriaChange.ADD_INDEX, null, _id, _inUndoRedo));
		
		return Status.OK_STATUS;
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		
		_elementSet.notifyCriteriaChanges(new CriteriaChangeEvent(ECriteriaChange.REMOVE_INDEX, _id, null, _inUndoRedo));
		
		return Status.OK_STATUS;
	}

}
