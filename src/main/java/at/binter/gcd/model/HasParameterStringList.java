package at.binter.gcd.model;

import java.util.List;
import java.util.Set;

public interface HasParameterStringList {
    List<String> getParameters();

    Set<String> getParametersRemoved();

    Set<String> getParametersAdded();
}