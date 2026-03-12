package com.nnk.poseidon.service;

import com.nnk.poseidon.domain.CurvePoint;
import com.nnk.poseidon.domain.dto.CurvePointRequest;
import com.nnk.poseidon.repositories.CurvePointRepository;
import com.nnk.poseidon.services.CurvePointServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CurvePointServiceTest {
    
    @Mock
    private CurvePointRepository curvePointRepository;
    
    @InjectMocks
    private CurvePointServiceImpl curvePointService;
    
    private AutoCloseable mocks;
    
    private CurvePoint curvePoint;
    
    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        
        curvePoint = CurvePoint.builder()
                .id(1)
                .curveId(1)
                .term(10.0)
                .value(5.0)
                .build();
    }
    
    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }
    
    @Test
    @DisplayName("getCurvePointById should return a curvePoint when curvePoint exists")
    void testGetCurvePointByIdFound() {
        given(curvePointRepository.findById(1)).willReturn(Optional.of(curvePoint));
        
        Optional<CurvePoint> result = curvePointService.getCurvePointById(1);

        assertThat(result).isPresent();
        assertThat(result.get().getCurveId()).isEqualTo(1);
        verify(curvePointRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("getCurvePointById should return empty when curvePoint does not exist")
    void testGetCurvePointByIdNotFound() {
        given(curvePointRepository.findById(2)).willReturn(Optional.empty());

        Optional<CurvePoint> result = curvePointService.getCurvePointById(2);

        assertThat(result).isEmpty();
        verify(curvePointRepository, times(1)).findById(2);
    }

    @Test
    @DisplayName("saveCurvePoint should save and return the curvePoint")
    void testSaveCurvePoint() {
        given(curvePointRepository.save(any())).willReturn(curvePoint);

        CurvePointRequest request = new CurvePointRequest(curvePoint.getId(), curvePoint.getCurveId(), curvePoint.getTerm(), curvePoint.getValue());
        CurvePoint savedCurvePoint = curvePointService.saveCurvePoint(request);

        assertThat(savedCurvePoint).isNotNull();
        assertThat(savedCurvePoint.getCurveId()).isEqualTo(1);
        verify(curvePointRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("updateCurvePoint should update and return the curvePoint")
    void testUpdateCurvePoint() {
        CurvePoint newCurvePoint = curvePoint;
        newCurvePoint.setCurveId(2);

        given(curvePointRepository.save(curvePoint)).willReturn(newCurvePoint);
        given(curvePointRepository.findById(1)).willReturn(Optional.of(curvePoint));

        CurvePointRequest request = new CurvePointRequest(curvePoint.getId(), 2, curvePoint.getTerm(), curvePoint.getValue());
        CurvePoint savedCurvePoint = curvePointService.updateCurvePoint(1, request);

        assertThat(savedCurvePoint).isNotNull();
        assertThat(savedCurvePoint.getCurveId()).isEqualTo(2);
        verify(curvePointRepository, times(1)).save(curvePoint);
    }

    @Test
    @DisplayName("deleteCurvePointById should delete curvePoint by id")
    void testDeleteCurvePointByIdById() {
        given(curvePointRepository.findById(1)).willReturn(Optional.of(curvePoint));

        curvePointService.deleteCurvePointById(1);

        verify(curvePointRepository, times(1)).delete(curvePoint);
    }
}
