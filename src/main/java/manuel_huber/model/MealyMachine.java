package manuel_huber.model;

import manuel_huber.InputStrategy.InputStrategy;
import manuel_huber.OutputStrategy.OutputStrategy;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * It's a mealy machine!
 */
public class MealyMachine {

    public MealyMachine() {
    }

    public MealyMachine(List<State> states, List<Symbol> inputAlphabet, List<Symbol> outputAlphabet, Map<State, Map<Symbol, State>> transformationTable, Map<State, Map<Symbol, Symbol>> outputTable) {
        this.states = states;
        this.inputAlphabet = inputAlphabet;
        this.outputAlphabet = outputAlphabet;
        this.transformationTable = transformationTable;
        this.outputTable = outputTable;
    }

    // Definition
    private List<State> states;
    private List<Symbol> inputAlphabet;
    private List<Symbol> outputAlphabet;
    private Map<State, Map<Symbol, State>> transformationTable;
    private Map<State, Map<Symbol, Symbol>> outputTable;

    // Runtime stuff
    private State state;
    private OutputStrategy outputStrategy;
    private InputStrategy inputStrategy;

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public List<Symbol> getInputAlphabet() {
        return inputAlphabet;
    }

    public void setInputAlphabet(List<Symbol> inputAlphabet) {
        this.inputAlphabet = inputAlphabet;
    }

    public List<Symbol> getOutputAlphabet() {
        return outputAlphabet;
    }

    public void setOutputAlphabet(List<Symbol> outputAlphabet) {
        this.outputAlphabet = outputAlphabet;
    }

    public Map<State, Map<Symbol, State>> getTransformationTable() {
        return transformationTable;
    }

    public void setTransformationTable(Map<State, Map<Symbol, State>> transformationTable) {
        this.transformationTable = transformationTable;
    }

    public Map<State, Map<Symbol, Symbol>> getOutputTable() {
        return outputTable;
    }

    public void setOutputTable(Map<State, Map<Symbol, Symbol>> outputTable) {
        this.outputTable = outputTable;
    }

    public OutputStrategy getOutputStrategy() {
        return outputStrategy;
    }

    public MealyMachine setOutputStrategy(OutputStrategy outputStrategy) {
        this.outputStrategy = outputStrategy;
        return this;
    }

    public InputStrategy getInputStrategy() {
        return inputStrategy;
    }

    public MealyMachine setInputStrategy(InputStrategy inputStrategy) {
        this.inputStrategy = inputStrategy;
        return this;
    }

    public void start() {
        if (inputStrategy == null) {
            System.out.println("No input strategy");
            return;
        }
        if (outputStrategy == null) {
            System.out.println("No output strategy");
            return;
        }
        state = states.get(0);

        Iterator<Symbol> input = inputStrategy.putIn(inputAlphabet);
        while (input.hasNext()) {
            Symbol inputSymbol = input.next();
            if (inputSymbol == null) System.out.println("Done");
            outputStrategy.putOut(this.getOutputTable().get(state).get(inputSymbol));
            state = this.transformationTable.get(state).get(inputSymbol);
        }
    }

}
