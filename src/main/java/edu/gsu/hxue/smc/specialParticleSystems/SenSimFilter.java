package edu.gsu.hxue.smc.specialParticleSystems;

import edu.gsu.hxue.smc.AbstractParticleSystem;
import edu.gsu.hxue.smc.Particle;
import edu.gsu.hxue.smc.resamplingStrategies.SystematicResampling;
import edu.gsu.hxue.smc.samplingStrategies.ProposalSampling;
import edu.gsu.hxue.smc.weightUpdatingStrategies.KernelEstimationProposalWeight;
import edu.gsu.hxue.smc.weightUpdatingStrategies.KernelEstimationProposalWeight.KernelFunction;

import java.math.BigDecimal;
import java.util.Vector;

/**
 * The SMC system using SenSim proposal (as proposed in Haidong's PhD dissertation), KernelEstimation weight updating.
 *
 * @author Haidong
 */
public class SenSimFilter extends AbstractParticleSystem {
    public SenSimFilter(Vector<Particle> particleSet, int particleNumberForEstimation, KernelFunction kernelFunction, BigDecimal bandWidth) {
        super(new ProposalSampling(), new KernelEstimationProposalWeight(particleNumberForEstimation, kernelFunction, bandWidth), new SystematicResampling(), particleSet);
        //super(new ProposalSampling(), new LikelihoodWeight(), new SystematicResampling(), particleSet);
    }

}
