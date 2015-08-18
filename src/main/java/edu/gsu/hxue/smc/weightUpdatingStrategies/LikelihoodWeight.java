package edu.gsu.hxue.smc.weightUpdatingStrategies;

import edu.gsu.hxue.smc.AbstractState.AbstractMeasurement;
import edu.gsu.hxue.smc.AbstractState.StateFunctionNotSupportedException;
import edu.gsu.hxue.smc.Particle;
import edu.gsu.hxue.smc.SamplingStrategy;
import edu.gsu.hxue.smc.WeightUpdatingStrategy;

import java.util.Vector;

public class LikelihoodWeight extends WeightUpdatingStrategy {

    @Override
    protected void updateUnnormalizedWeights(Vector<Particle> particleSet, AbstractMeasurement measurement, SamplingStrategy sampler) {
        try {
            int i = 0;
            for (Particle p : particleSet) {
                p.weight = p.weight.multiply(p.state.measurementPdf(measurement));
                System.out.printf("Weight-%d: %5e%n", i++, p.weight);
            }
        } catch (StateFunctionNotSupportedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
