package com.godx.annoprocessor.router;

import com.godx.annoprocessor.BaseProcessor;
import com.godx.annotation.router.AutoService;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

@com.google.auto.service.AutoService(Processor.class)
public class AutoServiceProcessor extends BaseProcessor {
    private HashMap<String, Set<String>> providers = new HashMap<>();
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(AutoService.class);
        for (Element element : elements){
            if (element.getKind() == ElementKind.CLASS){
                List<? extends AnnotationMirror> mirrors = element.getAnnotationMirrors();
                for (AnnotationMirror mirror : mirrors){
                    Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = mirror.getElementValues();
                    Set<? extends ExecutableElement> keySet = elementValues.keySet();
                    for (ExecutableElement key : keySet){
                        AnnotationValue value = elementValues.get(key);
                        String valueString = value.toString();

                        if (!valueString.contains(".class")){
                            continue;
                        }
                        valueString = valueString.substring(0,valueString.length() - 6);
                        Set<String> providerValue = providers.get(valueString);
                        String className = processingEnv.getElementUtils().getPackageOf(element).getQualifiedName().toString()+"."+element.getSimpleName().toString();
                        if (providerValue == null){
                            providerValue = new HashSet<>();
                        }else if (providerValue.contains(className)){
                             continue;
                        }
                        providerValue.add(className);
                        providers.put(valueString,providerValue);
                    }
                }
            }
        }

        for (String fileName : providers.keySet()){
            try {
                String resourceFile = "META-INF/services/" + fileName;
                FileObject fileObject = filer.createResource(StandardLocation.CLASS_OUTPUT, "",
                        resourceFile);
                Set<String> cacheString = providers.get(fileName);
                OutputStream fileOutputStream = fileObject.openOutputStream();
                for (String ssss : cacheString){
                    byte[] bytes = (ssss+"\n").getBytes();
                    fileOutputStream.write(bytes,0,bytes.length);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
            }catch (Exception e){
            }
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> strings = new HashSet<>();
        strings.add(AutoService.class.getCanonicalName());
        return strings;
    }
}
