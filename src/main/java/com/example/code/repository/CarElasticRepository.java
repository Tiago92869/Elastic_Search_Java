package com.example.code.repository;

import com.example.code.entity.Car;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CarElasticRepository extends ElasticsearchRepository<Car, String> {

    public List<Car> findByBrandAndColor(String brand, String color);

    public List<Car> findByFirstReleaseDateAfter(LocalDate date);
}
