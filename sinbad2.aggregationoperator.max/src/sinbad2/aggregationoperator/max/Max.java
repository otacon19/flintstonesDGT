package sinbad2.aggregationoperator.max;

import java.util.List;

import sinbad2.aggregationoperator.UnweightedAggregationOperator;
import sinbad2.aggregationoperator.max.valuation.RealOperator;
import sinbad2.aggregationoperator.max.valuation.IntervalRealOperator;
import sinbad2.core.validator.Validator;
import sinbad2.valuation.Valuation;
import sinbad2.valuation.real.RealValuation;
import sinbad2.valuation.real.interval.RealInterval;

public class Max extends UnweightedAggregationOperator {

	@Override
	public Valuation aggregate(List<Valuation> valuations) {
		Validator.notNull(valuations);
		
		//TODO TwoTuples
		if(valuations.size() > 0) {
			for(Valuation valuation: valuations) {
				if(valuation instanceof RealValuation) {
					RealOperator.aggregate(valuations);
				} else if(valuation instanceof RealInterval) {
					IntervalRealOperator.aggregate(valuations);
				} else {
					throw new IllegalArgumentException("Not supported type");
				}
			}
		}
		
		return null;
			
	}

}
