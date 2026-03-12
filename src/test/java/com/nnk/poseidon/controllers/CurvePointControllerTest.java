package com.nnk.poseidon.controllers;

import com.nnk.poseidon.configuration.SpringSecurityConfig;
import com.nnk.poseidon.domain.dto.CurvePointRequest;
import com.nnk.poseidon.services.CurvePointService;
import com.nnk.poseidon.services.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurvePointController.class)
@Import({SpringSecurityConfig.class})
@WithMockUser
class CurvePointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CurvePointService curvePointService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @DisplayName("GET /curvepoint/list - Should return list view")
    void curvePointsPage_shouldRenderCurvePointListView() throws Exception {
        mockMvc.perform(get("/curvepoint/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvepoint/list"))
                .andExpect(model().attributeExists("curvePoints"));
    }

    @Test
    @DisplayName("GET /curvepoint/add - Should show add form with empty request")
    void getAddCurvePoint_shouldShowForm() throws Exception {
        mockMvc.perform(get("/curvepoint/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvepoint/add"))
                .andExpect(model().attributeExists("curvePoint"));
    }

    @Test
    @DisplayName("GET /curvepoint/update/{id} - Should show update form with prefilled request")
    void getUpdateCurvePoint_shouldShowForm() throws Exception {
        CurvePointRequest request = new CurvePointRequest(1, 1, 10.0, 10.0);
        given(curvePointService.getByIdAsRequest(1)).willReturn(request);
        mockMvc.perform(get("/curvepoint/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvepoint/update"))
                .andExpect(model().attribute("curvePoint", request));
    }

    @Test
    @DisplayName("POST /curvepoint/validate - Success")
    void postValidate_shouldSaveCurvePointAndRedirectToList() throws Exception {
        mockMvc.perform(post("/curvepoint/validate")
                        .with(csrf())
                        .param("curveId", "1")
                        .param("term", "10.0")
                        .param("value", "10.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvepoint/list"));
        verify(curvePointService).saveCurvePoint(any(CurvePointRequest.class));
    }

    @Test
    @DisplayName("POST /curvepoint/validate - Failure")
    void postValidate_shouldReturnToForm() throws Exception {
        mockMvc.perform(post("/curvepoint/validate")
                        .with(csrf())
                        .param("curveId", "1")
                        .param("term", "-10.0")
                        .param("value", "-10.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvepoint/add"))
                .andExpect(model().hasErrors());
        verify(curvePointService, never()).saveCurvePoint(any(CurvePointRequest.class));
    }

    @Test
    @DisplayName("POST /curvepoint/update/{id} - Success")
    void postUpdateCurvePoint_shouldUpdateCurvePointAndRedirectToList() throws Exception {
        mockMvc.perform(post("/curvepoint/update/1")
                        .with(csrf())
                        .param("curveId", "1")
                        .param("term", "20.0")
                        .param("value", "20.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvepoint/list"));
        verify(curvePointService).updateCurvePoint(anyInt(), any(CurvePointRequest.class));
    }

    @Test
    @DisplayName("GET /curvepoint/delete/{id} - Success")
    void getDeleteCurvePoint_shouldDeleteCurvePointAndRedirectToList() throws Exception {
        mockMvc.perform(get("/curvepoint/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvepoint/list"));
        verify(curvePointService).deleteCurvePointById(anyInt());
    }
}