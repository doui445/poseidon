package com.nnk.poseidon.services;

import com.nnk.poseidon.domain.Rule;
import com.nnk.poseidon.domain.dto.RuleRequest;
import com.nnk.poseidon.repositories.RuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RuleServiceImpl implements RuleService {

    private final RuleRepository ruleRepository;

    @Override
    public List<Rule> getRules() {
        return ruleRepository.findAll();
    }

    @Override
    public Optional<Rule> getRuleById(Integer id) {
        return ruleRepository.findById(id);
    }

    @Override
    public Rule saveRule(RuleRequest request) {
        if (request.id() != null && ruleRepository.findById(request.id()).isPresent()) {
            throw new IllegalArgumentException("Rule already exist");
        }
        Rule rule = new Rule();
        rule.setName(request.name());
        rule.setDescription(request.description());
        rule.setJson(request.json());
        rule.setTemplate(request.template());
        rule.setSqlStr(request.sql());
        rule.setSqlPart(request.sqlPart());
        return ruleRepository.save(rule);
    }

    @Override
    public Rule updateRule(Integer id, RuleRequest request) {
        Rule rule = ruleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Rule Id:" + id));
        rule.setName(request.name());
        rule.setDescription(request.description());
        rule.setJson(request.json());
        rule.setTemplate(request.template());
        rule.setSqlStr(request.sql());
        rule.setSqlPart(request.sqlPart());
        return ruleRepository.save(rule);
    }

    @Override
    public void deleteRuleById(Integer id) {
        Rule rule = ruleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Rule Id:" + id));
        ruleRepository.delete(rule);
    }

    @Override
    public RuleRequest getByIdAsRequest(Integer id) {
        Rule rule = ruleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Rule Id:" + id));
        return new RuleRequest(
                rule.getId(),
                rule.getName(),
                rule.getDescription(),
                rule.getJson(),
                rule.getTemplate(),
                rule.getSqlStr(),
                rule.getSqlPart()
        );
    }
}
