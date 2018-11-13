package pl.mczernek.processor;

import android.support.annotation.Nullable;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import org.json.simple.JSONObject;

import javax.lang.model.element.Modifier;

public class JSONObjectTypeParser implements TypeParser {

    private final static String CANONICAL_JSON_OBJECT = "org.json.simple.JSONObject";

    @Override @Nullable
    public MethodSpec addEntry(TypeSpec.Builder builder, Object key, Object value) {
        if (value.getClass().getCanonicalName().equals(CANONICAL_JSON_OBJECT)) {
            TypeSpec type = JsonObjectParser.parse(key.toString(), (JSONObject) value).addModifiers(Modifier.STATIC, Modifier.PUBLIC).build();
            builder.addType(type);
            return MethodSpec.methodBuilder((String) key)
                    .addModifiers(Modifier.FINAL, Modifier.PUBLIC, Modifier.STATIC)
                    .returns(TypeName.OBJECT)
                    .addCode("return new $L();\n", type.name)
                    .build();
        }
        return null;
    }
}
