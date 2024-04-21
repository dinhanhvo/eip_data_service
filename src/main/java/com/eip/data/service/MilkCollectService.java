package com.eip.data.service;

import com.eip.data.entity.MilkCollect;
import com.eip.data.repositories.IMilkCollectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MilkCollectService {

    @Autowired
    IMilkCollectRepository milkCollectRepository;

    public List<MilkCollect> getMilkCollects() {
        return  milkCollectRepository.findAll();
    }

    public MilkCollect getMilkCollectById(Long id) {
        return  milkCollectRepository.findById(id).orElse(null);
    }

    public List<MilkCollect> getMilkCollectsByStatus(String status) {
        List<MilkCollect> mls = milkCollectRepository.findByMqttStatus(status);
        return  milkCollectRepository.findAll();
    }

    public MilkCollect updateStatusMilkCollectById(Long id, String status) {
        MilkCollect milkCollect = milkCollectRepository.findById(id).orElse(null);
        if (milkCollect != null) {
            milkCollect.setMqttStatus(status);
            milkCollectRepository.save(milkCollect);
        }
        return  milkCollect;
    }
}
