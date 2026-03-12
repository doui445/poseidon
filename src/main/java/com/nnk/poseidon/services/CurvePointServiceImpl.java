package com.nnk.poseidon.services;

import com.nnk.poseidon.domain.CurvePoint;
import com.nnk.poseidon.domain.dto.CurvePointRequest;
import com.nnk.poseidon.repositories.CurvePointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CurvePointServiceImpl implements CurvePointService {

    private final CurvePointRepository curvePointRepository;

    @Override
    public List<CurvePoint> getCurvePoints() {
        return curvePointRepository.findAll();
    }

    @Override
    public Optional<CurvePoint> getCurvePointById(Integer id) {
        return curvePointRepository.findById(id);
    }

    @Override
    public CurvePoint saveCurvePoint(CurvePointRequest request) {
        if (request.id() != null && curvePointRepository.findById(request.id()).isPresent()) {
            throw new IllegalArgumentException("CurvePoint already exist");
        }
        CurvePoint curvePoint = new CurvePoint();
        curvePoint.setCurveId(request.curveId());
        curvePoint.setTerm(request.term());
        curvePoint.setValue(request.value());
        curvePoint.setCreationDate(Timestamp.valueOf(LocalDateTime.now()));
        curvePoint.setAsOfDate(curvePoint.getCreationDate());
        return curvePointRepository.save(curvePoint);
    }

    @Override
    public CurvePoint updateCurvePoint(Integer id, CurvePointRequest request) {
        CurvePoint curvePoint = curvePointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid CurvePoint Id:" + id));
        curvePoint.setCurveId(request.curveId());
        curvePoint.setTerm(request.term());
        curvePoint.setValue(request.value());
        curvePoint.setAsOfDate(Timestamp.valueOf(LocalDateTime.now()));
        return curvePointRepository.save(curvePoint);
    }

    @Override
    public void deleteCurvePointById(Integer id) {
        CurvePoint curvePoint = curvePointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid CurvePoint Id:" + id));
        curvePointRepository.delete(curvePoint);
    }

    @Override
    public CurvePointRequest getByIdAsRequest(Integer id) {
        CurvePoint curvePoint = curvePointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid CurvePoint Id:" + id));
        return new CurvePointRequest(
                curvePoint.getId(),
                curvePoint.getCurveId(),
                curvePoint.getTerm(),
                curvePoint.getValue()
        );
    }
}
