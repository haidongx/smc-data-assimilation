package edu.gsu.hxue.smc.specialParticleSystems;

import edu.gsu.hxue.smc.AbstractParticleSystem;
import edu.gsu.hxue.smc.Particle;
import edu.gsu.hxue.smc.resamplingStrategies.SystematicResampling;
import edu.gsu.hxue.smc.samplingStrategies.PriorSampling;
import edu.gsu.hxue.smc.weightUpdatingStrategies.LikelihoodWeight;

import java.util.Vector;


/**
 * The SMC system using the transition prior as the proposal, the likelihood as incremental weight.
 *
 * @author Haidong
 */
public class BootstrapFilter extends AbstractParticleSystem {
    public BootstrapFilter(Vector<Particle> particleSet) {
        super(new PriorSampling(), new LikelihoodWeight(), new SystematicResampling(), particleSet);
    }

}
