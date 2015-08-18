package edu.gsu.hxue.smc;

public abstract class SamplingStrategy {
    public abstract AbstractState sampling(AbstractState currentState, AbstractState.AbstractMeasurement measurement);
}
