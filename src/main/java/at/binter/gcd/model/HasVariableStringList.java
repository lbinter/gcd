package at.binter.gcd.model;

import java.util.List;
import java.util.Set;

public interface HasVariableStringList {
    List<String> getVariables();

    Set<String> getVariablesRemoved();

    Set<String> getVariablesAdded();
}