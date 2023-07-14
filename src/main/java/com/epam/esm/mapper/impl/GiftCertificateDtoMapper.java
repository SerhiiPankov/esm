package com.epam.esm.mapper.impl;

import com.epam.esm.dto.GiftCertificateRequestDto;
import com.epam.esm.dto.GiftCertificateResponseDto;
import com.epam.esm.dto.TagRequestDto;
import com.epam.esm.dto.TagResponseDto;
import com.epam.esm.mapper.DtoMapper;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GiftCertificateDtoMapper
        implements DtoMapper<GiftCertificate, GiftCertificateRequestDto, GiftCertificateResponseDto> {

    private final DtoMapper<Tag, TagRequestDto, TagResponseDto> tagDtoMapper;

    public GiftCertificateDtoMapper(DtoMapper<Tag, TagRequestDto, TagResponseDto> tagDtoMapper) {
        this.tagDtoMapper = tagDtoMapper;
    }

    @Override
    public GiftCertificate mapToModel(GiftCertificateRequestDto dto) {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName(dto.getName());
        giftCertificate.setDescription(dto.getDescription());
        giftCertificate.setPrice(dto.getPrice());
        giftCertificate.setDuration(dto.getDuration());
        giftCertificate.setCreateDate(dto.getCreateDate());
        giftCertificate.setLastUpdateDate(dto.getLastUpdateDate());



        return null;
    }

    @Override
    public GiftCertificateResponseDto mapToDto(GiftCertificate giftCertificate) {
        GiftCertificateResponseDto giftCertificateResponseDto = new GiftCertificateResponseDto();
        giftCertificateResponseDto.setId(giftCertificate.getId());
        giftCertificateResponseDto.setName(giftCertificate.getName());
        giftCertificateResponseDto.setDescription(giftCertificate.getDescription());
        giftCertificateResponseDto.setPrice(giftCertificate.getPrice());
        giftCertificateResponseDto.setDuration(giftCertificate.getDuration());
        giftCertificateResponseDto.setCreateDate(giftCertificate.getCreateDate());
        giftCertificateResponseDto.setLastUpdateDate(giftCertificate.getLastUpdateDate());
        giftCertificateResponseDto.setTags(giftCertificate.getTags().stream()
                .map(tagDtoMapper::mapToDto)
                .collect(Collectors.toList()));
        return giftCertificateResponseDto;
    }
}
