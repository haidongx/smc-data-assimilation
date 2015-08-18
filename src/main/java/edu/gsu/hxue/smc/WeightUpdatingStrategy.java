package edu.gsu.hxue.smc;

import java.math.BigDecimal;
import java.util.Vector;

public abstract class WeightUpdatingStrategy {
    /**
     * Update the weights for a particle set according to the current measurement.
     * Note: not all the weight updating strategies need the sampling strategy. When it happens, they ignore the samplingStrategy.
     *
     * @param particleSet {s_i, w_i} at t
     * @param measurement m_t
     * @param sampler     the sampling strategy that used to obtain the current particle sets
     */
    public void updateWeights(Vector<Particle> particleSet, AbstractState.AbstractMeasurement measurement, SamplingStrategy sampler) {
        updateUnnormalizedWeights(particleSet, measurement, sampler);
        normalizeWeights(particleSet);
    }

    protected abstract void updateUnnormalizedWeights(Vector<Particle> particleSet, AbstractState.AbstractMeasurement measurement, SamplingStrategy samplingStrategy);

    private void normalizeWeights(Vector<Particle> particleSet) {
        BigDecimal totalWeight = BigDecimal.ZERO;

        for (Particle p : particleSet)
            totalWeight = p.weight.add(totalWeight);

        if (totalWeight.compareTo(BigDecimal.ZERO) == 0)
            for (Particle p : particleSet)
                p.weight = BigDecimal.ONE.divide(BigDecimal.valueOf(particleSet.size()), GlobalConstants.BIG_DECIMAL_MATHCONTEXT);
        else
            for (Particle p : particleSet)
                p.weight = p.weight.divide(totalWeight, GlobalConstants.BIG_DECIMAL_MATHCONTEXT);
    }

}
