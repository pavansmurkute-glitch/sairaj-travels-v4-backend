package com.sairajtravels.site.controller;

import com.sairajtravels.site.entity.Route;
import com.sairajtravels.site.repository.RouteRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteRepository repo;

    public RouteController(RouteRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Route> getAllRoutes() {
        return repo.findAll();
    }

    @PostMapping
    public Route createRoute(@RequestBody Route route) {
        return repo.save(route);
    }
}
