package pl.mczernek.processor;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

public class DoubleTypeParser implements TypeParser {

    private final static String CANONICAL_DOUBLE_NAME = "java.lang.Double";

    @Override
    public boolean addEntry(TypeSpec.Builder builder, Object key, Object value) {
        if(value.getClass().getCanonicalName().equals(CANONICAL_DOUBLE_NAME)) {
            builder.addMethod(MethodSpec.methodBuilder((String) key)
                    .addModifiers(Modifier.FINAL, Modifier.PUBLIC, Modifier.STATIC)
                    .returns(Double.class)
                    .addCode("return $L;\n", value)
                    .build());
            return true;
        }
        return false;
    }
}