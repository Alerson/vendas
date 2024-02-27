package com.github.alerson.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.alerson.domain.entity.Produto;
import com.github.alerson.domain.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProdutoController.class)
public class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoRepository produtoRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void getProdutoById_whenProdutoExists() throws Exception {
        Produto produto = new Produto();
        produto.setId(1);
        produto.setDescricao("xicara");
        produto.setPreco(new BigDecimal("10.00"));

        given(produtoRepository.findById(1)).willReturn(Optional.of(produto));

        mockMvc.perform(get("/api/produtos/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("xicara"))
                .andExpect(jsonPath("$.preco").value(10));
    }

    @Test
    void saveProduto_andReturnProduto() throws Exception {
        Produto produto = new Produto();
        produto.setDescricao("xicara");
        produto.setPreco(new BigDecimal("10.00"));

        given(produtoRepository.save(produto)).willReturn(produto);

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produto)))
                .andExpect(status().isCreated());
    }


    @Test
    void deleteProduto_whenProdutoExists() throws Exception {
        Produto produto = new Produto();
        produto.setId(1);

        given(produtoRepository.findById(1)).willReturn(Optional.of(produto));

        mockMvc.perform(delete("/api/produtos/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateProduto_whenProdutoExists() throws Exception {
        Produto existingProduto = new Produto();
        existingProduto.setId(1);
        existingProduto.setDescricao("xicara");
        existingProduto.setPreco(new BigDecimal("10.00"));

        Produto updatedProduto = new Produto();
        updatedProduto.setId(1);
        updatedProduto.setDescricao("caneca");
        updatedProduto.setPreco(new BigDecimal("15.00"));

        given(produtoRepository.findById(1)).willReturn(Optional.of(existingProduto));
        given(produtoRepository.save(updatedProduto)).willReturn(updatedProduto);

        mockMvc.perform(put("/api/produtos/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduto)))
                .andExpect(status().isNoContent());
    }

    @Test
    void findProdutos_matchingCriteria() throws Exception {
        Produto produto = new Produto();
        produto.setDescricao("caneca");

        given(produtoRepository.findAll(Example.of(produto, ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING))))
                .willReturn(Arrays.asList(produto));

        mockMvc.perform(get("/api/produtos") // Assuming `descricao` is a query parameter
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
