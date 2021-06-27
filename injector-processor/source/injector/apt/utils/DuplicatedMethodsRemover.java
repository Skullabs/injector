package injector.apt.utils;

import generator.apt.SimplifiedAST;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class DuplicatedMethodsRemover {

    public void removeFrom(SimplifiedAST.Type type) {
        val groupedByName = new HashMap<String, SimplifiedAST.Method>();
        type.getMethods().forEach( it -> groupedByName.putIfAbsent(it.toString(), it) );

        val deDuplicated = groupedByName
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        type.setMethods(deDuplicated);
    }
}
