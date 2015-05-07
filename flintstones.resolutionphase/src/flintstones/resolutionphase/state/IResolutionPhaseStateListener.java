package flintstones.resolutionphase.state;

/**
 * IResolutionPhaseListener.java
 * 
 * Observador que escucha los cambios producidos en la fase
 * 
 * @author Labella Romero, �lvaro
 * @version 1.0
 *
 */
public interface IResolutionPhaseStateListener {
	
	public void resolutionPhaseStateChange(ResolutionPhaseStateChangeEvent event);

}
