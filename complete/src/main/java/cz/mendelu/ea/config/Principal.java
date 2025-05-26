package cz.mendelu.ea.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class Principal {

    String username;

    String name;

    String email;

    List<String> roles = new ArrayList<>();

    PricingPlan pricingPlan;

}