package pl.mczernek.processor;

import android.support.annotation.Nullable;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

public class JSONObjectTypeParser implements TypeParser {

    private final static String CANONICAL_JSON_OBJECT = "org.json.simple.JSONObject";

    private final TypeParser[] parsers = {new BooleanTypeParser(), new StringTypeParser(), new DoubleTypeParser(), new JSONObjectTypeParser()};

    @Override @Nullable
    public MethodSpec addEntry(TypeSpec.Builder builder, Object key, Object value) {
        if (value.getClass().getCanonicalName().equals(CANONICAL_JSON_OBJECT)) {

        }
        return null;
    }
}
