package com.sairajtravels.site.controller;

import com.sairajtravels.site.entity.Bus;
import com.sairajtravels.site.repository.BusRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buses")
public class BusController {

    private final BusRepository repo;

    public BusController(BusRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Bus> getAllBuses() {
        return repo.findAll();
    }

    @PostMapping
    public Bus createBus(@RequestBody Bus bus) {
        return repo.save(bus);
    }
}
