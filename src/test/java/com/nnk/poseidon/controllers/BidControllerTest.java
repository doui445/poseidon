package com.nnk.poseidon.controllers;

import com.nnk.poseidon.configuration.SpringSecurityConfig;
import com.nnk.poseidon.domain.dto.BidRequest;
import com.nnk.poseidon.services.BidService;
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

@WebMvcTest(BidController.class)
@Import(SpringSecurityConfig.class)
@WithMockUser
class BidControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BidService bidService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @DisplayName("GET /bid/list - Should return list view")
    void bidsPage_shouldRenderBidListView() throws Exception {
        mockMvc.perform(get("/bid/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("bid/list"))
                .andExpect(model().attributeExists("bids"));
    }

    @Test
    @DisplayName("GET /bid/add - Should show add form with empty request")
    void getAddBid_shouldShowForm() throws Exception {
        mockMvc.perform(get("/bid/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("bid/add"))
                .andExpect(model().attributeExists("bid"));
    }

    @Test
    @DisplayName("GET /bid/update/{id} - Should show update form with prefilled request")
    void getUpdateBid_shouldShowForm() throws Exception {
        BidRequest request = new BidRequest(1, "AccountTest", "TypeTest", 10.0);
        given(bidService.getByIdAsRequest(1)).willReturn(request);
        mockMvc.perform(get("/bid/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bid/update"))
                .andExpect(model().attribute("bid", request));
    }

    @Test
    @DisplayName("POST /bid/validate - Success")
    void postValidate_shouldSaveBidAndRedirectToList() throws Exception {
        mockMvc.perform(post("/bid/validate")
                        .with(csrf())
                        .param("account", "AccountTest")
                        .param("type", "TypeTest")
                        .param("bidQuantity", "10.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bid/list"));
        verify(bidService).saveBid(any(BidRequest.class));
    }

    @Test
    @DisplayName("POST /bid/validate - Failure")
    void postValidate_shouldReturnToForm() throws Exception {
        mockMvc.perform(post("/bid/validate")
                        .with(csrf())
                        .param("account", "")
                        .param("type", "TypeTest")
                        .param("bidQuantity", "-10.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("bid/add"))
                .andExpect(model().hasErrors());
        verify(bidService, never()).saveBid(any(BidRequest.class));
    }

    @Test
    @DisplayName("POST /bid/update/{id} - Success")
    void postUpdateBid_shouldUpdateBidAndRedirectToList() throws Exception {
        mockMvc.perform(post("/bid/update/1")
                        .with(csrf())
                        .param("account", "UpdatedAccount")
                        .param("type", "UpdatedType")
                        .param("bidQuantity", "20.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bid/list"));
        verify(bidService).updateBid(anyInt(), any(BidRequest.class));
    }

    @Test
    @DisplayName("GET /bid/delete/{id} - Success")
    void getDeleteBid_shouldDeleteBidAndRedirectToList() throws Exception {
        mockMvc.perform(get("/bid/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bid/list"));
        verify(bidService).deleteBidById(anyInt());
    }
}