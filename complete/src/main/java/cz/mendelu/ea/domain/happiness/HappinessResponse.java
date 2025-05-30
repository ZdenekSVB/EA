// ===============================
// HappinessResponse.java (DTO for response)
// ===============================

package cz.mendelu.ea.domain.happiness;

import lombok.Data;

/**
 * DTO representing happiness data in API responses.
 */
@Data
public class HappinessResponse {

    private Long id;
    private Long countryId;
    private int year;
    private Integer rank;
    private Double happinessScore;
    private Double gdp;
    private Double socialSupport;

    /**
     * Create response DTO from Happiness entity.
     */
    public HappinessResponse(Happiness happiness) {
        this.id = happiness.getId();
        this.countryId = happiness.getCountry().getId();
        this.year = happiness.getYear();
        this.rank = happiness.getRank();
        this.happinessScore = happiness.getHappinessScore();
        this.gdp = happiness.getGdp();
        this.socialSupport = happiness.getSocialSupport();
    }
}
