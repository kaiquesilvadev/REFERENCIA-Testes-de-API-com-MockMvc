package com.devsuperior.dscommerce.TI;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.fabricaOBJ.criaProduct;
import com.devsuperior.dscommerce.util.TokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerTI {

	@Autowired
	private MockMvc mockMvc;
	private String productNome;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private TokenUtil tokenUtil;

	private String ClienteToken, adminToken, ivalidoToken;
	private Product product;
	private ProductDTO productDTO;

	@BeforeEach
	void setUp() throws Exception {
		// token
		adminToken = tokenUtil.obtainAccessToken(mockMvc, "alex@gmail.com", "123456");
		ClienteToken = tokenUtil.obtainAccessToken(mockMvc, "maria@gmail.com", "123456");
		ivalidoToken = adminToken + "wedw";
		
		//var
		product = criaProduct.novoProduct();
		productNome = "Macbook";

	}

	@DisplayName("get Deve Retorna Macbook Quando Status For Ok")
	@Test
	public void getDeveRetornaMacbookQuandoStatusForOk() throws Exception {

		ResultActions resultado = mockMvc.perform(
				get("/products?size=12&page=0&name={productNome}", productNome).accept(MediaType.APPLICATION_JSON));

		resultado.andExpect(status().isOk());
		resultado.andExpect(jsonPath("$.content[0].id").value(3L));
		resultado.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
		resultado.andExpect(jsonPath("$.content[0].price").value(1250.0));
		resultado.andExpect(jsonPath("$.content[0].imgUrl").value(
				"https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/3-big.jpg"));
	}

	@DisplayName("Inserção de produto insere produto com dados válidos quando logado como admin")
	@Test
	public void insertDEProductInsereProdutoComDadosValidosQuandoLogadoComoAdmin() throws Exception {

		productDTO = new ProductDTO(product);
		String body = objectMapper.writeValueAsString(productDTO);

		 ResultActions resultado = mockMvc.perform(
				 post("/products")
				.header("Authorization", "Bearer " + adminToken)
				.content(body)
				.contentType(MediaType.APPLICATION_JSON) 
				.accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print());

		 resultado.andExpect(status().isCreated());
		 resultado.andExpect(jsonPath("$.id").value(26L));
		 resultado.andExpect(jsonPath("$.name").isNotEmpty());
		 resultado.andExpect(jsonPath("$.description").isNotEmpty());
		 resultado.andExpect(jsonPath("$.price").isNotEmpty());
		 
	}

	@DisplayName("Inserção de produto retorna 422 e mensagens customizadas com dados inválidos quando logado como admin e campo name for inválido")
	@Test
	public void insertDeProductDeveRetorna422QuandoNomeForInvalidoELogadoComoAdmin() throws Exception {
		
		product.setName("");
		productDTO = new ProductDTO(product);
		String body = objectMapper.writeValueAsString(productDTO);
		
		 ResultActions resultado = mockMvc.perform(post("/products")
				.header("Authorization", "Bearer " + adminToken)
				.content(body)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		 resultado.andExpect(status().isUnprocessableEntity());
	}
	
	@DisplayName("Inserção de produto retorna 422 e mensagens customizadas com dados inválidos quando logado como admin e campo description for inválido")
	@Test
	public void insertDeProductDeveRetorna422QuandoDescriptionForInvalidoELogadoComoAdmin() throws Exception {
		
		product.setDescription("");
		productDTO = new ProductDTO(product);
		String body = objectMapper.writeValueAsString(productDTO);
		
		 ResultActions resultado = mockMvc.perform(post("/products")
				.header("Authorization", "Bearer " + adminToken)
				.content(body)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		 resultado.andExpect(status().isUnprocessableEntity());
	}

	@DisplayName("Inserção de produto retorna 422 e mensagens customizadas com dados inválidos quando logado como admin e campo price for negativo")
	@Test
	public void insertDeProductDeveRetorna422QuandoPriceForNegativoELogadoComoAdmin() throws Exception {
		
		product.setPrice(-200.00);
		productDTO = new ProductDTO(product);
		String body = objectMapper.writeValueAsString(productDTO);
		
		 ResultActions resultado = mockMvc.perform(post("/products")
				.header("Authorization", "Bearer " + adminToken)
				.content(body)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		 resultado.andExpect(status().isUnprocessableEntity());
	}

	@DisplayName("Inserção de produto retorna 422 e mensagens customizadas com dados inválidos quando logado como admin e campo price for zero")
	@Test
	public void insertDeProductDeveRetorna422QuandoPriceForZeroELogadoComoAdmin() throws Exception {
		
		product.setPrice(0.0);
		productDTO = new ProductDTO(product);
		String body = objectMapper.writeValueAsString(productDTO);
		
		 ResultActions resultado = mockMvc.perform(post("/products")
				.header("Authorization", "Bearer " + adminToken)
				.content(body)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		 resultado.andExpect(status().isUnprocessableEntity());
	}
	//

	@DisplayName("Inserção de produto retorna 422 e mensagens customizadas com dados inválidos quando logado como admin e não tiver categoria associada")
	@Test
	public void insertDeProductDeveRetorna422QuandoNaoTiverCategoriaAssociadaELogadoComoAdmin() throws Exception {
		
		product.getCategories().clear();
		productDTO = new ProductDTO(product);
		String body = objectMapper.writeValueAsString(productDTO);
		
		 ResultActions resultado = mockMvc.perform(post("/products")
				.header("Authorization", "Bearer " + adminToken)
				.content(body)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		 resultado.andExpect(status().isUnprocessableEntity());
	}
	//Inserção de produto retorna 403 quando logado como cliente
	@DisplayName("Inserção de produto retorna 403 quando logado como cliente")
	@Test
	public void insertDeProductDeveRetorna403QuandologadoComoCliente() throws Exception {
		
		productDTO = new ProductDTO(product);
		String body = objectMapper.writeValueAsString(productDTO);
		
		 ResultActions resultado = mockMvc.perform(post("/products")
				.header("Authorization", "Bearer " + ClienteToken)
				.content(body)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		 resultado.andExpect(status().isForbidden());
	}
}
