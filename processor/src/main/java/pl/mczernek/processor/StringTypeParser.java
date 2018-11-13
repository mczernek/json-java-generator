package pl.mczernek.processor;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

public class StringTypeParser implements TypeParser {

    private final static String CANONICAL_STRING_NAME = "java.lang.String";

    @Override
    public boolean addEntry(TypeSpec.Builder builder, Object key, Object value) {
        if(value.getClass().getCanonicalName().equals(CANONICAL_STRING_NAME)) {
            builder.addMethod(
            MethodSpec.methodBuilder((String) key)
                    .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                    .returns(String.class)
                    .addCode("return \"$L\";\n", value)
                    .build());
            return true;
        }
        return false;
    }
}