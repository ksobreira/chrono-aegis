package com.chronoaegis.inventario_service.service;

import com.chronoaegis.inventario_service.dto.ArmaDTO;
import com.chronoaegis.inventario_service.model.Arma;
import com.chronoaegis.inventario_service.repository.ArmaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArmaService {

    private final ArmaRepository armaRepository;

    public ArmaService(ArmaRepository armaRepository) {
        this.armaRepository = armaRepository;
    }

    public Arma criar(ArmaDTO dto){
        Arma arma = new Arma();
        arma.setNome(dto.getNome());
        arma.setDanoBase(dto.getDanoBase());
        arma.setForcaMin(dto.getForcaMinima());
        arma.setTags(dto.getTags());
        return armaRepository.save(arma);
    }

    public Arma buscarPorId(long id){
        return armaRepository.findById(id).orElseThrow(() -> new RuntimeException("Arma não encontrada"));
    }

    public List<Arma> listarTodas(){
        return armaRepository.findAll();
    }
}
