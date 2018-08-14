package pl.mczernek.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import pl.mczernek.annotation.JsonFile;

@SupportedAnnotationTypes("pl.mczernek.annotation.JsonFile")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@AutoService(Processor.class)
public class JsonFileProcessor extends AbstractProcessor {

    private Messager messager;
    private Elements elements;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.messager = processingEnvironment.getMessager();
        this.elements = processingEnvironment.getElementUtils();
        this.filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

//        messager.printMessage(Diagnostic.Kind.ERROR, "JsonFile annotation applicable only to classes");
//        return true;

        for (Element element : roundEnvironment.getElementsAnnotatedWith(JsonFile.class)) {
            if (element.getKind() != ElementKind.CLASS) {
                messager.printMessage(Diagnostic.Kind.ERROR, "JsonFile annotation applicable only to classes");
                return true;
            }

            TypeElement typeElement = (TypeElement) element;
            PackageElement elementPackage = elements.getPackageOf(typeElement);
            Name elementName = typeElement.getSimpleName();

            TypeSpec.Builder configClassBuilder = TypeSpec.classBuilder("JsonFile_" + elementName.toString())
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

            MethodSpec packageMethod = MethodSpec.methodBuilder("getPackage")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .returns(String.class)
                    .addCode("return \"$L\";\n", elementPackage.toString())
                    .build();

            configClassBuilder.addMethod(packageMethod);

            try {

                JavaFile.builder("pl.mczernek.jsonjavagenerator", configClassBuilder.build())
                        .build()
                        .writeTo(filer);
            } catch (IOException ex) {
                messager.printMessage(Diagnostic.Kind.ERROR, "IOException while generating class: " + "JsonFile_" + elementName.toString());
            }

        }

        return false;
    }
}
