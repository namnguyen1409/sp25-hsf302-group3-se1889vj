package com.group3.sp25hsf302group3se1889vj.config;

import com.group3.sp25hsf302group3se1889vj.dto.*;
import com.group3.sp25hsf302group3se1889vj.entity.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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


        modelMapper.addMappings(new PropertyMap<Brand, BrandDTO>() {
            @Override
            protected void configure() {
                // TODO: Thêm các thao tác mapping khác nếu cần
            }
        });


        modelMapper.addMappings(new PropertyMap<Coupon, CouponDTO>() {
            @Override
            protected void configure() {
                // TODO: Thêm các thao tác mapping khác nếu cần
            }

        });

        modelMapper.addMappings(new PropertyMap<CustomerAddress, CustomerAddressDTO>() {
            @Override
            protected void configure() {
                // TODO: Thêm các thao tác mapping khác nếu cần
            }
        });


        modelMapper.addMappings(new PropertyMap<Notification, NotificationDTO>() {
            @Override
            protected void configure() {
                // TODO: Thêm các thao tác mapping khác nếu cần
            }
        });



        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return modelMapper;
    }
}
