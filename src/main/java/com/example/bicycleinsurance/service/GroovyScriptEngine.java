package com.example.bicycleinsurance.service;

import com.example.bicycleinsurance.exception.ScriptExecutionException;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.Script;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class GroovyScriptEngine {

    private final Map<String, Class<?>> scriptCache = new ConcurrentHashMap<>();
    private final CompilerConfiguration compilerConfiguration;
    private final GroovyClassLoader groovyClassLoader;

    public GroovyScriptEngine() {
        compilerConfiguration = new CompilerConfiguration();
        compilerConfiguration.setScriptBaseClass("com.example.bicycleinsurance.groovy.BaseScript");

        SecureASTCustomizer secureASTCustomizer = new SecureASTCustomizer();
        secureASTCustomizer.setImportsWhitelist(Arrays.asList("java.lang.Math"));
        secureASTCustomizer.setMethodDefinitionAllowed(false);
        secureASTCustomizer.setClosuresAllowed(true);

        compilerConfiguration.addCompilationCustomizers(secureASTCustomizer);

        ClassLoader parentClassLoader = Thread.currentThread().getContextClassLoader();
        groovyClassLoader = new GroovyClassLoader(parentClassLoader, compilerConfiguration);
    }

    public Object executeScript(String scriptName, Map<String, Object> variables) {
        try {
            Class<?> scriptClass = scriptCache.computeIfAbsent(scriptName, this::compileScript);
            Script script = (Script) scriptClass.getDeclaredConstructor().newInstance();
            Binding binding = new Binding();
            variables.forEach(binding::setVariable);
            script.setBinding(binding);
            return script.run();
        } catch (Exception e) {
            log.error("Error executing Groovy script: {}", scriptName, e);
            throw new ScriptExecutionException("Error executing script: " + scriptName + ": " + e.getMessage());
        }
    }

    private Class<?> compileScript(String scriptName) {
        try {
            String scriptPath = "scripts/" + scriptName + ".groovy";
            InputStream scriptStream = getClass().getClassLoader().getResourceAsStream(scriptPath);
            if (scriptStream == null) {
                throw new IOException("Script not found: " + scriptPath);
            }
            String scriptText = new String(scriptStream.readAllBytes(), StandardCharsets.UTF_8);

            scriptText = scriptText.replaceAll("(?m)^\\s*package\\s+.*$", "");

            return groovyClassLoader.parseClass(scriptText, scriptName + ".groovy");
        } catch (IOException e) {
            log.error("Error compiling Groovy script: {}", scriptName, e);
            throw new ScriptExecutionException("Error compiling script: " + scriptName + ": " + e.getMessage());
        }
    }
}