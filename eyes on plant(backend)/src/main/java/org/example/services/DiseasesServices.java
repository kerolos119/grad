package org.example.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.document.Diseases;
import org.example.dto.DiseasesDto;
import org.example.dto.PageResult;
import org.example.mapper.DiseasesMapper;
import org.example.repo.DiseaseRepository;
import org.example.repo.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DiseasesServices {
    private final UserRepository userRepository;
    private final DiseaseRepository repository;
    private final DiseasesMapper mapper;

    public DiseasesDto create(DiseasesDto dto) {
        Diseases diseases = mapper.toEntity(dto);
        Diseases savedDisease = repository.save(diseases);
        return mapper.toDto(savedDisease);
    }

    public void delete(int id) {
        Diseases diseases = repository.findById(id)
                        .orElseThrow(()-> new EntityNotFoundException("Diseases not found"));
        repository.deleteById(id);
    }

    public PageResult<DiseasesDto> search(String dName, Pageable pageable) {
        Specification<Diseases> spec=(root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (dName != null){
                predicates.add(cb.like(root.get("name"),"%" + dName + "%"));
            }
            return cb.and(predicates.toArray(predicates.toArray(new Predicate[0])));
        };
        Page<Diseases> diseases = repository.findAll(spec,pageable);

        List<DiseasesDto> diseasesDtos = diseases.getContent()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        return new PageResult<>(
                diseasesDtos,
                diseases.getTotalElements(),
                diseases.getTotalPages(),
                diseases.getNumber() +1,
                diseases.getSize()
        );
    }
}
