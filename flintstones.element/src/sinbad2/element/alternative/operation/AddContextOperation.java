package sinbad2.element.alternative.operation;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import sinbad2.core.undoable.UndoableOperation;
import sinbad2.element.ProblemElementsSet;
import sinbad2.element.alternative.listener.AlternativesChangeEvent;
import sinbad2.element.alternative.listener.EAlternativesChange;

public class AddContextOperation extends UndoableOperation {

	private ProblemElementsSet _elementSet;
	private String _addAlternativeId;
	
	public AddContextOperation(String label, ProblemElementsSet elementSet, String addAlternativeId) {
		super(label);
		
		_elementSet = elementSet;
		_addAlternativeId = addAlternativeId;	
	}

	@Override
	public IStatus executeOperation(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return redo(monitor, info);
	}
	
	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		
		_elementSet.notifyAlternativesChanges(new AlternativesChangeEvent(EAlternativesChange.ADD_CONTEXT, null, _addAlternativeId, _inUndoRedo));
		
		return Status.OK_STATUS;
	}
	
	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		
		_elementSet.notifyAlternativesChanges(new AlternativesChangeEvent(EAlternativesChange.ADD_CONTEXT, null, _addAlternativeId, _inUndoRedo));
		
		return Status.OK_STATUS;
	}

}
