package com.pogrammerlin.macroknapsack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Set;

@Data
@Entity
@SuperBuilder
@Table(name = "macro")
@AllArgsConstructor
@NoArgsConstructor
public class Macro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "macro_id")
    private long id;

    @Column(name = "kcal_goal")
    private double kcalGoal;

    @Column(name = "carb_goal")
    private double carbGoal;

    @Column(name = "fat_goal")
    private double fatGoal;

    @Column(name = "fiber_goal")
    private double fiberGoal;

    @Column(name = "protein_goal")
    private double proteinGoal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany
    @JoinTable(name = "macro_nutrition_plan",
            joinColumns = @JoinColumn(name = "macro_id"),
            inverseJoinColumns = @JoinColumn(name = "nutrition_plan_id"))
    private Set<NutritionPlan> nutritionPlans;
}
