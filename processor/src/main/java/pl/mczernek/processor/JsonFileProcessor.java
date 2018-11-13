package pl.mczernek.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
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

        for (Element element : roundEnvironment.getElementsAnnotatedWith(JsonFile.class)) {
            if (element.getKind() != ElementKind.CLASS) {
                messager.printMessage(Diagnostic.Kind.ERROR, "JsonFile annotation applicable only to classes");
                return true;
            }

            TypeElement typeElement = (TypeElement) element;
            PackageElement elementPackage = elements.getPackageOf(typeElement);
            Name elementName = typeElement.getSimpleName();

            String filePath = element.getAnnotation(JsonFile.class).path();

            File projectDir = new File(System.getProperty("user.dir"));

            File jsonFile = new File(projectDir, filePath);

            try {
                FileReader fileReader = new FileReader(jsonFile);

                JSONParser parser = new JSONParser();
                JSONObject object = (JSONObject)parser.parse(fileReader);
                writeClassToFile(elementPackage, JsonObjectParser.parse("JsonFile_" + elementName.toString(), object));


            } catch (IOException ex) {
                messager.printMessage(Diagnostic.Kind.ERROR, "Unable to locate file: " + jsonFile.getAbsolutePath());
            }  catch (ParseException ex) {
                messager.printMessage(Diagnostic.Kind.ERROR, "Invalid JSON format! " + jsonFile.getAbsolutePath());
            }

        }

        return false;
    }

    private void writeClassToFile(PackageElement elementPackage, TypeSpec.Builder configClassBuilder) {
        TypeSpec typeSpec = configClassBuilder.build();
        try {
            JavaFile.builder(elementPackage.toString(), typeSpec)
                    .build()
                    .writeTo(filer);
        } catch (IOException ex) {
            messager.printMessage(Diagnostic.Kind.ERROR, "IOException while generating class: " + typeSpec.name);
        }
    }
}
