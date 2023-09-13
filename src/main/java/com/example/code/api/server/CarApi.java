package com.example.code.api.server;

import com.example.code.api.response.ErrorResponse;
import com.example.code.entity.Car;
import com.example.code.exception.IllegalApiParamException;
import com.example.code.repository.CarElasticRepository;
import com.example.code.service.CarService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping(value = "/api/car/v1")
public class CarApi {

    private static final Logger LOG = LoggerFactory.getLogger(CarApi.class);

    @Autowired
    private CarService carService;

    @Autowired
    private CarElasticRepository carElasticRepository;

    @GetMapping(value = "/random", produces = MediaType.APPLICATION_JSON_VALUE)
    public Car random() {
        return carService.generateCar();
    }

    @PostMapping(value = "/echo", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String echo(@RequestBody Car car){
        LOG.info("Car is {}", car);
        return car.toString();
    }

    @GetMapping(value = "/random/collection", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Car> randomCollection() {

        var result = new ArrayList<Car>();

        for(int i = 0; i < ThreadLocalRandom.current().nextInt(1, 10); i++){
            result.add(carService.generateCar());
        }

        return result;

    }

    @GetMapping(value = "/count")
    public String countCar() {
        return "There are : " + carElasticRepository.count() + " cars";
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String saveCar(@RequestBody Car car){
        var id = carElasticRepository.save(car);

        return "Saved with ID : " + id.getId();
    }

    @GetMapping(value = "/{id}")
    public Car getCar(@PathVariable("id") String carId){
        return carElasticRepository.findById(carId).orElse(null);
    }

    @PutMapping(value = "/{id}")
    public String updateCar(@PathVariable("id") String carId, @RequestBody Car updateCar){
        updateCar.setId(carId);
        var newCar = carElasticRepository.save(updateCar);

        return "Updated car with ID : " + newCar.getId();
    }

    @GetMapping(value = "/find-json", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Car> findCarByBrandAndColor(@RequestBody Car car){
        return carElasticRepository.findByBrandAndColor(car.getBrand(), car.getColor());
    }

    @GetMapping(value = "/cars/{brand}/{color}")
    public ResponseEntity<Object> findCarByPath(@PathVariable String brand, @PathVariable String color){

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.SERVER, "Spring");
        headers.add("X-Custom-Header", "Custom Response Header");

        if(StringUtils.isNumeric(color)){
            var errorResponse = new ErrorResponse("Invalid color : " + color, LocalDateTime.now());

            return new ResponseEntity<Object>(errorResponse, null, HttpStatus.BAD_REQUEST);
        }

        var cars = carElasticRepository.findByBrandAndColor(brand, color);

        return ResponseEntity.ok().headers(headers).body(cars);
    }


     @GetMapping(value = "/cars")
     public List<Car> findCarByParam(@RequestParam String brand, @RequestParam String color){

        if(StringUtils.isNumeric(color)){
            throw new IllegalArgumentException("Invalid color : " + color);
        }
        if(StringUtils.isNumeric(brand)){
            throw new IllegalApiParamException("Invalid brand : " + brand);
        }

        return carElasticRepository.findByBrandAndColor(brand, color);
     }

    @GetMapping(value = "/cars/date")
    public List<Car> findCarsReleasedAfter(
            @RequestParam(name = "first_release_date") @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate firstReleaseDate){
        return carElasticRepository.findByFirstReleaseDateAfter(firstReleaseDate);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    private ResponseEntity<ErrorResponse> handleInvalidColorException(IllegalArgumentException e){
        var message = "Exception, " + e.getMessage();
        LOG.warn(message);

        var errorResponse = new ErrorResponse(message, LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value = IllegalApiParamException.class)
    private ResponseEntity<ErrorResponse> handleInvalidBrandException(IllegalApiParamException e){
        var message = "Exception API Param, " + e.getMessage();
        LOG.warn(message);

        var errorResponse = new ErrorResponse(message, LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}
