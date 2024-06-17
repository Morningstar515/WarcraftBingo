package com.WarcraftBingo.DatabaseFunctions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DatabaseController {

    private final Data repository;

    @Autowired
    public DatabaseController(Data repository) {
        this.repository = repository;
    }

    @GetMapping("/getTiles")
    public List<Database> getTiles() {
        return repository.findAll();
    }

    @GetMapping("/getMC")
    public List<String> getMoltenCore() {
        return repository.getMoltenCore();
    }

    @GetMapping("/getBWL")
    public List<String> getBlackwingLair() {
        return repository.getBlackwingLair();
    }

    @GetMapping("/getZG")
    public List<String> getZulGurub() {
        return repository.getZulGurub();
    }

}
