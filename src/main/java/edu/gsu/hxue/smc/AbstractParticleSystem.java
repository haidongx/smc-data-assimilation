package edu.gsu.hxue.smc;

import java.math.BigDecimal;
import java.util.Vector;

public abstract class AbstractParticleSystem {
    private Vector<Particle> particleSet;

    private SamplingStrategy sampler;
    private WeightUpdatingStrategy weightUpdater;
    private ResamplingStrategy resampler;

    private Particle bestParticleBeforeResampling = null;

    public AbstractParticleSystem(SamplingStrategy sampler, WeightUpdatingStrategy weightUpdater, ResamplingStrategy resampler, Vector<Particle> particleSet) {
        this.sampler = sampler;
        this.weightUpdater = weightUpdater;
        this.resampler = resampler;
        this.particleSet = particleSet;
    }

    public void updateParticle(AbstractState.AbstractMeasurement measurement) {
        //Sampling
        int i = 0;
        System.out.print("Sampling: ");
        for (Particle p : particleSet) {
            System.out.print(i++ + " ");
            AbstractState temp = p.state;
            p.state = sampler.sampling(p.state, measurement);
            p.state.previousState = temp;
            p.state.previousState.previousState = null;  // very important, or it forms a linked list, and consume memory fast
        }
        System.out.println();

        //WeightUpdating
        weightUpdater.updateWeights(particleSet, measurement, sampler);

        //Record the best sample
        bestParticleBeforeResampling = this.getHighestWeightParticle();

        //Re-sampling
        particleSet = resampler.resampling(particleSet);
    }

    public Particle getBestParticleBeforeResampling() {
        return bestParticleBeforeResampling;
    }

    /**
     * @return the particle with the highest weight. Note: it is meanlingless after a resampling
     */
    public Particle getHighestWeightParticle() {
        Particle maxP = null;
        BigDecimal max = BigDecimal.ZERO;
        for (Particle p : particleSet) {
            if (max.compareTo(p.weight) < 0) {
                maxP = p;
                max = p.weight;
            }
        }
        return maxP;
    }

    public Vector<Particle> getParticleSet() {
        return this.particleSet;
    }

    public void setDescription(String des) {
        int c = 0;
        for (Particle p : this.particleSet) {
            p.state.setDescription(des + "_Particle" + c);
            c++;
        }

    }
}
