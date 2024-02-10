package com.github.alerson.domain.repository;

import com.github.alerson.domain.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ClienteRepository {

    private static String INSERT = "INSERT INTO CLIENTE (nome) VALUES (?)";
    private static String SELECT_ALL = "SELECT * FROM CLIENTE";
    private static String UPDATE = "UPDATE CLIENTE SET nome = ? WHERE id = ?";
    private static String DELETE = "DELETE FROM CLIENTE WHERE id = ?";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Cliente salvar(Cliente cliente) {
        jdbcTemplate.update(INSERT, new Object[]{cliente.getNome()});
        return cliente;
    }

    public Cliente atualizar(Cliente cliente) {
        jdbcTemplate.update(UPDATE, new Object[]{cliente.getNome(), cliente.getId()});
        return cliente;
    }

    public void deletar(Cliente cliente) {
        deletar(cliente.getId());
    }

    public void deletar(Integer id) {
        jdbcTemplate.update(DELETE, new Object[]{id});
    }

    public List<Cliente> buscarPorNome(String nome) {
        return jdbcTemplate.query(SELECT_ALL.concat(" WHERE nome like ?"), new Object[]{"%" + nome + "%"}, getRowMapperCliente());
    }

    public List<Cliente> listar() {
        return jdbcTemplate.query(SELECT_ALL, getRowMapperCliente());
    }

    private static RowMapper<Cliente> getRowMapperCliente() {
        return new RowMapper<Cliente>() {
            @Override
            public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Cliente(rs.getInt("id"), rs.getString("nome"));
            }
        };
    }
}