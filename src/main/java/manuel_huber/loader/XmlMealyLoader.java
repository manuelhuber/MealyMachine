package manuel_huber.loader;

import com.thoughtworks.xstream.XStream;
import manuel_huber.model.MealyMachine;
import manuel_huber.model.State;
import manuel_huber.model.Symbol;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlMealyLoader {

    private static final Path BASE_PATH = FileSystems.getDefault().getPath("src", "main", "resources", "xml");
    private static final String SAVE_XML = BASE_PATH.resolve("save.xml").toString();

    private XStream xStream;

    public XmlMealyLoader() {
        initializeXstream();
    }

    public MealyMachine load() {
        StringBuilder contents = new StringBuilder();

        try (BufferedReader input = new BufferedReader(new FileReader(SAVE_XML))) {
            String line;
            while ((line = input.readLine()) != null) {
                contents.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (MealyMachine) xStream.fromXML(contents.toString());
    }


    public void write() {
        List<Symbol> alphabet = new ArrayList<Symbol>();

        Symbol a = new Symbol("A");
        Symbol b = new Symbol("B");

        State s1 = new State("S1");
        State s2 = new State("S2");

        alphabet.add(a);
        alphabet.add(b);

        List<State> states = new ArrayList<State>();
        states.add(s1);
        states.add(s2);

        // State transitions

        Map<Symbol, State> stateOne = new HashMap<Symbol, State>();
        stateOne.put(a, s1);
        stateOne.put(b, s2);

        Map<Symbol, State> stateTwo = new HashMap<Symbol, State>();
        stateTwo.put(a, s2);
        stateTwo.put(b, s1);

        Map<State, Map<Symbol, State>> stateTransition = new HashMap<State, Map<Symbol, State>>();
        stateTransition.put(s1, stateOne);
        stateTransition.put(s2, stateTwo);

        // Outputs

        Map<Symbol, Symbol> outputOne = new HashMap<Symbol, Symbol>();
        outputOne.put(a, b);
        outputOne.put(b, a);

        Map<Symbol, Symbol> outputTwo = new HashMap<Symbol, Symbol>();
        outputTwo.put(a, b);
        outputTwo.put(b, a);

        Map<State, Map<Symbol, Symbol>> outputMap = new HashMap<State, Map<Symbol, Symbol>>();
        outputMap.put(s1, outputOne);
        outputMap.put(s2, outputTwo);


        MealyMachine machine = new MealyMachine(states, alphabet, alphabet, stateTransition, outputMap);

        String xml = xStream.toXML(machine);

        File file = new File(SAVE_XML);

        try (PrintWriter out = new PrintWriter(file)) {
            out.print(xml);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initializeXstream() {
        xStream = new XStream();
        xStream.alias("machine", MealyMachine.class);
        xStream.alias("state", State.class);
        xStream.alias("symbol", Symbol.class);
    }
}
