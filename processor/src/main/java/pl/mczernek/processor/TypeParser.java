package pl.mczernek.processor;

import com.squareup.javapoet.TypeSpec;

public interface TypeParser {

    boolean addEntry(TypeSpec.Builder builder, Object key, Object value);

}
