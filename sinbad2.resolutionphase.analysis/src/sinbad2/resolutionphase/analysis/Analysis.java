package sinbad2.resolutionphase.analysis;

import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.builder.HashCodeBuilder;

import sinbad2.core.workspace.WorkspaceContentPersistenceException;
import sinbad2.element.ProblemElementsSet;
import sinbad2.resolutionphase.IResolutionPhase;
import sinbad2.resolutionphase.io.XMLRead;
import sinbad2.resolutionphase.io.XMLWriter;
import sinbad2.resolutionphase.state.EResolutionPhaseStateChange;
import sinbad2.resolutionphase.state.ResolutionPhaseStateChangeEvent;

public class Analysis implements IResolutionPhase {
	
	private ProblemElementsSet _elementSet;
	
	public Analysis() {
		_elementSet = new ProblemElementsSet();
	}
	
	public ProblemElementsSet getElementSet() {
		return _elementSet;
	}
	
	@Override
	public IResolutionPhase copyStructure() {
		return new Analysis();
	}
	
	@Override
	public void copyData(IResolutionPhase iResolutionPhase) {
		Analysis analysis = (Analysis) iResolutionPhase;

		clear();
		_elementSet.setExperts(analysis.getElementSet().getExperts());
		_elementSet.setAlternatives(analysis.getElementSet().getAlternatives());
		_elementSet.setCriteria(analysis.getElementSet().getCriteria());	
	}
	
	@Override
	public void save(XMLWriter writer)
			throws WorkspaceContentPersistenceException {
		XMLStreamWriter streamWriter = writer.getStreamWriter();
		try {
			_elementSet.save(streamWriter);
		} catch (XMLStreamException e) {
			throw new WorkspaceContentPersistenceException();
		}
	}
	
	@Override
	public void read(XMLRead reader, Map<String, IResolutionPhase> buffer)
			throws WorkspaceContentPersistenceException {
		try {
			_elementSet.read(reader);
		} catch (XMLStreamException e) {
			throw new WorkspaceContentPersistenceException();
		}
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder(17, 31);
		hcb.append(_elementSet);
		return hcb.toHashCode();
	}
	
	@Override
	public IResolutionPhase clone() {
		Analysis result = null;

		try {
			result = (Analysis) super.clone();
			result._elementSet = (ProblemElementsSet) _elementSet.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public void notifyResolutionPhaseStateChange(
			ResolutionPhaseStateChangeEvent event) {
		if (event.getChange().equals(EResolutionPhaseStateChange.ACTIVATED)) {
			activate();
		}
	}
	
	@Override
	public void clear() {
		_elementSet.clear();
		
	}

	@Override
	public void activate() {}

	@Override
	public boolean validate() {
		if (_elementSet.getAlternatives().isEmpty()) {
			return false;
		}

		if (_elementSet.getExperts().isEmpty()) {
			return false;
		}

		if (_elementSet.getCriteria().isEmpty()) {
			return false;
		}
		
		return true;
	}

}
