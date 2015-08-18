package edu.gsu.hxue.smc.samplingStrategies;

import edu.gsu.hxue.smc.AbstractState;
import edu.gsu.hxue.smc.AbstractState.AbstractMeasurement;
import edu.gsu.hxue.smc.AbstractState.StateFunctionNotSupportedException;
import edu.gsu.hxue.smc.SamplingStrategy;

public class ProposalSampling extends SamplingStrategy {
    @Override
    public AbstractState sampling(AbstractState currentState, AbstractMeasurement measurement) {
        try {
            return currentState.propose(measurement);
        } catch (StateFunctionNotSupportedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
