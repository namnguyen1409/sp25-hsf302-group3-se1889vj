package com.group3.sp25hsf302group3se1889vj.config;

import com.group3.sp25hsf302group3se1889vj.dto.CategoryDTO;
import com.group3.sp25hsf302group3se1889vj.entity.Category;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new PropertyMap<Category, CategoryDTO>() {
            @Override
            protected void configure() {
                map().setCreatedBy(source.getCreatedBy());
                map().setCreatedAt(source.getCreatedAt());
                map().setUpdatedBy(source.getUpdatedBy());
                map().setUpdatedAt(source.getUpdatedAt());
                if (source.getParent() != null) {
                    map().setParentId(source.getParent().getId());
                    map().setParentName(source.getParent().getName());
                }
                using(context -> {
                    Set<Category> subCategories = (Set<Category>) context.getSource();
                    return subCategories != null ? subCategories.stream()
                            .filter(Objects::nonNull)
                            .map(category -> modelMapper.map(category, CategoryDTO.class))
                            .collect(Collectors.toSet()) : new HashSet<>();
                }).map(source.getSubCategories(), destination.getSubCategories());
            }
        });




        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return modelMapper;
    }
}
