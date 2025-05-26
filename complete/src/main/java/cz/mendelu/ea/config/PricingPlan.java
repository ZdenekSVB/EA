package cz.mendelu.ea.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
import java.util.List;

@AllArgsConstructor
@Getter
enum PricingPlan {
    FREE(100),
    BASIC(1000),
    PRO(10000);

    private final int rateLimit;

    static PricingPlan forRoles(List<String> roles) {
        if (roles.contains("SUPERADMIN")) {
            return PRO;
        } else if (roles.contains("OFFICER")) {
            return BASIC;
        } else {
            return FREE;
        }
    }

}


