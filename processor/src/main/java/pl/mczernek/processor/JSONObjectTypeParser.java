package pl.mczernek.processor;

import android.support.annotation.Nullable;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import org.json.simple.JSONObject;

import javax.lang.model.element.Modifier;

public class JSONObjectTypeParser implements TypeParser {

    private final static String CANONICAL_JSON_OBJECT = "org.json.simple.JSONObject";

    @Override
    public boolean addEntry(TypeSpec.Builder builder, Object key, Object value) {
        if (value.getClass().getCanonicalName().equals(CANONICAL_JSON_OBJECT)) {
            TypeSpec type = JsonObjectParser.parse(key.toString(), (JSONObject) value).addModifiers(Modifier.STATIC, Modifier.PUBLIC).build();
            builder.addType(type);
            String variableName = type.name;
            builder.addField(FieldSpec.builder(TypeName.OBJECT, variableName, Modifier.STATIC).initializer("new $L()", type.name).build());
            builder.addMethod(MethodSpec.methodBuilder((String) key)
                    .addModifiers(Modifier.FINAL, Modifier.PUBLIC, Modifier.STATIC)
                    .returns(TypeName.OBJECT)
                    .addCode("return $L;\n", variableName)
                    .build());
            return true;
        }
        return false;
    }
}
