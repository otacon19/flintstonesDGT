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

public class RemoveContextOperation extends UndoableOperation {
	
	private ProblemElementsSet _elementSet;
	private String _removeAlternativeId;

	public RemoveContextOperation(String label, ProblemElementsSet elementSet, String removeAlternativeId) {
		super(label);

		_elementSet = elementSet;
		_removeAlternativeId = removeAlternativeId;
	}

	@Override
	public IStatus executeOperation(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return redo(monitor, info);
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		
		_elementSet.notifyAlternativesChanges(new AlternativesChangeEvent(EAlternativesChange.REMOVE_CONTEXT, _removeAlternativeId, null, _inUndoRedo));
		
		return Status.OK_STATUS;
		
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		
		_elementSet.notifyAlternativesChanges(new AlternativesChangeEvent(EAlternativesChange.ADD_CONTEXT, null, _removeAlternativeId, _inUndoRedo));
		
		return Status.OK_STATUS;
		
	}

}
