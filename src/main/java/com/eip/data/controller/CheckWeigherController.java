package com.eip.data.controller;

import com.eip.data.entity.MilkCollect;
import com.eip.data.service.MilkCollectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class CheckWeigherController {

    @Autowired
    MilkCollectService milkCollectService;

    @GetMapping("/milk")
    public ResponseEntity<List<MilkCollect>> getMilkCollect() {
        List<MilkCollect> response = milkCollectService.getMilkCollects();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/milk/id/{id}")
    public ResponseEntity<MilkCollect> getMilkCollectById(@PathVariable("id") Long id) {
        MilkCollect response = milkCollectService.getMilkCollectById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/milk_status")
    public ResponseEntity<List<MilkCollect>> getMilkCollectByStatus(@RequestParam("status") String status) {
        List<MilkCollect> response = milkCollectService.getMilkCollectsByStatus(status);
        return ResponseEntity.ok(response);
    }
}
