package com.thehecklers.sburrestdemo;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class SburRestDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SburRestDemoApplication.class, args);
    }

}

interface CoffeeRepository extends CrudRepository<Coffee, String> {
    @Transactional
    void deleteById(String id);
}

@RestController
@RequestMapping("/coffees")
@CrossOrigin
class RestApiDemoController {
    private final CoffeeRepository coffeeRepository;

    public RestApiDemoController(CoffeeRepository coffeeRepository) {
        this.coffeeRepository = coffeeRepository;

        if (this.coffeeRepository.count() == 0) {
            this.coffeeRepository.saveAll(List.of(
                    new Coffee("Café Cereza"),
                    new Coffee("Café Ganador"),
                    new Coffee("Café Lareño"),
                    new Coffee("Café Três Pontas")
            ));
        }
    }

    @GetMapping
    Iterable<Coffee> getCoffees() {
        return coffeeRepository.findAll();
    }

    @GetMapping("/{id}")
    Optional<Coffee> getCoffeeById(@PathVariable String id) {
        return coffeeRepository.findById(id);
    }

    @PostMapping
    Coffee postCoffee(@RequestBody Coffee coffee) {
        return coffeeRepository.save(coffee);
    }

    @PutMapping("/{id}")
    ResponseEntity<Coffee> putCoffee(@PathVariable String id, @RequestBody Coffee coffee) {
        // Força o ID da URL para dentro do objeto para o JPA saber que é um UPDATE
        coffee.setId(id);

        // O save() faz o Update automaticamente se o ID existir no banco
        return (coffeeRepository.existsById(id))
                ? new ResponseEntity<>(coffeeRepository.save(coffee), HttpStatus.OK)
                : new ResponseEntity<>(coffeeRepository.save(coffee), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    void deleteCoffee(@PathVariable String id) {
        // Simples e direto: se existe, apaga.
        if (coffeeRepository.existsById(id)) {
            coffeeRepository.deleteById(id);
        }
    }
}

@Entity
class Coffee {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // O JPA agora gera o UUID automaticamente!
    private String id;

    private String name;

    // O JPA exige um construtor vazio
    public Coffee() {}

    public Coffee(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Como o JPA vai cuidar do ID, este construtor pode só receber o nome
    public Coffee(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}