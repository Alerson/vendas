package com.github.alerson.domain.repository;

import com.github.alerson.domain.entity.Cliente;
import com.github.alerson.domain.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    Set<Pedido> findByCliente(Cliente cliente);

}
