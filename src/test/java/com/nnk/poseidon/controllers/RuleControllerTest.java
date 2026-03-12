package com.nnk.poseidon.controllers;

import com.nnk.poseidon.configuration.SpringSecurityConfig;
import com.nnk.poseidon.domain.dto.RuleRequest;
import com.nnk.poseidon.services.RuleService;
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

@WebMvcTest(RuleController.class)
@Import(SpringSecurityConfig.class)
@WithMockUser
public class RuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RuleService ruleService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @DisplayName("GET /rule/list - Should return list view")
    void rulesPage_shouldRenderRuleListView() throws Exception {
        mockMvc.perform(get("/rule/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("rule/list"))
                .andExpect(model().attributeExists("rules"));
    }

    @Test
    @DisplayName("GET /rule/add - Should show add form with empty request")
    void getAddRule_shouldShowForm() throws Exception {
        mockMvc.perform(get("/rule/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("rule/add"))
                .andExpect(model().attributeExists("rule"));
    }

    @Test
    @DisplayName("GET /rule/update/{id} - Should show update form with prefilled request")
    void getUpdateRule_shouldShowForm() throws Exception {
        RuleRequest request = new RuleRequest(1, "NameTest", "DescriptionTest", "JsonTest", "TemplateTest", "SQLTest", "SQLPartTest");
        given(ruleService.getByIdAsRequest(1)).willReturn(request);
        mockMvc.perform(get("/rule/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("rule/update"))
                .andExpect(model().attribute("rule", request));
    }

    @Test
    @DisplayName("POST /rule/validate - Success")
    void postValidate_shouldSaveRuleAndRedirectToList() throws Exception {
        mockMvc.perform(post("/rule/validate")
                        .with(csrf())
                        .param("name", "NameTest")
                        .param("description", "DescriptionTest")
                        .param("json", "JsonTest")
                        .param("template", "TemplateTest")
                        .param("sql", "SQLTest")
                        .param("sqlPart", "SQLPartTest"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rule/list"));
        verify(ruleService).saveRule(any(RuleRequest.class));
    }

    @Test
    @DisplayName("POST /rule/validate - Failure")
    void postValidate_shouldReturnToForm() throws Exception {
        mockMvc.perform(post("/rule/validate")
                        .with(csrf())
                        .param("name", "NameTest")
                        .param("description", "")
                        .param("json", "JsonTest")
                        .param("template", "")
                        .param("sql", "SQLTest")
                        .param("sqlPart", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("rule/add"))
                .andExpect(model().hasErrors());
        verify(ruleService, never()).saveRule(any(RuleRequest.class));
    }

    @Test
    @DisplayName("POST /rule/update/{id} - Success")
    void postUpdateRule_shouldUpdateRuleAndRedirectToList() throws Exception {
        mockMvc.perform(post("/rule/update/1")
                        .with(csrf())
                        .param("name", "UpdatedName")
                        .param("description", "DescriptionTest")
                        .param("json", "UpdatedJson")
                        .param("template", "TemplateTest")
                        .param("sql", "UpdatedSQL")
                        .param("sqlPart", "SQLPartTest"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rule/list"));
        verify(ruleService).updateRule(anyInt(), any(RuleRequest.class));
    }

    @Test
    @DisplayName("GET /rule/delete/{id} - Success")
    void getDeleteRule_shouldDeleteRuleAndRedirectToList() throws Exception {
        mockMvc.perform(get("/rule/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rule/list"));
        verify(ruleService).deleteRuleById(anyInt());
    }
}
