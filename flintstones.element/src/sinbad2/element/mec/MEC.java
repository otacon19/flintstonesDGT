package sinbad2.element.mec;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.graphics.Image;

import sinbad2.element.alternative.Alternative;
import sinbad2.element.criterion.Criterion;

public class MEC {
	
	private String _id;
	private List<Alternative> _contexts;
	private List<Criterion> _indexes;
	private Image _formula;
	
	public MEC() {
		_id = null;
		_contexts = new LinkedList<Alternative>();
		_indexes = new LinkedList<Criterion>();
		_formula = null;
	}
	
	public void setId(String id) {
		_id = id;
	}
	
	public String getId() {
		return _id;
	}
	
	public void setContexts(List<Alternative> contexts) {
		_contexts = contexts;
	}
	
	public List<Alternative> getContexts() {
		return _contexts;
	}
	
	public void setIndexes(List<Criterion> indexes) {
		_indexes = indexes;
	}
	
	public List<Criterion> getIndexes() {
		return _indexes;
	}
	
	public void setFormula(Image formula) {
		_formula = formula;
	}
	
	public Image getFormula() {
		return _formula;
	}
}
