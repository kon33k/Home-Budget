package com.kon.budget.controller;

import com.kon.budget.service.AssetsService;
import com.kon.budget.service.dtos.AssetDto;
import com.kon.budget.service.dtos.AssetsDto;
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
    public AssetsDto getAssets() {
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

}


