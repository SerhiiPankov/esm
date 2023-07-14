package com.epam.esm.controller;


import com.epam.esm.dto.GiftCertificateRequestDto;
import com.epam.esm.dto.GiftCertificateResponseDto;
import com.epam.esm.mapper.DtoMapper;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {
    private final GiftCertificateService certificateService;
    private final DtoMapper<GiftCertificate, GiftCertificateRequestDto, GiftCertificateResponseDto> dtoMapper;


    public GiftCertificateController(GiftCertificateService certificateService,
                                     DtoMapper<GiftCertificate, GiftCertificateRequestDto,
                                             GiftCertificateResponseDto> dtoMapper) {
        this.certificateService = certificateService;
        this.dtoMapper = dtoMapper;
    }
}
