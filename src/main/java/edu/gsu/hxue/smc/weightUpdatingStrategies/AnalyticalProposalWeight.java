package edu.gsu.hxue.smc.weightUpdatingStrategies;

import edu.gsu.hxue.smc.AbstractState.AbstractMeasurement;
import edu.gsu.hxue.smc.AbstractState.StateFunctionNotSupportedException;
import edu.gsu.hxue.smc.Particle;
import edu.gsu.hxue.smc.SamplingStrategy;
import edu.gsu.hxue.smc.WeightUpdatingStrategy;

import java.util.Vector;

public class AnalyticalProposalWeight extends WeightUpdatingStrategy {

    @Override
    protected void updateUnnormalizedWeights(Vector<Particle> particleSet, AbstractMeasurement measurement, SamplingStrategy sampler) {
        try {
            for (Particle p : particleSet)
                p.weight = p.weight.multiply(p.state.proposalPdf(measurement));
        } catch (StateFunctionNotSupportedException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

}
