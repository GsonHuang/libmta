package com.mta.libprocessor;

import com.google.auto.service.AutoService;
import com.mta.libannotation.Action;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class ActionProcessor extends AbstractProcessor {

    private Filer mFiler;
    private Elements mElementUtils;
    private Messager envMessager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        envMessager = processingEnv.getMessager();
    }

    //processor run more time
    boolean creatMark;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (creatMark){
            return true;
        }
        envMessager.printMessage(Diagnostic.Kind.NOTE, "=========================");
        Set<? extends Element> rootElements = roundEnv.getElementsAnnotatedWith(Action.class);

        List<FieldSpec> fieldSpecs = new ArrayList<>();
        for (Element rootElement : rootElements) {
            Action annotation = rootElement.getAnnotation(Action.class);
            String[] values = annotation.value();
            for (String value : values) {

                FieldSpec fieldSpec = FieldSpec.builder(String.class, value.toUpperCase())
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                        .initializer("$S", value)
                        .build();
                fieldSpecs.add(fieldSpec);
            }
        }
        String className = "Actions";
        String packagePath = "com.libmta";

        TypeSpec typeSpec = TypeSpec.interfaceBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addFields(fieldSpecs)
                .build();
        JavaFile build = JavaFile.builder(packagePath, typeSpec).build();
        envMessager.printMessage(Diagnostic.Kind.NOTE, build.toString() + "");
        try {
            build.writeTo(mFiler);
            creatMark = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Action.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}