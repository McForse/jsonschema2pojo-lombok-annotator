package com.mcforse.js2pojo;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.codemodel.JDefinedClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsonschema2pojo.AbstractAnnotator;

public class LombokAnnotator extends AbstractAnnotator {
    @Override
    public void propertyInclusion(JDefinedClass clazz, JsonNode schema) {
        JsonNode additionalProperties = schema.get("additionalProperties");

        if (additionalProperties != null) {
            additionalProperties.fieldNames().forEachRemaining(property -> {
                Class annotation = getAnnotation(property);

                if (!annotation.equals(IllegalArgumentException.class)) {
                    clazz.annotate(annotation);
                }
            });
        }
    }

    @Override
    public boolean isAdditionalPropertiesSupported() {
        return false;
    }

    private Class getAnnotation(String property) {
        switch (property) {
            case "lombok-no-args-constructor":
                return NoArgsConstructor.class;
            case "lombok-all-args-constructor":
                return AllArgsConstructor.class;
            case "lombok-data":
                return Data.class;
            case "lombok-builder":
                return Builder.class;
            default:
                return IllegalArgumentException.class;
        }
    }
}
