package sinbad2.domain.listener;

import sinbad2.domain.DomainSet;

public interface IDomainSetChangeListener {
	
	public void notifyNewActiveDomainSet(DomainSet domainSet);
	
}
