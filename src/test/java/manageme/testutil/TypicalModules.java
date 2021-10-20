package manageme.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import manageme.model.ManageMe;
import manageme.model.module.Module;

public class TypicalModules {
    public static final Module MODULE_A = new ModuleBuilder().withName("CS111")
            .withLink("https://www.google.com").build();
    public static final Module MODULE_B = new ModuleBuilder().withName("CS222")
            .withLink("https://www.github.com").build();

    private TypicalModules() {} // prevents instantiation

    /**
     * Returns an {@code ManageMe} with all the typical modules.
     */
    public static ManageMe getTypicalManageMe() {
        ManageMe ab = new ManageMe();
        for (Module module : getTypicalModules()) {
            ab.addModule(module);
        }
        return ab;
    }

    public static List<Module> getTypicalModules() {
        return new ArrayList<>(Arrays.asList(MODULE_A, MODULE_B));
    }
}
