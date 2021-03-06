package sinbad2.aggregationoperator.arithmetic;

import java.util.List;

import sinbad2.aggregationoperator.UnweightedAggregationOperator;
import sinbad2.aggregationoperator.arithmetic.valuation.RealOperator;
import sinbad2.core.validator.Validator;
import sinbad2.valuation.Valuation;
import sinbad2.valuation.real.RealValuation;

public class Arithmetic extends UnweightedAggregationOperator {

	@Override
	public Valuation aggregate(List<Valuation> valuations) {
		Validator.notNull(valuations);
		
		//TODO TwoTuples
		if(valuations.size() > 0) {
			for(Valuation valuation: valuations) {
				if(valuation instanceof RealValuation) {
					RealOperator.aggregate(valuations);
				} else {
					throw new IllegalArgumentException("Not supported type");
				}
			}
		}
		
		return null;
	}

}
