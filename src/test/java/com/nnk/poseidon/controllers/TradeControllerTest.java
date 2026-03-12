package com.nnk.poseidon.controllers;

import com.nnk.poseidon.configuration.SpringSecurityConfig;
import com.nnk.poseidon.domain.dto.TradeRequest;
import com.nnk.poseidon.services.TradeService;
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

@WebMvcTest(TradeController.class)
@Import(SpringSecurityConfig.class)
@WithMockUser
public class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TradeService tradeService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @DisplayName("GET /trade/list - Should return list view")
    void tradesPage_shouldRenderTradeListView() throws Exception {
        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(model().attributeExists("trades"));
    }

    @Test
    @DisplayName("GET /trade/add - Should show add form with empty request")
    void getAddTrade_shouldShowForm() throws Exception {
        mockMvc.perform(get("/trade/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"))
                .andExpect(model().attributeExists("trade"));
    }

    @Test
    @DisplayName("GET /trade/update/{id} - Should show update form with prefilled request")
    void getUpdateTrade_shouldShowForm() throws Exception {
        TradeRequest request = new TradeRequest(1, "AccountTest", "TypeTest", 10.0);
        given(tradeService.getByIdAsRequest(1)).willReturn(request);
        mockMvc.perform(get("/trade/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attribute("trade", request));
    }

    @Test
    @DisplayName("POST /trade/validate - Success")
    void postValidate_shouldSaveTradeAndRedirectToList() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .with(csrf())
                        .param("account", "AccountTest")
                        .param("type", "TypeTest")
                        .param("buyQuantity", "10.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
        verify(tradeService).saveTrade(any(TradeRequest.class));
    }

    @Test
    @DisplayName("POST /trade/validate - Failure")
    void postValidate_shouldReturnToForm() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .with(csrf())
                        .param("account", "")
                        .param("type", "TypeTest")
                        .param("buyQuantity", "-10.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"))
                .andExpect(model().hasErrors());
        verify(tradeService, never()).saveTrade(any(TradeRequest.class));
    }

    @Test
    @DisplayName("POST /trade/update/{id} - Success")
    void postUpdateTrade_shouldUpdateTradeAndRedirectToList() throws Exception {
        mockMvc.perform(post("/trade/update/1")
                        .with(csrf())
                        .param("account", "UpdatedAccount")
                        .param("type", "UpdatedType")
                        .param("buyQuantity", "20.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
        verify(tradeService).updateTrade(anyInt(), any(TradeRequest.class));
    }

    @Test
    @DisplayName("GET /trade/delete/{id} - Success")
    void getDeleteTrade_shouldDeleteTradeAndRedirectToList() throws Exception {
        mockMvc.perform(get("/trade/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
        verify(tradeService).deleteTradeById(anyInt());
    }
}
