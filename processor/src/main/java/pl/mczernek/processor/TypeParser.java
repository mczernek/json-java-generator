package pl.mczernek.processor;

import android.support.annotation.Nullable;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

public interface TypeParser {

    @Nullable
    MethodSpec addEntry(TypeSpec.Builder builder, Object key, Object value);

}
