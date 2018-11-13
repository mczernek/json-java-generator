package pl.mczernek.processor;

import android.support.annotation.Nullable;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

public class BooleanTypeParser implements TypeParser {

    private final static String CANONICAL_BOOLEAN_NAME = "java.lang.Boolean";

    @Override @Nullable
    public MethodSpec addEntry(TypeSpec.Builder builder, Object key, Object value) {
        if(value.getClass().getCanonicalName().equals(CANONICAL_BOOLEAN_NAME)) {
            return MethodSpec.methodBuilder((String) key)
                    .addModifiers(Modifier.FINAL, Modifier.PUBLIC, Modifier.STATIC)
                    .returns(Boolean.class)
                    .addCode("return $L;\n", value)
                    .build();
        }
        return null;
    }
}