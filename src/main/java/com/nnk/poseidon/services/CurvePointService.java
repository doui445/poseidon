package com.nnk.poseidon.services;

import com.nnk.poseidon.domain.CurvePoint;
import com.nnk.poseidon.domain.dto.CurvePointRequest;

import java.util.List;
import java.util.Optional;

public interface CurvePointService {
    
    List<CurvePoint> getCurvePoints();

    Optional<CurvePoint> getCurvePointById(Integer id);

    CurvePoint saveCurvePoint(CurvePointRequest curvePoint);

    CurvePoint updateCurvePoint(Integer id, CurvePointRequest curvePointRequest);

    void deleteCurvePointById(Integer id);

    CurvePointRequest getByIdAsRequest(Integer id);
}
