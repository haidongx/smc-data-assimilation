package edu.gsu.hxue.identicalTwinExperiments;

import edu.gsu.hxue.smc.AbstractParticleSystem;
import edu.gsu.hxue.smc.AbstractState;
import edu.gsu.hxue.smc.Particle;

import java.math.BigDecimal;
import java.util.Vector;

public abstract class AbstractIdenticalTwinExperiment {
    protected AbstractState realSystem; // a single simulation with real setting
    protected AbstractState simulatedSystem; // a single simulation with wrong setting
    protected AbstractParticleSystem particleSystem; // a particle set of simulations

    protected abstract AbstractState createRealSystem();

    protected abstract AbstractState createSimulatedSystem();

    protected abstract AbstractParticleSystem createParticleSystem(Vector<Particle> particleSet);

    public void runDataAssimilationExperiement(int stepNumber, int particleNumber) throws Exception {
        // Create the real system from its factory method
        this.realSystem = this.createRealSystem();
        realSystem.setDescription("t0_Real");

        System.out.println("SMC --------------  Real system created!!! ");
        //System.out.println("Memory usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000.0 + "MB");

        // Create the simulated system from its factory method
        this.simulatedSystem = this.createSimulatedSystem();
        simulatedSystem.setDescription("t0_Sim");

        System.out.println("Simulated --------------  Real system created!!! ");
        //System.out.println("Memory usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000.0 + "MB");

        // Create the initial particle set from the simulated system factory method
        Vector<Particle> initialParticleSet = new Vector<Particle>();
        for (int i = 0; i < particleNumber; i++) {
            AbstractState s = null;
            try {
                s = (AbstractState) simulatedSystem.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                System.exit(1);
            }
            s.setDescription("t0_" + "Particle" + i);
            initialParticleSet.add(new Particle(s, BigDecimal.valueOf(1.0 / particleNumber)));

            System.out.println("SMC -------------- Particle" + i + " created!!! ");
            System.out.println("Memory usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000.0 + "MB");
        }

        // Create the particle system from its factory method
        this.particleSystem = this.createParticleSystem(initialParticleSet);

        for (int t = 1; t <= stepNumber; t++) {
            System.out.println("SMC -------------- Step" + t + " started !!!!!!!!!!!! ");
            //System.out.println("Memory usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000.0 + "MB");
            try {
                // The real system at time t
                realSystem = realSystem.transitionFunction();
                realSystem.setDescription("t" + t + "_" + "Real");

                System.out.println("SMC -------------- real sys finished");
                //System.gc();
                //System.out.println("Memory usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000.0 + "MB");

                //The simulated system at time t
                simulatedSystem = simulatedSystem.transitionFunction();
                simulatedSystem.setDescription("t" + t + "_" + "Sim");

                System.out.println("SMC -------------- sim sys finished");
                //System.gc();
                //System.out.println("Memory usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000.0 + "MB");

                // Measurement from the real system
                AbstractState.AbstractMeasurement measurement = realSystem.measurementFunction();
                System.out.println("SMC -------------- measurement finished");
                //System.out.println("Memory usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000.0 + "MB");

                // Assimilate data to the particle system
                particleSystem.updateParticle(measurement);
                particleSystem.setDescription("t" + t); // add a description for the state for each particle
                System.out.println("SMC -------------- particles finished");
                System.out.println("Memory usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000.0 + "MB");


                // Report experiment results
                reportOnStep(t);
            } catch (AbstractState.StateFunctionNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract void reportOnStep(int step) throws Exception;

}
