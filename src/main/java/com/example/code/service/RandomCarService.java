package com.example.code.service;

import com.example.code.entity.Car;
import com.example.code.entity.Engine;
import com.example.code.entity.Tire;
import com.example.code.util.RandomDateUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class RandomCarService implements CarService {

    @Override
    public Car generateCar(){

        var brand = BRANDS.get(ThreadLocalRandom.current().nextInt(0, BRANDS.size()));
        var color = COLORS.get(ThreadLocalRandom.current().nextInt(0, BRANDS.size()));
        var type = TYPES.get(ThreadLocalRandom.current().nextInt(0, BRANDS.size()));

        var available = ThreadLocalRandom.current().nextBoolean();
        var price = ThreadLocalRandom.current().nextInt(5000, 12001);
        var date = RandomDateUtil.generateRandomLocalDate();

        int randomCount = ThreadLocalRandom.current().nextInt(ADDITIONAL_FEATURES.size());
        var additionalFeatures = new ArrayList<String>();

        for (int i = 0; i < randomCount; i++){
            additionalFeatures.add(ADDITIONAL_FEATURES.get(i));
        }

        var fuels = FUELS.get(ThreadLocalRandom.current().nextInt(FUELS.size()));
        var horsePower = ThreadLocalRandom.current().nextInt(100, 221);
        var engine = new Engine();
        engine.setFuelType(fuels);
        engine.setHorsePower(horsePower);

        var tires = new ArrayList<Tire>();
        for (int i = 0; i < 3; i++){
            var tire = new Tire();
            var manufacturer = TIRE_MANUFACTURERS.get(ThreadLocalRandom.current().nextInt(TIRE_MANUFACTURERS.size()));
            var size = ThreadLocalRandom.current().nextInt(15, 18);
            var priceT = ThreadLocalRandom.current().nextInt(200, 401);

            tire.setManufacturer(manufacturer);
            tire.setSize(size);
            tire.setPrice(priceT);

            tires.add(tire);
        }

        var secreteFeature = ThreadLocalRandom.current().nextBoolean() ? "Can fly" : null;


        var result = new Car(brand, color, type);
        result.setAdditionalFeatures(additionalFeatures);
        result.setAvailable(available);
        result.setPrice(price);
        result.setFirstReleaseDate(date);
        result.setEngine(engine);
        result.setTires(tires);
        result.setSecreteFeature(secreteFeature);

        return result;

    }
}
