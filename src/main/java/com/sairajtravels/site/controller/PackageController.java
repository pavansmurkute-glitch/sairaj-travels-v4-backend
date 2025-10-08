package com.sairajtravels.site.controller;

import com.sairajtravels.site.dto.PackageDTO;
import com.sairajtravels.site.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/packages")
public class PackageController {

    @Autowired
    private PackageService packageService;

    @GetMapping
    public ResponseEntity<List<PackageDTO>> getAllPackages() {
        try {
            List<PackageDTO> packages = packageService.getAllPackages();
            return ResponseEntity.ok(packages);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/admin")
    public ResponseEntity<List<PackageDTO>> getAllPackagesForAdmin() {
        try {
            System.out.println("=== PACKAGE CONTROLLER: /admin endpoint called ===");
            List<PackageDTO> packages = packageService.getAllPackagesForAdmin();
            System.out.println("Controller returning " + packages.size() + " packages");
            return ResponseEntity.ok(packages);
        } catch (Exception e) {
            System.out.println("=== PACKAGE CONTROLLER: Error occurred ===");
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<PackageDTO>> getPackagesByCategory(@PathVariable String categoryId) {
        try {
            List<PackageDTO> packages = packageService.getPackagesByCategory(categoryId);
            return ResponseEntity.ok(packages);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/featured")
    public ResponseEntity<List<PackageDTO>> getFeaturedPackages() {
        try {
            List<PackageDTO> packages = packageService.getFeaturedPackages();
            return ResponseEntity.ok(packages);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<PackageDTO>> searchPackages(@RequestParam String q) {
        try {
            List<PackageDTO> packages = packageService.searchPackages(q);
            return ResponseEntity.ok(packages);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<PackageDTO>> getPackagesByPriceRange(
            @RequestParam BigDecimal minPrice, 
            @RequestParam BigDecimal maxPrice) {
        try {
            List<PackageDTO> packages = packageService.getPackagesByPriceRange(minPrice, maxPrice);
            return ResponseEntity.ok(packages);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<List<PackageDTO>> getPackagesByCategoryAndPriceRange(
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        try {
            List<PackageDTO> packages;
            if (categoryId != null && minPrice != null && maxPrice != null) {
                packages = packageService.getPackagesByCategoryAndPriceRange(categoryId, minPrice, maxPrice);
            } else if (categoryId != null) {
                packages = packageService.getPackagesByCategory(categoryId);
            } else if (minPrice != null && maxPrice != null) {
                packages = packageService.getPackagesByPriceRange(minPrice, maxPrice);
            } else {
                packages = packageService.getAllPackages();
            }
            return ResponseEntity.ok(packages);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackageDTO> getPackageById(@PathVariable Integer id) {
        try {
            PackageDTO packageDTO = packageService.getPackageById(id);
            if (packageDTO != null) {
                return ResponseEntity.ok(packageDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping
    public ResponseEntity<PackageDTO> createPackage(@RequestBody PackageDTO packageDTO) {
        try {
            PackageDTO createdPackage = packageService.createPackage(packageDTO);
            return ResponseEntity.ok(createdPackage);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PackageDTO> updatePackage(@PathVariable Integer id, @RequestBody PackageDTO packageDTO) {
        try {
            PackageDTO updatedPackage = packageService.updatePackage(id, packageDTO);
            if (updatedPackage != null) {
                return ResponseEntity.ok(updatedPackage);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable Integer id) {
        try {
            boolean deleted = packageService.deletePackage(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
