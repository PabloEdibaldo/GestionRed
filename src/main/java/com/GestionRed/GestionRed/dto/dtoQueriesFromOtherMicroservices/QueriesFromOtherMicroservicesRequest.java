package com.GestionRed.GestionRed.dto.dtoQueriesFromOtherMicroservices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class promotionRequest {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class otro{
        private String titlePromotion;
        private String maxLimit;
        private Long idRouter;
    }
}

