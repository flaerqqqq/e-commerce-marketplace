package com.example.ecommercemarketplace.controllers;


import com.example.ecommercemarketplace.dto.*;
import com.example.ecommercemarketplace.services.MerchantService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/merchants")
@AllArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

    @GetMapping("/{id}")
    public MerchantResponseDto getMerchantByMerchantPublicId(@PathVariable("id") String id){
        MerchantDto merchant = merchantService.findMerchantByPublicId(id);
        MerchantResponseDto merchantResponseDto = new MerchantResponseDto();

        BeanUtils.copyProperties(merchant, merchantResponseDto);

        return merchantResponseDto;
    }

    @GetMapping
    public Page<MerchantResponseDto> getAllMerchants(Pageable pageable){
        Page<MerchantResponseDto> page = merchantService.findAllMerchants(pageable).map(merchantDto ->
        {
            MerchantResponseDto merchantResponseDto = new MerchantResponseDto();
            BeanUtils.copyProperties(merchantDto, merchantResponseDto);
            return merchantResponseDto;
        });

        return page;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MerchantUpdateResponseDto updateMerchantFully(@RequestBody @Valid MerchantUpdateRequestDto merchantUpdateRequestDto,
                                                 @PathVariable("id") String id){
        MerchantDto merchantDto = new MerchantDto();
        BeanUtils.copyProperties(merchantUpdateRequestDto, merchantDto);

        MerchantDto updatedMerchant = merchantService.updateMerchantFully(id, merchantDto);

        MerchantUpdateResponseDto merchantUpdateResponseDto = new MerchantUpdateResponseDto();
        BeanUtils.copyProperties(updatedMerchant, merchantUpdateResponseDto);

        return merchantUpdateResponseDto;
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MerchantUpdateResponseDto updateMerchantPatch(@RequestBody @Valid MerchantPatchUpdateRequestDto merchantPatchUpdateRequestDto,
                                                 @PathVariable("id") String id){
        MerchantDto merchantDto = new MerchantDto();
        BeanUtils.copyProperties(merchantPatchUpdateRequestDto, merchantDto);

        MerchantDto updatedMerchant = merchantService.updateMerchantPatch(id, merchantDto);

        MerchantUpdateResponseDto merchantUpdateResponseDto = new MerchantUpdateResponseDto();
        BeanUtils.copyProperties(updatedMerchant, merchantUpdateResponseDto);

        return merchantUpdateResponseDto;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMerchant(@PathVariable("id") String id){
        merchantService.removeMerchantByPublicId(id);
    }
}
