package com.kon.budget.controller;

import com.kon.budget.service.AssetsService;
import com.kon.budget.service.dtos.AssetDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.Arrays.asList;

@RestController
@RequestMapping("/assets")
public class AssetsController {

    private AssetsService assetsService;

    public AssetsController(AssetsService assetsService) {
        this.assetsService = assetsService;
    }

    @GetMapping
    public List<AssetDto> getAssets() {
        return  assetsService.getAllAssets();
    }

    @PostMapping()
    public void setAsset(@RequestBody AssetDto dto) {
        assetsService.setAsset(dto);
    }

    @DeleteMapping
    public void deleteAsset(@RequestBody AssetDto dto) {
        assetsService.deleteAsset(dto);
    }

    @PutMapping
    public void updateAsset(@RequestBody AssetDto dto) {
        assetsService.updateAsset(dto);
    }

}


