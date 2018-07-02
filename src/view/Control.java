package view;

import static org.lwjgl.glfw.GLFW.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * The Control class is a data structure that represents a keyed input control.
 */
public class Control {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs a new Control that is associated with the given keys, description,
     * and callback function.
     * 
     * @param primary     The GLFW code of the primary key associated with this Control.
     * @param secondary   The GLFW code of the secondary key associated with this Control.
     * @param description The description of this Control.
     * @param callback    The callback function to call during a key event match.
     */
    public Control(int primary, int secondary, String description, BiConsumer<Integer, Integer> callback) {
        this.primary = primary;
        this.secondary = secondary;
        this.description = description;
        this.callback = callback;
    }

    /**
     * Returns the GLFW code of the primary key associated with this event.
     * 
     * @return The GLFW code.
     */
    public int getPrimaryKey() {
        return this.primary;
    }

    /**
     * Returns the GLFW code of the secondary key associated with this event.
     * 
     * @return The GLFW code.
     */
    public int getSecondaryKey() {
        return this.secondary;
    }

    /**
     * Returns the callback function associated with this Control.
     * 
     * @return The callback function.
     */
    public BiConsumer<Integer, Integer> getCallback() {
        return this.callback;
    }

    /**
     * Returns a String representation of this Control.
     *
     * @return The String representation.
     */
    public String toString() {
        // Derive the names of the keys that are bound to this Control.
        String first = Control.nameMap.get(this.primary);
        String second = Control.nameMap.get(this.secondary);
        return String.format("%-11s    %-11s    %s", first, second, this.description);
    }


    // Private members
    // -------------------------------------------------------------------------

    /**
     * The key name map that associates GLFW key codes with human-readable names.
     */
    private static Map<Integer, String> nameMap;

    /**
     * The GLFW code of the primary key bound to this Control.
     */
    private int primary;

    /**
     * The GLFW code of the secondary key bound to this Control.
     */
    private int secondary;

    /**
     * The description of this Control.
     */
    private String description;

    /**
     * The callback associated with this Control.
     */
    private BiConsumer<Integer, Integer> callback;

    /**
     * The following static block initializes the key name map.
     */
    static {
        nameMap = new HashMap<>();
        nameMap.put(GLFW_KEY_UNKNOWN,       "");
        nameMap.put(GLFW_KEY_ESCAPE,        "Esc");
        nameMap.put(GLFW_KEY_P,             "P");
        nameMap.put(GLFW_KEY_V,             "V");
        nameMap.put(GLFW_KEY_KP_ADD,        "+");
        nameMap.put(GLFW_KEY_KP_SUBTRACT,   "-");
        nameMap.put(GLFW_KEY_W,             "W");
        nameMap.put(GLFW_KEY_S,             "S");
        nameMap.put(GLFW_KEY_A,             "A");
        nameMap.put(GLFW_KEY_D,             "D");
        nameMap.put(GLFW_KEY_UP,            "Up Arrow");
        nameMap.put(GLFW_KEY_DOWN,          "Down Arrow");
        nameMap.put(GLFW_KEY_LEFT,          "Left Arrow");
        nameMap.put(GLFW_KEY_RIGHT,         "Right Arrow");
        nameMap.put(GLFW_KEY_SPACE,         "Space");
        nameMap.put(GLFW_KEY_LEFT_CONTROL,  "Left CTRL");
        nameMap.put(GLFW_KEY_RIGHT_CONTROL, "Right CTRL");
        nameMap.put(GLFW_MOUSE_BUTTON_1,    "Mouse");
    }
}