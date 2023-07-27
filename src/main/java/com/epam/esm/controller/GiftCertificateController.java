package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateRequestDto;
import com.epam.esm.dto.GiftCertificateResponseDto;
import com.epam.esm.mapper.DtoMapper;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {
    private final GiftCertificateService certificateService;
    private final DtoMapper<GiftCertificate,
            GiftCertificateRequestDto, GiftCertificateResponseDto> dtoMapper;

    public GiftCertificateController(GiftCertificateService certificateService,
                                     DtoMapper<GiftCertificate, GiftCertificateRequestDto,
                                             GiftCertificateResponseDto> dtoMapper) {
        this.certificateService = certificateService;
        this.dtoMapper = dtoMapper;
    }

    @PostMapping
    public GiftCertificateResponseDto create(@RequestBody GiftCertificateRequestDto requestDto)
            throws ReflectiveOperationException {
        GiftCertificate giftCertificate =
                certificateService.create(dtoMapper.mapToModel(requestDto));
        return dtoMapper.mapToDto(giftCertificate);
    }

    @PutMapping("/{id}")
    public GiftCertificateResponseDto update(@PathVariable BigInteger id,
                                             @RequestBody GiftCertificateRequestDto requestDto)
            throws ReflectiveOperationException {
        GiftCertificate giftCertificate = dtoMapper.mapToModel(requestDto);
        giftCertificate.setId(id);
        return dtoMapper.mapToDto(certificateService.update(giftCertificate));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable BigInteger id) {
        certificateService.delete(id);
    }

    @GetMapping("/{id}")
    public GiftCertificateResponseDto get(@PathVariable BigInteger id) {
        return dtoMapper.mapToDto(certificateService.get(id));
    }

    @GetMapping
    public List<GiftCertificateResponseDto> getAll(@RequestParam Map<String, String> params) {
        return certificateService.getAllByParameters(params).stream()
                .map(dtoMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
