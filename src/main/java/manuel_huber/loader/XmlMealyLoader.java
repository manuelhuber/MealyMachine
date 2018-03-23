package manuel_huber.loader;

import com.thoughtworks.xstream.XStream;
import manuel_huber.model.Constants;
import manuel_huber.model.MealyMachine;
import manuel_huber.model.State;
import manuel_huber.model.Symbol;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads Mealy machines from XML files
 * XML is still relevant
 */
public class XmlMealyLoader {

    private static final Path BASE_PATH = Constants.RESOURCE_PATH.resolve("xml");
    private static final String SAVE_XML = BASE_PATH.resolve("save.xml").toString();

    // This is an external library to handle XML (de-)serialization
    private XStream xStream;

    public XmlMealyLoader() {
        initializeXstream();
    }

    public MealyMachine load() {
        StringBuilder contents = new StringBuilder();

        // Read the file as string (pretty standard stuff)
        try (BufferedReader input = new BufferedReader(new FileReader(SAVE_XML))) {
            String line;
            while ((line = input.readLine()) != null) {
                contents.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Let XStream do the XML stuff
        return (MealyMachine) xStream.fromXML(contents.toString());
    }

    /**
     * Creates a Mealy Machine and writes it into a XML file (which can be used as reference for other machines).
     * The machine has two states ("S1" and "S2") and two in- / outputs ("A" and "B")
     * Input "A" causes the state to stay the same and output "B"
     * Input "B" causes the state to change and output "A"
     */
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
        // Alias make the XML look nicer
        xStream.alias("machine", MealyMachine.class);
        xStream.alias("state", State.class);
        xStream.alias("symbol", Symbol.class);
    }
}
