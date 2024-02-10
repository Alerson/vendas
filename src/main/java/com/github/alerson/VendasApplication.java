package com.github.alerson;

import com.github.alerson.domain.entity.Cliente;
import com.github.alerson.domain.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class VendasApplication {
    @Bean
    public CommandLineRunner run(@Autowired ClienteRepository clienteRepository){
        return args -> {
            clienteRepository.salvar(new Cliente("Alerson"));
            clienteRepository.salvar(new Cliente("Marcela"));

            clienteRepository.listar().stream().forEach(System.out::println);

            clienteRepository.listar().stream().forEach(cliente -> {
                cliente.setNome(cliente.getNome() + " atualizado");
                clienteRepository.atualizar(cliente);
            });

            clienteRepository.listar().stream().forEach(System.out::println);

            clienteRepository.buscarPorNome("Mar").stream().forEach(System.out::println);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(VendasApplication.class, args);
    }

}
