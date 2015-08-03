package sinbad2.element.test.criterion;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import sinbad2.element.criterion.Criterion;

public class CriterionTest {
	
	private Criterion _tester;
	private Criterion _criterion;

	@Before
	public void testCriterionBefore() {
		_tester = new Criterion("Subcriterion"); //$NON-NLS-1$
		_criterion = new Criterion("Criterion"); //$NON-NLS-1$
	}

	@Test
	public void testClone() {
		Criterion clone = (Criterion) _tester.clone();
		
		assertThat(_tester.getId() == clone.getId(), is(true));
	}

	@Test
	public void testEqualsObject() {
		assertTrue("return true if there are the same object", _tester.equals(_tester)); //$NON-NLS-1$
		assertFalse("return false if there are the same object", _tester.equals(_criterion)); //$NON-NLS-1$
	}

}
