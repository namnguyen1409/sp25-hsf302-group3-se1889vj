package com.group3.sp25hsf302group3se1889vj.config;

import com.group3.sp25hsf302group3se1889vj.dto.*;
import com.group3.sp25hsf302group3se1889vj.entity.*;
import org.hibernate.collection.spi.PersistentSet;
import org.modelmapper.Converter;
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

        modelMapper.addConverter(new PersistentSetToSetConverter());

        modelMapper.addMappings(new PropertyMap<Category, CategoryDTO>() {
            Converter<Category, Long> parentToIdConverter = ctx ->
                    ctx.getSource() == null ? null : ctx.getSource().getId();

            Converter<Category, String> parentToNameConverter = ctx ->
                    ctx.getSource() == null ? null : ctx.getSource().getName();
            @Override
            protected void configure() {
                using(parentToIdConverter).map(source.getParent(), destination.getParentId());
                using(parentToNameConverter).map(source.getParent(), destination.getParentName());
                using(context -> {
                    Set<Category> subCategories = (Set<Category>) context.getSource();
                    return subCategories != null ? subCategories.stream()
                            .filter(Objects::nonNull)
                            .map(category -> modelMapper.map(category, CategoryDTO.class))
                            .collect(Collectors.toSet()) : new HashSet<>();
                }).map(source.getSubCategories(), destination.getSubCategories());
            }
        });

        modelMapper.addMappings(new PropertyMap<Order,OrderDTO>() {
            @Override
            protected void configure() {

            }
        });

        modelMapper.addMappings(new PropertyMap<OrderItem, OrderItemDTO>() {
            @Override
            protected void configure() {
                map().setOrderId(source.getOrder().getId());
                map().setProductId(source.getProduct().getId());
                map().setProductVariantId(source.getProductVariant().getId());
            }
        });




        modelMapper.addMappings(new PropertyMap<OrderStatusHistory, OrderStatusHistoryDTO>() {
            @Override
            protected void configure() {
                skip(destination.getOrderStatus());
                map().setOrderId(source.getOrder().getId());
            }
        });


        modelMapper.addMappings(new PropertyMap<Notification, NotificationDTO>() {
            @Override
            protected void configure() {
                map().setUsername(source.getUser().getUsername());
            }
        });

        modelMapper.addMappings(new PropertyMap<ProductImage, ProductImageDTO>() {
            @Override
            protected void configure() {
                map().setProductId(source.getProduct().getId());
            }
        });

        modelMapper.addMappings(
                new PropertyMap<OrderTransaction, OrderTransactionDTO>() {
                    @Override
                    protected void configure() {
                        map().setOrderId(source.getOrder().getId());
                    }
                }
        );

        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return modelMapper;
    }
}
