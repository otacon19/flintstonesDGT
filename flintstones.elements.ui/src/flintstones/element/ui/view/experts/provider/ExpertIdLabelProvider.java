package flintstones.element.ui.view.experts.provider;


import org.eclipse.jface.viewers.ColumnLabelProvider;

import flintstones.element.expert.Expert;
import flintstones.element.ui.Images;

import org.eclipse.swt.graphics.Image;

/**
 * ColumnLabelProvider.java
 * 
 * Clase que se encarga de extraer el valor del objeto del �rbol para mostrarlo de forma correcta
 * 
 * @author Labella Romero, �lvaro
 * @version 1.0
 *
 */
public class ExpertIdLabelProvider extends ColumnLabelProvider {
	
	/**
	 * M�todo que extrae la cadena que mostrar� el objeto en la visualizaci�n
	 */
	@Override
	public String getText(Object obj){
		return ((Expert) obj).getId();
	}
	
	/**
	 * M�todo que obtiene la imagen que va a mostrar el objeto en la visualizaci�n
	 */
	@Override
	public Image getImage(Object obj){
		if(((Expert) obj).hasMembers()) {
			return Images.GroupOfExperts;
		} else {
			return Images.Expert;
		}
	}

}
