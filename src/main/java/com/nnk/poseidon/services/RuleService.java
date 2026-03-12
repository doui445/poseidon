package com.nnk.poseidon.services;

import com.nnk.poseidon.domain.Rule;
import com.nnk.poseidon.domain.dto.RuleRequest;

import java.util.List;
import java.util.Optional;

public interface RuleService {

    List<Rule> getRules();

    Optional<Rule> getRuleById(Integer id);

    Rule saveRule(RuleRequest request);

    Rule updateRule(Integer id, RuleRequest request);

    void deleteRuleById(Integer id);

    RuleRequest getByIdAsRequest(Integer id);
}
