package br.com.fiap.locatech.locatech;

import br.com.fiap.locatech.locatech.controllers.handlers.ControllerExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LocatechApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void servesVersionedApiRoutes() throws Exception {
		mockMvc.perform(get("/v1/veiculos").param("page", "1").param("size", "10"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/v2/veiculos").param("page", "1").param("size", "10"))
				.andExpect(status().isOk());
	}

	@Test
	void returnsStandardNotFoundResponse() throws Exception {
		mockMvc.perform(get("/v1/pessoas/9999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Pessoa não encontrada"))
				.andExpect(jsonPath("$.status").value(404));
	}

	@Test
	void returnsValidationMessagesForInvalidRentalDates() throws Exception {
		mockMvc.perform(post("/v1/alugueis")
						.contentType("application/json")
						.content("""
								{"pessoaId":1,"veiculoId":1,"dataInicio":null,"dataFim":"2024-10-15"}
								"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasItem(containsString("data de início"))))
				.andExpect(jsonPath("$.status").value(400));
	}

	@Test
	void rejectsRentalWhenEndDateIsNotAfterStartDate() throws Exception {
		mockMvc.perform(post("/v1/alugueis")
						.contentType("application/json")
						.content("""
								{"pessoaId":1,"veiculoId":1,"dataInicio":"2024-10-15","dataFim":"2024-10-15"}
								"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasItem(containsString("A data de fim deve ser posterior"))));
	}

	@Test
	void rejectsOverlappingRentalWithConflictResponse() throws Exception {
		mockMvc.perform(post("/v1/alugueis")
						.contentType("application/json")
						.content("""
								{"pessoaId":1,"veiculoId":1,"dataInicio":"2024-10-10","dataFim":"2024-10-20"}
								"""))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.message").value("Veículo não disponível para o período informado"))
				.andExpect(jsonPath("$.status").value(409));
	}

	@Test
	void rejectsCpfWithInvalidCheckDigits() throws Exception {
		mockMvc.perform(post("/v1/pessoas")
						.contentType("application/json")
						.content("""
								{"nome":"Teste","cpf":"00000000000","telefone":"11999999999","email":"teste@example.com"}
								"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasItem(containsString("CPF inválido"))));
	}

	@Test
	void acceptsCpfWithValidCheckDigits() throws Exception {
		mockMvc.perform(post("/v1/pessoas")
						.contentType("application/json")
						.content("""
								{"nome":"Pessoa Teste","cpf":"52998224725","telefone":"11999999999","email":"teste@example.com"}
								"""))
				.andExpect(status().isCreated());
	}

	@Test
	void rejectsVehicleWithNonPositiveDailyRate() throws Exception {
		mockMvc.perform(post("/v1/veiculos")
						.contentType("application/json")
						.content("""
								{"marca":"Teste","modelo":"Teste","placa":"ABC1234","ano":2020,"cor":"azul","valorDiaria":0}
								"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400));
	}

	@Test
	void servesOpenApiDocumentationForVersionedRoutes() throws Exception {
		mockMvc.perform(get("/v3/api-docs"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.paths['/v1/alugueis']").exists())
				.andExpect(jsonPath("$.paths['/v2/alugueis']").exists());
	}

	@Test
	void returnsStandardBadRequestForMalformedJson() throws Exception {
		mockMvc.perform(post("/v1/pessoas")
						.contentType("application/json")
						.content("{"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Requisição inválida ou parâmetro obrigatório ausente"))
				.andExpect(jsonPath("$.status").value(400));
	}

	@Test
	void unexpectedErrorsReturnGenericInternalServerError() {
		var response = new ControllerExceptionHandler()
				.handlerUnexpectedException(new IllegalStateException("internal detail"));

		assertEquals(500, response.getStatusCode().value());
		assertEquals("Erro interno do servidor", response.getBody().message());
	}
}
