package com.group3.sp25hsf302group3se1889vj.component;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.HashSet;
import java.util.Set;

public class CustomThymeleafDialect extends AbstractProcessorDialect {
    private static final String DIALECT_NAME = "Custom Thymeleaf Dialect";

    public CustomThymeleafDialect() {
        super(DIALECT_NAME, "custom", 1000);
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        Set<IProcessor> processors = new HashSet<>();
        processors.add(new AutoFieldIdProcessor(dialectPrefix));
        return processors;
    }
}
