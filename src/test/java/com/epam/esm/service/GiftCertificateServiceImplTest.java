package com.epam.esm.service;

import com.epam.esm.exception.DataProcessingException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.specification.PaginationAndSortingHandler;
import com.epam.esm.specification.SpecificationManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;


class GiftCertificateServiceImplTest {
    private static final String NAME = "TestCertificateName";
    private static final String DESCRIPTION = "TestCertificateDescription";
    private static final BigDecimal PRICE = BigDecimal.valueOf(99.99);
    private static final Integer DURATION = 3;
    private static final LocalDateTime CREATE_DATE_TIME = LocalDateTime.now();
    private static final BigInteger ID = BigInteger.valueOf(1L);
    private static final BigInteger TAG_ID = BigInteger.valueOf(1L);
    private static final String TAG_NAME = "TestTagName";
    private static Tag tag;
    private static GiftCertificate rawGiftCertificate;
    private static GiftCertificate addedGiftCertificate;

    private TagRepository tagRepository;
    private SpecificationManager specificationManager;
    private PaginationAndSortingHandler paginationAndSortingHandler;
    private GiftCertificateRepository giftCertificateRepository;
    private GiftCertificateService giftCertificateService;


    static {
        tag = new Tag();
        tag.setId(TAG_ID);
        tag.setName(TAG_NAME);

        rawGiftCertificate = new GiftCertificate();
        rawGiftCertificate.setName(NAME);
        rawGiftCertificate.setDescription(DESCRIPTION);
        rawGiftCertificate.setPrice(PRICE);
        rawGiftCertificate.setDuration(DURATION);
        rawGiftCertificate.setTags(List.of(tag));

        addedGiftCertificate = new GiftCertificate();
        addedGiftCertificate.setId(ID);
        addedGiftCertificate.setName(NAME);
        addedGiftCertificate.setDescription(DESCRIPTION);
        addedGiftCertificate.setPrice(PRICE);
        addedGiftCertificate.setDuration(DURATION);
        addedGiftCertificate.setCreateDate(CREATE_DATE_TIME);
        addedGiftCertificate.setLastUpdateDate(CREATE_DATE_TIME);
        addedGiftCertificate.setTags(List.of(tag));
    }

    @BeforeEach
    void beforeEach() {
        giftCertificateRepository = mock(GiftCertificateRepository.class);
        tagRepository = mock(TagRepository.class);
        specificationManager = mock(SpecificationManager.class);
        paginationAndSortingHandler = spy(PaginationAndSortingHandler.class);
        giftCertificateService = new GiftCertificateServiceImpl(
                giftCertificateRepository, tagRepository, specificationManager, paginationAndSortingHandler);
    }

    @Test
    void create() {
        when(giftCertificateRepository.create(rawGiftCertificate)).thenReturn(addedGiftCertificate);
        GiftCertificate giftCertificate = giftCertificateService.create(rawGiftCertificate);
        Assertions.assertEquals(addedGiftCertificate, giftCertificate);
        verify(giftCertificateRepository, atLeastOnce()).create(rawGiftCertificate);
        verify(giftCertificateRepository, atLeastOnce()).addTags(rawGiftCertificate);
    }

    @Test
    void update_Ok() {
        when(giftCertificateRepository.update(addedGiftCertificate)).thenReturn(1);
        when(giftCertificateRepository.get(ID))
                .thenReturn(Optional.of(addedGiftCertificate));
        when(tagRepository.getTagsByGiftCertificateId(ID))
                .thenReturn(List.of(tag));
        GiftCertificate updatedGiftCertificate = giftCertificateService.update(addedGiftCertificate);
        Assertions.assertEquals(addedGiftCertificate, updatedGiftCertificate);
        verify(giftCertificateRepository, atLeastOnce()).addTags(addedGiftCertificate);
        verify(giftCertificateRepository, atLeastOnce()).update(addedGiftCertificate);
        verify(giftCertificateRepository, atLeastOnce()).get(ID);
        verify(tagRepository, atLeastOnce()).getTagsByGiftCertificateId(ID);
    }

    @Test
    void update_DidNotUpdate_NotOk() {
        when(giftCertificateRepository.update(addedGiftCertificate)).thenReturn(0);
        Assertions.assertThrows(DataProcessingException.class,
                () -> giftCertificateService.update(addedGiftCertificate));
        verify(giftCertificateRepository, atLeastOnce()).addTags(addedGiftCertificate);
        verify(giftCertificateRepository, atLeastOnce()).update(addedGiftCertificate);
        verify(giftCertificateRepository, never()).get(ID);
        verify(tagRepository, never()).getTagsByGiftCertificateId(ID);
    }

    @Test
    void update_CanNotGetCertificate_NotOk() {
        when(giftCertificateRepository.update(addedGiftCertificate)).thenReturn(1);
        when(giftCertificateRepository.get(ID))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(DataProcessingException.class,
                () -> giftCertificateService.update(addedGiftCertificate));
        verify(giftCertificateRepository, atLeastOnce()).addTags(addedGiftCertificate);
        verify(giftCertificateRepository, atLeastOnce()).update(addedGiftCertificate);
        verify(giftCertificateRepository, atLeastOnce()).get(ID);
        verify(tagRepository, never()).getTagsByGiftCertificateId(ID);
    }

    @Test
    void delete() {
        giftCertificateService.delete(ID);
        verify(tagRepository, atLeastOnce()).deleteTagsByGiftCertificateId(ID);
        verify(giftCertificateRepository, atLeastOnce()).delete(ID);
    }

    @Test
    void get_Ok() {
        when(giftCertificateRepository.get(ID))
                .thenReturn(Optional.of(addedGiftCertificate));
        when(tagRepository.getTagsByGiftCertificateId(ID))
                .thenReturn(List.of(tag));
        GiftCertificate giftCertificateFromDb = giftCertificateService.get(ID);
        Assertions.assertEquals(addedGiftCertificate, giftCertificateFromDb);
        verify(giftCertificateRepository, atLeastOnce()).get(ID);
        verify(tagRepository, atLeastOnce()).getTagsByGiftCertificateId(ID);
    }

    @Test
    void get_CanNotGetCertificate_NotOk() {
        when(giftCertificateRepository.get(ID))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(DataProcessingException.class,
                () -> giftCertificateService.get(ID));
        verify(giftCertificateRepository, atLeastOnce()).get(ID);
        verify(tagRepository, never()).getTagsByGiftCertificateId(ID);
    }

    @Test
    void getAllByParameters_Ok() {
        when(specificationManager.get("nameTag", new String[] {"tag"}))
                .thenReturn(" t.name IN ('tag') ");
        when(specificationManager.get("partNameOrDesc", new String[] {"part"}))
                .thenReturn("gc.description LIKE '%part%'");
        when(giftCertificateRepository.getAllByParameters(" WHERE gc.description LIKE '%part%' AND  t.name IN ('tag')   LIMIT 0, 6"))
                .thenReturn(List.of(addedGiftCertificate));
        when(tagRepository.getTagsByGiftCertificateId(ID))
                .thenReturn(List.of(tag));
        List<GiftCertificate> allByParameters = giftCertificateService.getAllByParameters(getParams());
        Assertions.assertIterableEquals(List.of(addedGiftCertificate), allByParameters);
    }
    
    private Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("nameTag", "tag");
        params.put("partNameOrDesc", "part");
        return params;
    }
}