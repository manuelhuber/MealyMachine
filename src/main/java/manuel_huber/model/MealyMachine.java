package manuel_huber.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

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

    public void start() {
        state = states.get(0);
        while (true) {
            tick();
        }
    }

    private void tick() {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        Optional<Symbol> inputSymbol = inputAlphabet.stream().filter(symbol -> symbol.getSymbol().equals(input)).findFirst();
        if (!inputSymbol.isPresent()) {
            System.out.println("Unknown input. Please use one of the following strings:");
            System.out.println(inputAlphabet.stream().map(Symbol::getSymbol).collect(Collectors.joining(", ")));
            return;
        }
        System.out.println("Output:");
        System.out.println(this.getOutputTable().get(state).get(inputSymbol.get()).getSymbol());
        state = this.transformationTable.get(state).get(inputSymbol.get());
        System.out.println("New state");
        System.out.println(state.getName());
    }
}
