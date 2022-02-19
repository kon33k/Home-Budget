package com.kon.budget.controller;

import com.kon.budget.enums.AssetCategory;
import com.kon.budget.service.AssetsService;
import com.kon.budget.service.dtos.AssetDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assets")
@AllArgsConstructor
public class AssetsController {

    private AssetsService assetsService;

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

    @GetMapping("/find")
    public List<AssetDto> getAllAssetsByCategory(@PathParam("category") String category) {
        return assetsService.getAssetsByCategory(AssetCategory.valueOf(category.toUpperCase()));
    }

    @GetMapping("/filter")
    public List<AssetDto> getFilteredAssets(@RequestParam Map<String, String> filter) {
        return assetsService.getFilteredAssets(filter);
    }

}


