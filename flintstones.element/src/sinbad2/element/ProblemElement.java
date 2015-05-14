package sinbad2.element;

/**
 * ProblemElement.java
 * 
 * Clase que representa un elemento del problema.
 * 
 * @author Labella Romero, �lvaro
 * @version 1.0
 *
 */
public abstract class ProblemElement implements Cloneable, Comparable<ProblemElement> {
	
	protected String _id;
	
	/**
	 * Constructor clase ProblemElement
	 */
	public ProblemElement() {
		_id = null;
	}
	
	/**
	 * Constructor clase ProblemElement
	 * @param id: id del elemento del problema
	 */
	public ProblemElement(String id){
		_id = id;
	}
	
	/**
	 * M�todo get del atributo id de la clase ProblemElement
	 * @return devuelve el id del elemento del problema
	 */
	public String getId() {
		return _id;
	}
	
	/**
	 * M�todo abstracto para mostrar la jerarq�a de los expertos a medida que se van a�adiendo: ExpertoY es hijo de ExpertoX...
	 * @return
	 */
	public abstract String getFormatId();
	
	@Override
	public String toString() {
		return getFormatId();
	}
	
	/**
	 * M�todo set del atributo id de la clase ProblemElement
	 * @param id: id del elemento del problema
	 */
	public void setId(String id) {
		_id = id;
	}

	@Override
	public int compareTo(ProblemElement other) {
		// TODO validator
		return getFormatId().compareTo(other.getFormatId());
	}
	
	/**
	 * Funci�n que se encarga de la clonaci�n del objeto, asignando el mismo id que el de su copia.
	 */
	@Override
	public Object clone() {
		ProblemElement result = null;
		
		try {
			result = (ProblemElement) super.clone();
		} catch (CloneNotSupportedException e) {
			//No ocurre nunca
		}
		
		result.setId(new String(_id));
		
		return result;
		
	}

	

}
