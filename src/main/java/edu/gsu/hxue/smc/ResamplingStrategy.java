package edu.gsu.hxue.smc;

import java.util.Vector;

public abstract class ResamplingStrategy {
    public abstract Vector<Particle> resampling(Vector<Particle> particleSet);
}
