package com.nnk.poseidon.it;

import com.nnk.poseidon.domain.Rule;
import com.nnk.poseidon.repositories.RuleRepository;
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
public class RuleTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RuleRepository ruleRepository;

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Should display the rule list page with updated data from database")
    public void ruleIntegrationTest() throws Exception {
        Rule rule = Rule.builder()
                .name("Integration Rule Name")
                .description("Description")
                .json("Json")
                .template("Template")
                .sqlStr("SQL")
                .sqlPart("SQL Part")
                .build();

        // Save
        rule = ruleRepository.save(rule);
        assertNotNull(rule.getId());
        assertEquals("Integration Rule Name", rule.getName());

        // Update
        rule.setName("Rule Name Update");
        rule = ruleRepository.save(rule);
        assertEquals("Rule Name Update", rule.getName());

        mockMvc.perform(get("/rule/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("rule/list"))
                .andExpect(model().attributeExists("rules"))
                .andExpect(content().string(containsString("Rule Name Update")))
                .andExpect(content().string(containsString("SQL Part")));

        // Find
        List<Rule> listResult = ruleRepository.findAll();
        assertFalse(listResult.isEmpty());

        // Delete
        Integer id = rule.getId();
        ruleRepository.delete(rule);
        Optional<Rule> optionalRule = ruleRepository.findById(id);
        assertFalse(optionalRule.isPresent());
    }
}
