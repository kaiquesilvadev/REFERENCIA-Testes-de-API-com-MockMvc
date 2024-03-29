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
	private Long idExistenteAlex , idInexistente , idExistenteMaria ;

	@BeforeEach
	void setUp() throws Exception {
		// token
		adminToken = tokenUtil.obtainAccessToken(mockMvc, "alex@gmail.com", "123456");
		ClienteToken = tokenUtil.obtainAccessToken(mockMvc, "maria@gmail.com", "123456");
		
		//var
		idExistenteMaria = 1L;
		idExistenteAlex = 2L ;
		idInexistente = 1000L ;
	}
	
	@DisplayName("Busca de pedido por id retorna pedido existente quando logado como admin")
	@Test
	public void findByIdDeveRetornaPedidoQuandoExistente() throws Exception {
		
	   ResultActions resultado = mockMvc.perform(get("/orders/{id}" , idExistenteAlex)
					.header("Authorization", "Bearer " + adminToken)
					.accept(MediaType.APPLICATION_PROBLEM_JSON));
	   
	   resultado.andExpect(status().isOk());
	   resultado.andExpect(jsonPath("$.id").value(idExistenteAlex));
	}
	
		@DisplayName("Busca de pedido por id retorna pedido existente quando logado como cliente e o pedido pertence ao usuário")
		@Test
		public void findByIdDeveRetornarPedidoQuandoLogadoComoClienteEPedidoPertencerAOUsuario() throws Exception {
		
			 ResultActions resultado = mockMvc.perform(get("/orders/{id}" , idExistenteMaria)
				    .header("Authorization", "Bearer " +  ClienteToken)
					.accept(MediaType.APPLICATION_JSON));
			
			   resultado.andExpect(status().isOk());
			   resultado.andExpect(jsonPath("$.id").value(idExistenteMaria));
		}
		
		@DisplayName("Busca de pedido por id retorna 403 quando pedido não pertence ao usuário (com perfil de cliente)")
		@Test
		public void findByIdDeveRetornar403QuandoPedidoNaoPertenceAOUsuarioLogadoComoCliente() throws Exception {
		
			 ResultActions resultado = mockMvc.perform(get("/orders/{id}" , idExistenteAlex)
					.header("Authorization", "Bearer " +  ClienteToken)
					.accept(MediaType.APPLICATION_JSON));
			
			   resultado.andExpect(status().isForbidden());
		}
		
		@DisplayName("Busca de pedido por id retorna 401 quando não logado como admin ou cliente")
		@Test
		public void findByIdDeveRetornar401QuandoNaoLogado() throws Exception {
		
			 ResultActions resultado = mockMvc.perform(get("/orders/{id}" , idExistenteAlex)
					.accept(MediaType.APPLICATION_JSON));
			
			   resultado.andExpect(status().isUnauthorized());
		}
		
		@DisplayName("Busca de pedido por id retorna 404 para pedido inexistente quando logado como admin")
		@Test
		public void findByIdDeveRetornar404QuandoPedidoInexistenteLogadoComoAdmin() throws Exception {
		
			 ResultActions resultado = mockMvc.perform(get("/orders/{id}" , idInexistente)
					.header("Authorization", "Bearer " + adminToken)
					.accept(MediaType.APPLICATION_JSON));
			
			   resultado.andExpect(status().isNotFound());
		}
		
		@DisplayName("Busca de pedido por id retorna 404 para pedido inexistente quando logado como cliente")
		@Test
		public void findByIdDeveRetornar404QuandoPedidoInexistenteLogadoComoCliente() throws Exception {
		
			 ResultActions resultado = mockMvc.perform(get("/orders/{id}" , idInexistente)
					.header("Authorization", "Bearer " + ClienteToken)
					.accept(MediaType.APPLICATION_JSON));
			
			   resultado.andExpect(status().isNotFound());
		}
}
