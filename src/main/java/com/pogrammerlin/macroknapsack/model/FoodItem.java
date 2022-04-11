package com.pogrammerlin.macroknapsack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@SuperBuilder
@Table(name = "food_items")
@AllArgsConstructor
@NoArgsConstructor
public class FoodItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "foot_item_id")
    private long id;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "name")
    private String name;

    @Column(name = "kcal")
    private double kcal;

    @Column(name = "carb")
    private double carb;

    @Column(name = "fat")
    private double fat;

    @Column(name = "protein")
    private double protein;

    @Column(name = "fiber")
    private double fiber;

    @Column(name = "servingSize")
    private double servingSize;
}
