package com.group3.sp25hsf302group3se1889vj.config;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.hibernate.collection.spi.PersistentSet;

import java.util.HashSet;
import java.util.Set;

public class PersistentSetToSetConverter implements Converter<PersistentSet<?>, Set<?>> {
    @Override
    public Set<?> convert(MappingContext<PersistentSet<?>, Set<?>> context) {
        return context.getSource() != null ? new HashSet<>(context.getSource()) : new HashSet<>();
    }
}