package com.devsuperior.dscommerce.TI;

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

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerTI {

	@Autowired
	private MockMvc mockMvc;
	private String productNome;
	
	@BeforeEach
	void setUp() throws Exception {
		productNome ="Macbook";
		 
	}
	
	@DisplayName("get Deve Retorna Macbook Quando Status For Ok")
	@Test
	public void getDeveRetornaMacbookQuandoStatusForOk() throws Exception {
		
		   ResultActions resultado = mockMvc
				   .perform(get("/products?size=12&page=0&name={productNome}" , productNome)
						   .accept(MediaType.APPLICATION_JSON));
		   
		   resultado.andExpect(status().isOk());
		   resultado.andExpect(jsonPath("$.content[0].id").value(3L));
		   resultado.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
		   resultado.andExpect(jsonPath("$.content[0].price").value( 1250.0));
		   resultado.andExpect(jsonPath("$.content[0].imgUrl")
				   .value( "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/3-big.jpg"));
	}
 }
