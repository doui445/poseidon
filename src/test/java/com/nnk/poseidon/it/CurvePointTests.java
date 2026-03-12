package com.nnk.poseidon.it;

import com.nnk.poseidon.domain.CurvePoint;
import com.nnk.poseidon.repositories.CurvePointRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CurvePointTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurvePointRepository curvePointRepository;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should display the curvePoint list page with updated data from database")
    public void curvePointIntegrationTest() throws Exception {
        CurvePoint curvePoint = CurvePoint.builder()
                .curveId(10)
                .term(10.0)
                .value(30.0)
                .build();

        // Save
        curvePoint = curvePointRepository.save(curvePoint);
        assertNotNull(curvePoint.getId());
        assertEquals(10, (int) curvePoint.getCurveId());

        // Update
        curvePoint.setCurveId(20);
        curvePoint = curvePointRepository.save(curvePoint);
        assertEquals(20, (int) curvePoint.getCurveId());

        mockMvc.perform(get("/curvepoint/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvepoint/list"))
                .andExpect(model().attributeExists("curvePoints"))
                .andExpect(content().string(containsString("20")))
                .andExpect(content().string(containsString("30")));

        // Find
        List<CurvePoint> listResult = curvePointRepository.findAll();
        assertFalse(listResult.isEmpty());

        // Delete
        Integer id = curvePoint.getId();
        curvePointRepository.delete(curvePoint);
        Optional<CurvePoint> curvePointList = curvePointRepository.findById(id);
        assertFalse(curvePointList.isPresent());
    }
}
