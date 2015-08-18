package edu.gsu.hxue.smc.weightUpdatingStrategies;

import edu.gsu.hxue.smc.*;
import edu.gsu.hxue.smc.AbstractState.AbstractMeasurement;
import edu.gsu.hxue.smc.AbstractState.StateFunctionNotSupportedException;
import edu.gsu.hxue.smc.samplingStrategies.PriorSampling;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.math.BigDecimal;
import java.util.Vector;

public class KernelEstimationProposalWeight extends WeightUpdatingStrategy {
    private int sampleNumberForEstimation;
    private KernelFunction kernelFunction;
    private BigDecimal bandWidth;

    public KernelEstimationProposalWeight(int sampleNumberForEstimation, KernelFunction kernelFunction, BigDecimal bandWidth) {
        this.sampleNumberForEstimation = sampleNumberForEstimation;
        this.kernelFunction = kernelFunction;
        this.bandWidth = bandWidth;
    }

    @Override
    protected void updateUnnormalizedWeights(Vector<Particle> particleSet, AbstractMeasurement measurement, SamplingStrategy proposalSampler) {
        try {
            for (Particle p : particleSet) {
                // Likelihood
                BigDecimal likelihood = p.state.measurementPdf(measurement);

                System.out.printf("Likelihood: %5e  ", likelihood);

                // Draw M samples for estimating prior density
                PriorSampling priorSampler = new PriorSampling();
                BigDecimal priorDensity = BigDecimal.ZERO;
                for (int i = 0; i < sampleNumberForEstimation; i++) {
                    AbstractState auxPriorSample = priorSampler.sampling(p.state.previousState, measurement);
                    //System.out.println("\ndis: " + p.state.distantce(auxPriorSample));
                    //System.out.println("kernel value: " + kernelFunction.kernel(p.state, auxPriorSample, this.bandWidth));
                    priorDensity = priorDensity.add(this.kernelFunction.kernel(p.state, auxPriorSample, this.bandWidth));
                }
                if (sampleNumberForEstimation > 0)
                    priorDensity = priorDensity.divide(bandWidth.multiply(BigDecimal.valueOf(sampleNumberForEstimation)), GlobalConstants.BIG_DECIMAL_MATHCONTEXT);
                System.out.printf("priorDensity: %5e  ", priorDensity);


                // Draw M samples for estimating proposal density
                BigDecimal proposalDensity = BigDecimal.ZERO;
                for (int i = 0; i < sampleNumberForEstimation; i++) {
                    AbstractState auxProposalSample = proposalSampler.sampling(p.state.previousState, measurement);
                    //System.out.println("\ndis: " + p.state.distantce(auxProposalSample));
                    //System.out.println("kernel value: " + kernelFunction.kernel(p.state, auxProposalSample, this.bandWidth));
                    proposalDensity = proposalDensity.add(this.kernelFunction.kernel(p.state, auxProposalSample, this.bandWidth));
                }
                if (sampleNumberForEstimation > 0)
                    proposalDensity = proposalDensity.divide(bandWidth.multiply(BigDecimal.valueOf(sampleNumberForEstimation)), GlobalConstants.BIG_DECIMAL_MATHCONTEXT);
                System.out.printf("proposalDensity: %5e    ", proposalDensity);

                // Set new unnormalized weight
                p.weight = p.weight.multiply(likelihood);

                if (sampleNumberForEstimation > 0) {
                    p.weight = p.weight.multiply(priorDensity);
                    p.weight = p.weight.divide(proposalDensity, GlobalConstants.BIG_DECIMAL_MATHCONTEXT);
                }

                System.out.printf("Weight: %5e%n", p.weight);
            }
        } catch (StateFunctionNotSupportedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static abstract class KernelFunction {
        public abstract BigDecimal kernel(AbstractState state, AbstractState sample, BigDecimal bandWidth);
    }

    public static class GaussianKernel extends KernelFunction {
        @Override
        public BigDecimal kernel(AbstractState state, AbstractState sample, BigDecimal bandWidth) {
            BigDecimal dis = BigDecimal.valueOf(state.distance(sample));
            //System.out.println("dis: " + dis);
            double sigma = 1;
            NormalDistribution norm = new NormalDistribution(0, sigma);
            BigDecimal x = dis.divide(bandWidth, GlobalConstants.BIG_DECIMAL_MATHCONTEXT);
            //System.out.println("before norm: " + x);
            //System.out.println("After norm: " + norm.density(x.doubleValue()));

            return BigDecimal.valueOf(norm.density(x.doubleValue()));
        }
    }

}
