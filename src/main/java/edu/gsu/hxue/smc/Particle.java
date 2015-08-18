package edu.gsu.hxue.smc;

import java.math.BigDecimal;

public class Particle {
    /**
     * The system state
     */
    public AbstractState state;
    /**
     * The importance weight
     */
    public BigDecimal weight;

    public Particle(AbstractState state, BigDecimal weight) {
        this.state = state;
        this.weight = weight;
    }

}
