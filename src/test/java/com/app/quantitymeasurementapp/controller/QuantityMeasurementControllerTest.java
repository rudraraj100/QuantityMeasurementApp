package com.app.quantitymeasurementapp.controller;

import com.app.quantitymeasurementapp.dto.QuantityDTO;
import com.app.quantitymeasurementapp.dto.QuantityInputDTO;
import com.app.quantitymeasurementapp.dto.QuantityMeasurementDTO;
import com.app.quantitymeasurementapp.exception.GlobalExceptionHandler;
import com.app.quantitymeasurementapp.exception.QuantityMeasurementException;
import com.app.quantitymeasurementapp.service.IQuantityMeasurementService;
import com.app.quantitymeasurementapp.user.Role;
import com.app.quantitymeasurementapp.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuantityMeasurementController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class QuantityMeasurementControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private IQuantityMeasurementService service;

	private QuantityDTO feetDto;
	private QuantityDTO inchesDto;
	private QuantityDTO kilogramDto;
	private QuantityInputDTO lengthAddInput;
	private QuantityMeasurementDTO addResult;
	private QuantityMeasurementDTO compareResult;
	private QuantityMeasurementDTO convertResult;
	private QuantityMeasurementDTO divideResult;
	private QuantityMeasurementDTO subtractResult;
	private User testUser;

	@BeforeEach
	void setUp() {
		feetDto = new QuantityDTO(1.0, "FEET", "LengthUnit");
		inchesDto = new QuantityDTO(12.0, "INCHES", "LengthUnit");
		kilogramDto = new QuantityDTO(1.0, "KILOGRAM", "WeightUnit");
		lengthAddInput = new QuantityInputDTO(feetDto, inchesDto);

		addResult = new QuantityMeasurementDTO();
		addResult.setThisValue(1.0);
		addResult.setThisUnit("FEET");
		addResult.setThisMeasurementType("LengthUnit");
		addResult.setThatValue(12.0);
		addResult.setThatUnit("INCHES");
		addResult.setThatMeasurementType("LengthUnit");
		addResult.setOperation("add");
		addResult.setResultValue(2.0);
		addResult.setResultUnit("FEET");
		addResult.setResultMeasurementType("LengthUnit");
		addResult.setError(false);

		compareResult = new QuantityMeasurementDTO();
		compareResult.setThisValue(1.0);
		compareResult.setThisUnit("FEET");
		compareResult.setThisMeasurementType("LengthUnit");
		compareResult.setThatValue(12.0);
		compareResult.setThatUnit("INCHES");
		compareResult.setThatMeasurementType("LengthUnit");
		compareResult.setOperation("compare");
		compareResult.setResultString("true");
		compareResult.setError(false);

		convertResult = new QuantityMeasurementDTO();
		convertResult.setThisValue(1.0);
		convertResult.setThisUnit("FEET");
		convertResult.setThisMeasurementType("LengthUnit");
		convertResult.setOperation("convert");
		convertResult.setResultValue(12.0);
		convertResult.setResultUnit("INCHES");
		convertResult.setError(false);

		divideResult = new QuantityMeasurementDTO();
		divideResult.setThisValue(10.0);
		divideResult.setThisUnit("FEET");
		divideResult.setThisMeasurementType("LengthUnit");
		divideResult.setThatValue(2.0);
		divideResult.setThatUnit("FEET");
		divideResult.setThatMeasurementType("LengthUnit");
		divideResult.setOperation("divide");
		divideResult.setResultValue(5.0);
		divideResult.setResultUnit("RATIO");
		divideResult.setError(false);

		subtractResult = new QuantityMeasurementDTO();
		subtractResult.setThisValue(4.0);
		subtractResult.setThisUnit("FEET");
		subtractResult.setThisMeasurementType("LengthUnit");
		subtractResult.setThatValue(24.0);
		subtractResult.setThatUnit("INCHES");
		subtractResult.setThatMeasurementType("LengthUnit");
		subtractResult.setOperation("subtract");
		subtractResult.setResultValue(2.0);
		subtractResult.setResultUnit("FEET");
		subtractResult.setError(false);

		testUser = User.builder().id(7L).name("Test User").email("test@example.com").role(Role.USER).active(true)
				.build();
	}

	private UsernamePasswordAuthenticationToken authPrincipal() {
		return new UsernamePasswordAuthenticationToken(testUser, null, List.of());
	}

	@Test
	@DisplayName("POST /add — 1 FEET + 12 INCHES = 2 FEET  →  200 OK")
	void testAdd_LengthUnits_ReturnsCorrectResult() throws Exception {
		Mockito.when(service.add(org.mockito.ArgumentMatchers.any(QuantityDTO.class),
				org.mockito.ArgumentMatchers.any(QuantityDTO.class), org.mockito.ArgumentMatchers.isNull(),
				org.mockito.ArgumentMatchers.isNull())).thenReturn(addResult);

		mockMvc.perform(post("/api/v1/quantities/add").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(lengthAddInput))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.operation", is("add"))).andExpect(jsonPath("$.resultValue", is(2.0)))
				.andExpect(jsonPath("$.resultUnit", is("FEET"))).andExpect(jsonPath("$.error", is(false)));
	}

	@Test
	@DisplayName("POST /add — missing request body  →  400 Bad Request")
	void testAdd_MissingBody_Returns400() throws Exception {
		mockMvc.perform(post("/api/v1/quantities/add").contentType(MediaType.APPLICATION_JSON).content("{}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("POST /add — incompatible units (LENGTH + WEIGHT)  →  400")
	void testAdd_IncompatibleUnits_Returns400() throws Exception {
		QuantityInputDTO incompatible = new QuantityInputDTO(feetDto, kilogramDto);

		Mockito.when(service.add(any(), any(), isNull(), isNull())).thenThrow(new QuantityMeasurementException(
				"add Error: Cannot perform arithmetic between different measurement categories: LengthUnit and WeightUnit"));

		mockMvc.perform(post("/api/v1/quantities/add").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(incompatible))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status", is(400)))
				.andExpect(jsonPath("$.message", containsString("Cannot perform arithmetic")));
	}

	@Test
	@DisplayName("POST /subtract — 4 FEET - 24 INCHES = 2 FEET  →  200 OK")
	void testSubtract_ReturnsCorrectResult() throws Exception {
		QuantityInputDTO input = new QuantityInputDTO(new QuantityDTO(4.0, "FEET", "LengthUnit"),
				new QuantityDTO(24.0, "INCHES", "LengthUnit"));

		Mockito.when(service.subtract(any(), any(), isNull(), isNull())).thenReturn(subtractResult);

		mockMvc.perform(post("/api/v1/quantities/subtract").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(input))).andExpect(status().isOk())
				.andExpect(jsonPath("$.resultValue", is(2.0))).andExpect(jsonPath("$.resultUnit", is("FEET")));
	}

	@Test
	@DisplayName("POST /divide — 10 FEET / 2 FEET = 5.0  →  200 OK")
	void testDivide_ReturnsRatio() throws Exception {
		QuantityInputDTO input = new QuantityInputDTO(new QuantityDTO(10.0, "FEET", "LengthUnit"),
				new QuantityDTO(2.0, "FEET", "LengthUnit"));

		Mockito.when(service.divide(any(), any(), isNull(), isNull())).thenReturn(divideResult);

		mockMvc.perform(post("/api/v1/quantities/divide").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(input))).andExpect(status().isOk())
				.andExpect(jsonPath("$.resultValue", is(5.0))).andExpect(jsonPath("$.resultUnit", is("RATIO")));
	}

	@Test
	@DisplayName("POST /divide — divide by zero  →  500 Internal Server Error")
	void testDivide_ByZero_Returns500() throws Exception {
		QuantityInputDTO input = new QuantityInputDTO(new QuantityDTO(1.0, "FEET", "LengthUnit"),
				new QuantityDTO(0.0, "INCHES", "LengthUnit"));

		Mockito.when(service.divide(any(), any(), isNull(), isNull()))
				.thenThrow(new ArithmeticException("Divide by zero"));

		mockMvc.perform(post("/api/v1/quantities/divide").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(input))).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(500)));
	}

	@Test
	@DisplayName("POST /compare — 1 FEET == 12 INCHES  →  resultString 'true'")
	void testCompare_EqualQuantities_ReturnsTrue() throws Exception {
		Mockito.when(service.compare(any(), any(), isNull(), isNull())).thenReturn(compareResult);

		mockMvc.perform(post("/api/v1/quantities/compare").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(lengthAddInput))).andExpect(status().isOk())
				.andExpect(jsonPath("$.resultString", is("true"))).andExpect(jsonPath("$.operation", is("compare")));
	}

	@Test
	@DisplayName("POST /compare — invalid unit name  →  400 Bad Request")
	void testCompare_InvalidUnit_Returns400() throws Exception {
		QuantityInputDTO bad = new QuantityInputDTO(new QuantityDTO(1.0, "FEET", "LengthUnit"),
				new QuantityDTO(12.0, "INCHE", "LengthUnit"));

		Mockito.when(service.compare(any(), any(), isNull(), isNull()))
				.thenThrow(new QuantityMeasurementException("Unit must be valid for the specified measurement type"));

		mockMvc.perform(post("/api/v1/quantities/compare").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(bad))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error", is("Quantity Measurement Error")));
	}

	@Test
	@DisplayName("POST /convert — 1 FEET → 12.0 INCHES  →  200 OK")
	void testConvert_FeetToInches_ReturnsCorrectValue() throws Exception {
		QuantityInputDTO input = new QuantityInputDTO(new QuantityDTO(1.0, "FEET", "LengthUnit"),
				new QuantityDTO(0.0, "INCHES", "LengthUnit"));

		Mockito.when(service.convert(any(), anyString(), isNull(), isNull())).thenReturn(convertResult);

		mockMvc.perform(post("/api/v1/quantities/convert").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(input))).andExpect(status().isOk())
				.andExpect(jsonPath("$.resultValue", is(12.0))).andExpect(jsonPath("$.resultUnit", is("INCHES")));
	}

	@Test
	@DisplayName("GET /history/operation/add — requires login")
	void testGetHistoryByOperation_RequiresLogin() throws Exception {
		mockMvc.perform(get("/api/v1/quantities/history/operation/add")).andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message", is("Please sign in to view your history")));
	}

	@Test
	@DisplayName("GET /history/operation/add — returns user-specific records when logged in")
	void testGetHistoryByOperation_ReturnsMatchingRecordsForLoggedInUser() throws Exception {
		Mockito.when(service.getHistoryByOperation(7L, "add")).thenReturn(List.of(addResult));

		mockMvc.perform(get("/api/v1/quantities/history/operation/add").principal(authPrincipal()))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].operation", is("add")));
	}

	@Test
	@DisplayName("GET /history/type/LengthUnit — returns length measurement records for logged-in user")
	void testGetHistoryByMeasurementType_ReturnsMatchingRecords() throws Exception {
		Mockito.when(service.getHistoryByMeasurementType(7L, "LengthUnit"))
				.thenReturn(List.of(addResult, compareResult));

		mockMvc.perform(get("/api/v1/quantities/history/type/LengthUnit").principal(authPrincipal()))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));
	}

	@Test
	@DisplayName("GET /count/compare — returns operation count for logged-in user")
	void testGetOperationCount_ReturnsCount() throws Exception {
		Mockito.when(service.getOperationCount(7L, "compare")).thenReturn(3L);

		mockMvc.perform(get("/api/v1/quantities/count/compare").principal(authPrincipal())).andExpect(status().isOk())
				.andExpect(content().string("3"));
	}

	@Test
	@DisplayName("GET /history/errored — returns error records for logged-in user")
	void testGetErrorHistory_ReturnsErrorRecords() throws Exception {
		QuantityMeasurementDTO errDto = new QuantityMeasurementDTO();
		errDto.setOperation("add");
		errDto.setError(true);
		errDto.setErrorMessage("Cannot perform arithmetic between different measurement categories");

		Mockito.when(service.getErrorHistory(7L)).thenReturn(List.of(errDto));

		mockMvc.perform(get("/api/v1/quantities/history/errored").principal(authPrincipal())).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].error", is(true)))
				.andExpect(jsonPath("$[0].errorMessage", containsString("Cannot perform arithmetic")));
	}

	@Test
	@DisplayName("POST /add — response Content-Type is application/json")
	void testContentNegotiation_ResponseIsJSON() throws Exception {
		Mockito.when(service.add(any(), any(), isNull(), isNull())).thenReturn(addResult);

		mockMvc.perform(post("/api/v1/quantities/add").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(lengthAddInput)))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	@Test
	@DisplayName("POST /add — response body contains all expected fields")
	void testResponseSerialization_AllFieldsPresent() throws Exception {
		Mockito.when(service.add(any(), any(), isNull(), isNull())).thenReturn(addResult);

		mockMvc.perform(post("/api/v1/quantities/add").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(lengthAddInput))).andExpect(jsonPath("$.thisValue").exists())
				.andExpect(jsonPath("$.thisUnit").exists()).andExpect(jsonPath("$.thisMeasurementType").exists())
				.andExpect(jsonPath("$.operation").exists()).andExpect(jsonPath("$.resultValue").exists())
				.andExpect(jsonPath("$.resultUnit").exists()).andExpect(jsonPath("$.error").exists());
	}
}
