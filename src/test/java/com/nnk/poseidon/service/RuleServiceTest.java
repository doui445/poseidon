package com.nnk.poseidon.service;

import com.nnk.poseidon.domain.Rule;
import com.nnk.poseidon.domain.dto.RuleRequest;
import com.nnk.poseidon.repositories.RuleRepository;
import com.nnk.poseidon.services.RuleServiceImpl;
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

public class RuleServiceTest {
    @Mock
    private RuleRepository ruleRepository;

    @InjectMocks
    private RuleServiceImpl ruleService;

    private AutoCloseable mocks;

    private Rule rule;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        rule = Rule.builder()
                .id(1)
                .name("myRule")
                .description("blablabla")
                .json("...")
                .template("...")
                .sqlStr("SQL")
                .sqlPart("SQL")
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    @DisplayName("getRuleById should return a rule when rule exists")
    void testGetRuleByIdFound() {
        given(ruleRepository.findById(1)).willReturn(Optional.of(rule));

        Optional<Rule> result = ruleService.getRuleById(1);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("myRule");
        verify(ruleRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("getRuleById should return empty when rule does not exist")
    void testGetRuleByIdNotFound() {
        given(ruleRepository.findById(2)).willReturn(Optional.empty());

        Optional<Rule> result = ruleService.getRuleById(2);

        assertThat(result).isEmpty();
        verify(ruleRepository, times(1)).findById(2);
    }

    @Test
    @DisplayName("saveRule should save and return the rule")
    void testSaveRule() {
        given(ruleRepository.save(any())).willReturn(rule);

        RuleRequest request = new RuleRequest(rule.getId(), rule.getName(), rule.getDescription(), rule.getJson(), rule.getTemplate(), rule.getSqlStr(), rule.getSqlPart());
        Rule savedRule = ruleService.saveRule(request);

        assertThat(savedRule).isNotNull();
        assertThat(savedRule.getName()).isEqualTo("myRule");
        verify(ruleRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("updateRule should update and return the rule")
    void testUpdateRule() {
        Rule newRule = rule;
        newRule.setName("myNewRule");

        given(ruleRepository.save(rule)).willReturn(newRule);
        given(ruleRepository.findById(1)).willReturn(Optional.of(rule));

        RuleRequest request = new RuleRequest(rule.getId(), "myNewRule", rule.getDescription(), rule.getJson(), rule.getTemplate(), rule.getSqlStr(), rule.getSqlPart());
        Rule savedRule = ruleService.updateRule(1, request);

        assertThat(savedRule).isNotNull();
        assertThat(savedRule.getName()).isEqualTo("myNewRule");
        verify(ruleRepository, times(1)).save(rule);
    }

    @Test
    @DisplayName("deleteRuleById should delete rule by id")
    void testDeleteRuleByIdById() {
        given(ruleRepository.findById(1)).willReturn(Optional.of(rule));

        ruleService.deleteRuleById(1);

        verify(ruleRepository, times(1)).delete(rule);
    }
}
