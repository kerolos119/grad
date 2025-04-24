package org.example.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.example.document.Diseases;
import org.example.dto.DiseaseDto;
import org.example.dto.PageResult;
import org.example.dto.UsersDto;
import org.example.exception.DiseaseException;
import org.example.exception.UserException;
import org.example.mapper.DiseasesMapper;
import org.example.mapper.UserMapper;
import org.example.repo.DiseaseRepository;
import org.example.repo.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DiseasesServices {
    private final DiseaseRepository repository;
    private final DiseasesMapper mapper;

    public DiseaseDto create(DiseaseDto dto) {
        Diseases diseases = mapper.toEntity(dto);
        Diseases savedDisease = repository.save(diseases);
        return mapper.toDto(savedDisease);
    }


    public Page<DiseaseDto> search(String dName, Pageable pageable) {
        Specification<Diseases> spec = buildSearchSpec(dName);
        return repository.findAll(spec,pageable)
                .map(mapper::toDto);
    }

        private Specification<Diseases> buildSearchSpec(String dName){
            return ((root, query, cb) ->{
                List<Predicate> predicates = new ArrayList<>();

                if ((dName != null && !dName.isEmpty())){
                    predicates.add(cb.like(
                            cb.lower(root.get("name")),
                            "%" + dName.toLowerCase() + "%"
                    ));
                }
                return cb.and(predicates.toArray(new Predicate[0]));
            } );
        }


    }
