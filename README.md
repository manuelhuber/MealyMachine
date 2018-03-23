# MealyMachine
A mealy machine. Serialized as XML (with XStream), reads input and outputs either from command line or JSON (with GSON), uses the strategy pattern and some Java 8 streams

The machine defined in the XML file is pretty simple.
It has two states ("S1" and "S2") and two in- / outputs ("A" and "B")
Input "A" causes the state to stay the same and output "B"
Input "B" causes the state to change and output "A"

Run the programm and choose if you want to use the command line or JSON files as in- and/or output
Copy the files from the resources/json to the resources/input folder if you want to use them (they will be deleted while being read).
The JSON output generates files in the resources/output folder.
