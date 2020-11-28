package com.amboucheba.seriesTemporellesTpWeb.resources;

import com.amboucheba.seriesTemporellesTpWeb.models.Partage;
import com.amboucheba.seriesTemporellesTpWeb.repositories.PartageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/partages")
public class PartageResource {

    @Autowired
    PartageRepository partageRepository;

    @GetMapping
    public ResponseEntity getAll(){

        List<Partage> partages = StreamSupport.stream(partageRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        return ResponseEntity.ok(partages);
    }
}
