package com.example.code.api.server;

import com.example.code.entity.CarPromotion;
import com.example.code.exception.IllegalApiParamException;
import com.example.code.repository.CarPromotionElasticRepository;
import com.example.code.service.CarPromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/car/v1")
public class CarPromotionApi {

    @Autowired
    private CarPromotionService carPromotionService;

    @Autowired
    private CarPromotionElasticRepository carPromotionElasticRepository;

    @GetMapping(value = "/promotion")
    public List<CarPromotion> listAvailablePromotions(@RequestParam(name = "type") String promotionType, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        if(!carPromotionService.isValidPromotionType(promotionType)){
            throw new IllegalApiParamException("Invalid promotion type : " + promotionType);
        }

        var pageable = PageRequest.of(page, size);
        return carPromotionElasticRepository.findByType(promotionType, pageable).getContent();
    }
}
