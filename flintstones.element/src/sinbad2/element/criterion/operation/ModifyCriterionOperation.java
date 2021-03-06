package sinbad2.element.criterion.operation;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import sinbad2.core.undoable.UndoableOperation;
import sinbad2.element.ProblemElementsSet;
import sinbad2.element.criterion.Criterion;

public class ModifyCriterionOperation extends UndoableOperation {
	
	private ProblemElementsSet _elementSet;
	private Criterion _modifyCriterion;
	private List<Criterion> _brothers;
	private String _newId;
	private String _oldId;
	

	public ModifyCriterionOperation(String label, Criterion modifyCriterion, String newId, ProblemElementsSet elementSet) {
		super(label);
		
		_elementSet = elementSet;
		_modifyCriterion = modifyCriterion;
		_newId = newId;
		_oldId = _modifyCriterion.getId();
		
		_brothers = _elementSet.getCriteria();
	}
	
	@Override
	public IStatus executeOperation(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return redo(monitor, info);
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		
		Criterion oldCriterion = (Criterion) _modifyCriterion.clone();

		_elementSet.modifyCriterion(_modifyCriterion, _newId, _inUndoRedo);
		
		if(!_newId.equals(oldCriterion.getId())) {
			Collections.sort(_brothers);
			
		}
		
		return Status.OK_STATUS;
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		
		_elementSet.modifyCriterion(_modifyCriterion, _oldId, _inUndoRedo);
		
		if(!_oldId.equals(_newId)) {
			Collections.sort(_brothers);
		}
		
		return Status.OK_STATUS;
	}

}
