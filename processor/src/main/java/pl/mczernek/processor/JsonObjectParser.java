package pl.mczernek.processor;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import org.json.simple.JSONObject;

import javax.lang.model.element.Modifier;

public class JsonObjectParser {

    private static final TypeParser[] parsers = {new BooleanTypeParser(), new StringTypeParser(), new DoubleTypeParser(), new JSONObjectTypeParser()};

    public static TypeSpec.Builder parse(String name, JSONObject object) {
        TypeSpec.Builder configClassBuilder = TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        for (Object key: object.keySet()) {
            for(TypeParser typeParser: parsers) {
                if (typeParser.addEntry(configClassBuilder, key, object.get(key))) break;
            }
        }
        return configClassBuilder;
    }


}
