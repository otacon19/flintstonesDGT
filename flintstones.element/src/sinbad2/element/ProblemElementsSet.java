package sinbad2.element;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang.builder.HashCodeBuilder;

import sinbad2.core.validator.Validator;
import sinbad2.core.workspace.Workspace;
import sinbad2.element.alternative.Alternative;
import sinbad2.element.alternative.listener.AlternativesChangeEvent;
import sinbad2.element.alternative.listener.EAlternativesChange;
import sinbad2.element.alternative.listener.IAlternativesChangeListener;
import sinbad2.element.criterion.Criterion;
import sinbad2.element.criterion.listener.CriteriaChangeEvent;
import sinbad2.element.criterion.listener.ECriteriaChange;
import sinbad2.element.criterion.listener.ICriteriaChangeListener;
import sinbad2.element.expert.Expert;
import sinbad2.element.expert.listener.EExpertsChange;
import sinbad2.element.expert.listener.ExpertsChangeEvent;
import sinbad2.element.expert.listener.IExpertsChangeListener;
import sinbad2.resolutionphase.io.XMLRead;

public class ProblemElementsSet implements Cloneable {
	
	private List<Expert> _experts;
	private List<Alternative> _alternatives;
	private List<Criterion> _criteria;
	
	private List<IExpertsChangeListener> _expertsListener;
	private List<IAlternativesChangeListener> _alternativesListener;
	private List<ICriteriaChangeListener> _criteriaListener;

	
	public ProblemElementsSet(){
		_experts = new LinkedList<Expert>();
		_alternatives = new LinkedList<Alternative>();
		_criteria = new LinkedList<Criterion>();

		_expertsListener = new LinkedList<IExpertsChangeListener>();
		_alternativesListener = new LinkedList<IAlternativesChangeListener>();
		_criteriaListener = new LinkedList<ICriteriaChangeListener>();
	}

	public List<Expert> getExperts() {
		return _experts;
	}
	
	public List<Alternative> getAlternatives() {
		return _alternatives;
	}
	
	public List<Criterion> getCriteria() {
		return _criteria;
	}

	public void setExperts(List<Expert> experts) {
		Validator.notNull(experts);
		
		_experts = experts;
		
		notifyExpertsChanges(new ExpertsChangeEvent(EExpertsChange.EXPERTS_CHANGES, null, _experts, false));
	}
	
	public void setAlternatives(List<Alternative> alternatives) {
		Validator.notNull(alternatives);
		
		_alternatives = alternatives;
		
		notifyAlternativesChanges(new AlternativesChangeEvent(EAlternativesChange.ALTERNATIVES_CHANGES, null, _alternatives, false));
	}
	
	public void setCriteria(List<Criterion> criteria) {
		Validator.notNull(criteria);
		
		_criteria = criteria;
		
		notifyCriteriaChanges(new CriteriaChangeEvent(ECriteriaChange.CRITERIA_CHANGES, null, _criteria, false));
		
	}

	public void addExpert(Expert expert, boolean hasParent, boolean inUndoRedo) {
		
		if(!hasParent) {
			_experts.add(expert);
			Collections.sort(_experts);
		}
		
		_experts.add(expert);
		Collections.sort(_experts);
		
		notifyExpertsChanges(new ExpertsChangeEvent(EExpertsChange.ADD_EXPERT, null, expert, inUndoRedo));
		
	}
	
	public void addAlternative(Alternative alternative, boolean inUndoRedo) {
		
		_alternatives.add(alternative);
		Collections.sort(_alternatives);
		
		notifyAlternativesChanges(new AlternativesChangeEvent(EAlternativesChange.ADD_ALTERNATIVE, null, alternative, inUndoRedo));
		
	}
	
	public void addCriterion(Criterion criterion, boolean inUndoRedo) {
		
		_criteria.add(criterion);
		Collections.sort(_criteria);
		
		notifyCriteriaChanges(new CriteriaChangeEvent(ECriteriaChange.ADD_CRITERION, null, criterion, inUndoRedo));
		
	}
	
	public void moveExpert(Expert moveExpert, Expert newParent, Expert oldParent, boolean inUndoRedo) {
		
		if(oldParent == null) {
			_experts.remove(moveExpert);
			newParent.addChildren(moveExpert);
		} else {
			oldParent.removeChildren(moveExpert);
			if(newParent == null) {
				addExpert(moveExpert, false, inUndoRedo);
			} else {
				newParent.addChildren(moveExpert);
			}
		}
		
		notifyExpertsChanges(new ExpertsChangeEvent(EExpertsChange.MOVE_EXPERT, oldParent, moveExpert, inUndoRedo));
	}
	
	public void addMultipleExperts(List<Expert> insertExperts, boolean hasParent, boolean inUndoRedo) {
		Expert parent = insertExperts.get(0).getParent();
		
		for(Expert expert: insertExperts) {	
			if(!hasParent) {
				_experts.add(expert);
			} else {
				parent.addChildren(expert);
			}
			_experts.add(expert);
		}
		
		notifyExpertsChanges(new ExpertsChangeEvent(EExpertsChange.REMOVE_MULTIPLE_EXPERTS, null, insertExperts, inUndoRedo));
		
	}
	
	public void addMultipleAlternatives(List<Alternative> insertAlternatives, boolean inUndoRedo) {
		
		for(Alternative alternative: insertAlternatives) {	
			_alternatives.add(alternative);
		
		}
		
		Collections.sort(_alternatives);
		
		notifyAlternativesChanges(new AlternativesChangeEvent(EAlternativesChange.ADD_MULTIPLE_ALTERNATIVES, null, insertAlternatives, 
				inUndoRedo));
		
	}
	
	public void addMultipleCriteria(List<Criterion> insertCriteria, boolean inUndoRedo) {
		
		for(Criterion criterion: insertCriteria) {	
			_criteria.add(criterion);
		}
		
		
		notifyCriteriaChanges(new CriteriaChangeEvent(ECriteriaChange.ADD_CRITERIA, null, insertCriteria, inUndoRedo));
		
	}
	
	public void removeExpert(Expert expert, boolean hasParent, boolean inUndoRedo) {
		
		if(!hasParent) {
			_experts.remove(expert);
			Collections.sort(_experts);
		}
		
		_experts.remove(expert);
		Collections.sort(_experts);
		
		notifyExpertsChanges(new ExpertsChangeEvent(EExpertsChange.REMOVE_EXPERT, expert, null, inUndoRedo));
		
	}
	
	public void removeAlternative(Alternative alternative, boolean inUndoRedo) {
		
		_alternatives.remove(alternative);
		Collections.sort(_alternatives);
		
		notifyAlternativesChanges(new AlternativesChangeEvent(EAlternativesChange.REMOVE_ALTERNATIVE, alternative, null, inUndoRedo));
		
	}
	
	public void removeCriterion(Criterion criterion, boolean inUndoRedo) {
		
		_criteria.remove(criterion);
		Collections.sort(_criteria);
		
		notifyCriteriaChanges(new CriteriaChangeEvent(ECriteriaChange.REMOVE_CRITERION, criterion, null, inUndoRedo));
	}
	
	public void removeMultipleExperts(List<Expert> removeExperts, boolean hasParent, boolean inUndoRedo) {
		Expert parent = removeExperts.get(0).getParent();
		
		for(Expert expert: removeExperts) {	
			if(!hasParent) {
				_experts.remove(expert);
			} else {
				parent.removeChildren(expert);
				expert.setParent(parent);
			}
			_experts.remove(expert);
		}
		
		notifyExpertsChanges(new ExpertsChangeEvent(EExpertsChange.REMOVE_MULTIPLE_EXPERTS, removeExperts, null, inUndoRedo));
		
	}
	
	public void removeMultipleAlternatives(List<Alternative> removeAlternatives, boolean inUndoRedo) {
		
		for(Alternative alternative: removeAlternatives) {
			_alternatives.remove(alternative);
			
		}
		
		Collections.sort(_alternatives);
		
		notifyAlternativesChanges(new AlternativesChangeEvent(EAlternativesChange.REMOVE_MULTIPLE_ALTERNATIVES, removeAlternatives, null,
				inUndoRedo));	
	
	}
	
	public void removeMultipleCriteria(List<Criterion> removeCriteria, boolean inUndoRedo) {
		
		for(Criterion criterion: removeCriteria) {
			_criteria.remove(criterion);
		}
		
		notifyCriteriaChanges(new CriteriaChangeEvent(ECriteriaChange.REMOVE_CRITERIA, removeCriteria, null, inUndoRedo));
		
	}
	
	public void modifyExpert(Expert modifyExpert, String id, boolean inUndoRedo) {
		Expert oldExpert = (Expert) modifyExpert.clone();
		modifyExpert.setId(id);
		
		
		notifyExpertsChanges(new ExpertsChangeEvent(EExpertsChange.MODIFY_EXPERT, oldExpert, modifyExpert, inUndoRedo));
		
	}
	
	public void modifyAlternative(Alternative modifyAlternative, String id, boolean inUndoRedo) {
		Alternative oldAlternative = (Alternative) modifyAlternative.clone();
		modifyAlternative.setId(id);
		
		Collections.sort(_alternatives);
		
		notifyAlternativesChanges(new AlternativesChangeEvent(EAlternativesChange.MODIFY_ALTERNATIVE, oldAlternative, 
				modifyAlternative, inUndoRedo));
		
	}
	
	public void modifyCriterion(Criterion modifyCriterion, String id, boolean inUndoRedo) {
		Criterion oldCriterion = (Criterion) modifyCriterion.clone();
		modifyCriterion.setId(id);
		
		notifyCriteriaChanges(new CriteriaChangeEvent(ECriteriaChange.MODIFY_CRITERION, oldCriterion, modifyCriterion, inUndoRedo));
	}
	
	public void registerExpertsChangesListener(IExpertsChangeListener listener) {
		_expertsListener.add(listener);
	}
	
	public void unregisterExpertsChangeListener(IExpertsChangeListener listener) {
		_expertsListener.remove(listener);
	}
	
	public void registerAlternativesChangesListener(IAlternativesChangeListener listener) {
		_alternativesListener.add(listener);
	}
	
	public void unregisterAlternativesChangeListener(IAlternativesChangeListener listener) {
		_alternativesListener.remove(listener);
	}
	
	public void registerCriteriaChangesListener(ICriteriaChangeListener listener) {
		_criteriaListener.add(listener);
	}
	
	public void unregisterCriteriaChangeListener(ICriteriaChangeListener listener) {
		_criteriaListener.remove(listener);
	}
	
	public void notifyExpertsChanges(ExpertsChangeEvent event) {
		for(IExpertsChangeListener listener: _expertsListener) {
			listener.notifyExpertsChange(event);
		}
		
		Workspace.getWorkspace().updateHashCode();
	}
	
	public void notifyAlternativesChanges(AlternativesChangeEvent event) {
		for(IAlternativesChangeListener listener: _alternativesListener) {
			listener.notifyAlternativesChange(event);
		}
		
		Workspace.getWorkspace().updateHashCode();
	}
	
	public void notifyCriteriaChanges(CriteriaChangeEvent event) {
		for(ICriteriaChangeListener listener: _criteriaListener) {
			listener.notifyCriteriaChange(event);
		}
		
		Workspace.getWorkspace().updateHashCode();
	}
	
	public void clear() {
		if(!_experts.isEmpty()) {
			_experts.clear();
			notifyExpertsChanges(new ExpertsChangeEvent(EExpertsChange.EXPERTS_CHANGES, null, _experts, false));
		}
		
		if(!_alternatives.isEmpty()) {
			_alternatives.clear();
			notifyAlternativesChanges(new AlternativesChangeEvent(EAlternativesChange.ALTERNATIVES_CHANGES, null, _alternatives, false));
		}
		
		if(!_criteria.isEmpty()) {
			_criteria.clear();
			notifyCriteriaChanges(new CriteriaChangeEvent(ECriteriaChange.CRITERIA_CHANGES, null, _criteria, false));
		}
	}
	
	public void save(XMLStreamWriter writer) throws XMLStreamException {
		writer.writeStartElement("elements"); //$NON-NLS-1$

		writer.writeStartElement("experts"); //$NON-NLS-1$
		saveExperts(_experts, writer);
		writer.writeEndElement();

		writer.writeStartElement("alternatives"); //$NON-NLS-1$
		saveAlternatives(_alternatives, writer);
		writer.writeEndElement();

		writer.writeStartElement("criteria"); //$NON-NLS-1$
		saveCriteria(_criteria, writer);
		writer.writeEndElement();

		writer.writeEndElement();
	}

	public void read(XMLRead reader) throws XMLStreamException {
		reader.goToStartElement("elements"); //$NON-NLS-1$
		readExperts(reader);
		readAlternatives(reader);
		readCriteria(reader);
		reader.goToEndElement("elements"); //$NON-NLS-1$
	}

	public void readExperts(XMLRead reader) throws XMLStreamException {
		reader.goToStartElement("experts"); //$NON-NLS-1$

		XMLEvent event;
		String id;
		Expert expert = null;
		Expert parent = null;
		boolean end = false;
		while (reader.hasNext() && !end) {
			event = reader.next();

			if (event.isStartElement()) {
				if ("expert".equals(reader.getStartElementLocalPart())) { //$NON-NLS-1$
					id = reader.getStartElementAttribute("id"); //$NON-NLS-1$
					expert = new Expert(id);
					if (parent == null) {
						_experts.add(expert);
						parent = expert;
					} else {
						parent.addChildren(expert);
						parent = expert;
					}
					_experts.add(expert);
				}
			} else if (event.isEndElement()) {
				if ("expert".equals(reader.getEndElementLocalPart())) { //$NON-NLS-1$
					parent = expert.getParent();
					expert = parent;
				} else if ("experts".equals(reader.getEndElementLocalPart())) { //$NON-NLS-1$
					end = true;
					Collections.sort(_experts);
				}
			}
		}
	}

	public void readAlternatives(XMLRead reader) throws XMLStreamException {
		reader.goToStartElement("alternatives"); //$NON-NLS-1$

		XMLEvent event;
		boolean end = false;
		while (reader.hasNext() && !end) {
			event = reader.next();

			if (event.isStartElement()) {
				if ("alternative".equals(reader.getStartElementLocalPart())) { //$NON-NLS-1$
					_alternatives.add(new Alternative(reader
							.getStartElementAttribute("id"))); //$NON-NLS-1$
				}
			} else if (event.isEndElement()) {
				if ("alternatives".equals(reader.getEndElementLocalPart())) { //$NON-NLS-1$
					end = true;
					Collections.sort(_alternatives);
				}
			}
		}
	}

	public void readCriteria(XMLRead reader) throws XMLStreamException {
		reader.goToStartElement("criteria"); //$NON-NLS-1$

		XMLEvent event;
		String id;
		Criterion criterion = null;
		boolean end = false;
		while (reader.hasNext() && !end) {
			event = reader.next();

			if (event.isStartElement()) {
				if ("criterion".equals(reader.getStartElementLocalPart())) { //$NON-NLS-1$
					id = reader.getStartElementAttribute("id"); //$NON-NLS-1$
					criterion = new Criterion(id);
					_criteria.add(criterion);
				}
			} else if (event.isEndElement()) {
				if ("criteria".equals(reader.getEndElementLocalPart())) { //$NON-NLS-1$
					end = true;
					Collections.sort(_criteria);
				}
			}
		}
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder(17, 31);
		for (Expert expert : _experts) {
			hcb.append(expert);
		}
		for (Alternative alternative : _alternatives) {
			hcb.append(alternative);
		}
		for (Criterion criterion : _criteria) {
			hcb.append(criterion);
		}
		return hcb.toHashCode();
	}

	
	@Override
	public Object clone() throws CloneNotSupportedException {
		
		ProblemElementsSet result = null;
		
		result = (ProblemElementsSet) super.clone();
		
		result._experts = new LinkedList<Expert>();
		for(Expert expert: _experts){
			result._experts.add((Expert) expert.clone());
		}
		
		result._alternatives = new LinkedList<Alternative>();
		for(Alternative alternative: _alternatives){
			result._alternatives.add((Alternative) alternative.clone());
		}
		
		result._criteria = new LinkedList<Criterion>();
		for(Criterion criterion: _criteria){
			result._criteria.add((Criterion) criterion.clone());
		}
		
		result._expertsListener = new LinkedList<IExpertsChangeListener>();
		for(IExpertsChangeListener listener: _expertsListener) {
			result._expertsListener.add(listener);
		}
		
		result._alternativesListener = new LinkedList<IAlternativesChangeListener>();
		for(IAlternativesChangeListener listener: _alternativesListener) {
			result._alternativesListener.add(listener);
		}
		
		result._criteriaListener = new LinkedList<ICriteriaChangeListener>();
		for(ICriteriaChangeListener listener: _criteriaListener) {
			result._criteriaListener.add(listener);
		}
		
		return result;
		
	}
	
	private void saveExperts(List<Expert> experts, XMLStreamWriter writer)
			throws XMLStreamException {
		for (Expert expert : experts) {
			writer.writeStartElement("expert"); //$NON-NLS-1$
			writer.writeAttribute("id", expert.getId()); //$NON-NLS-1$
			if (expert.hasChildrens()) {
				saveExperts(expert.getChildrens(), writer);
			}
			writer.writeEndElement();
		}
	}

	private void saveAlternatives(List<Alternative> alternatives,
			XMLStreamWriter writer) throws XMLStreamException {
		for (Alternative alternative : alternatives) {
			writer.writeStartElement("alternative"); //$NON-NLS-1$
			writer.writeAttribute("id", alternative.getId()); //$NON-NLS-1$
			writer.writeEndElement();
		}
	}

	private void saveCriteria(List<Criterion> criteria, XMLStreamWriter writer)
			throws XMLStreamException {
		for (Criterion criterion : criteria) {
			writer.writeStartElement("criterion"); //$NON-NLS-1$
			writer.writeAttribute("id", criterion.getId()); //$NON-NLS-1$
			writer.writeEndElement();
		}
	}
	
}