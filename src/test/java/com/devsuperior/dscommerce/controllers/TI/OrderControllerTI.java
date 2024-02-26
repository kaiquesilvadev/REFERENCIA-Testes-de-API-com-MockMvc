package com.devsuperior.dscommerce.controllers.TI;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscommerce.util.TokenUtil;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerTI {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TokenUtil tokenUtil;

	private String ClienteToken, adminToken;
	private Long idExistente , idInexistente ;

	@BeforeEach
	void setUp() throws Exception {
		// token
		adminToken = tokenUtil.obtainAccessToken(mockMvc, "alex@gmail.com", "123456");
		ClienteToken = tokenUtil.obtainAccessToken(mockMvc, "maria@gmail.com", "123456");
		
		//var
		idExistente = 3L ;
		idInexistente = 1000L ;
	}
	
	@DisplayName("Busca de pedido por id retorna pedido existente quando logado como admin")
	@Test
	public void findByIdDeveRetornaPedidoQuandoExistente() throws Exception {
		
	   ResultActions resultado = mockMvc.perform(get("/orders/{id}" , idExistente)
				.header("Authorization", "Bearer " + adminToken)
				.accept(MediaType.APPLICATION_PROBLEM_JSON));
	   
	   resultado.andExpect(status().isOk());
	   resultado.andExpect(jsonPath("$.id").value(idExistente));
	}
}
