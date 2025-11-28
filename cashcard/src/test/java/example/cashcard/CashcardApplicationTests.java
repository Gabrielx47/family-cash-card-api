package example.cashcard;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import java.net.URI;
import java.security.Principal;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class CashCardApplicationTests {
    @Autowired
    TestRestTemplate restTemplate;
    @InjectMocks
    private CashCardController cashCardController;
    private MockMvc mockMvc;
    @Mock
    CashCardRepository cashCardRepository;
    
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(cashCardController).build();
    }
    
    /*@Test
    @Transactional
    void shouldReturnACashCardWhenDataIsSaved() {
        CashCard saved = new CashCard(99L, 123.45, "sarah1");
        when(cashCardRepository.findByIdAndOwner(99L, "sarah1"))
                .thenReturn(saved);
        
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity("/cashcards/99", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	DocumentContext documentContext = JsonPath.parse(response.getBody());
	Number id = documentContext.read("$.id");
	assertThat(id).isEqualTo(99);

	Double amount = documentContext.read("$.amount");
	assertThat(amount).isEqualTo(123.45);
                
        String owner = documentContext.read("$.owner");
        assertThat(owner).isEqualTo("sarah1");
    }*/
    
    @Test
    void shouldReturnACashCardWhenDataIsSaved() throws Exception {

        CashCard saved = new CashCard(99L, 123.45, "sarah1");

        when(cashCardRepository.findByIdAndOwner(99L, "sarah1"))
                .thenReturn(saved);

        MvcResult result = mockMvc.perform(
                        get("/cashcards/99")
                                .principal(() -> "sarah1"))
                .andExpect(status().isOk())
                .andReturn();
        DocumentContext json = JsonPath.parse(result.getResponse().getContentAsString());

        assertThat(json.read("$.id", Number.class)).isEqualTo(99);
        assertThat(json.read("$.amount", Double.class)).isEqualTo(123.45);
        assertThat(json.read("$.owner", String.class)).isEqualTo("sarah1");

        verify(cashCardRepository).findByIdAndOwner(99L, "sarah1");
    }

    @Test
    void shouldNotReturnACashCardWithAnUnknownId() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity("/cashcards/1000", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isBlank();
    }
    
    /*@Test
    //@DirtiesContext
    void shouldCreateANewCashCard() {
        CashCard newCashCard = new CashCard(null, 250.00, null);
        ResponseEntity<Void> createResponse = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .postForEntity("/cashcards", newCashCard, Void.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI locationOfNewCashCard = createResponse.getHeaders().getLocation();
         ResponseEntity<String> getResponse = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity(locationOfNewCashCard.getPath(), String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Number id = documentContext.read("$.id");
        Double amount = documentContext.read("$.amount");

        assertThat(id).isNotNull();
        assertThat(amount).isEqualTo(250.00);
    }*/
    
    @Test
    @Transactional
    void shouldCreateANewCashCard() {
        // dado que o POST vai salvar isso
        CashCard saved = new CashCard(1L, 250.00, "sarah1");
        cashCardRepository.save(saved);

        CashCard newCashCard = new CashCard(null, 250.00, null);

        ResponseEntity<Void> createResponse = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .postForEntity("/cashcards", newCashCard, Void.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // A Location vem do controller, então mantém
        URI locationOfNewCashCard = createResponse.getHeaders().getLocation();
        assertThat(locationOfNewCashCard.getPath()).isEqualTo("/cashcards/1");
    }

    /*@Test
    void shouldReturnAllCashCardsWhenListIsRequested() {
        
        CashCard saved = new CashCard(99L, 123.45, "sarah1");

        when(cashCardRepository.find(99L, "sarah1"))
                .thenReturn(saved);
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity("/cashcards", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int cashCardCount = documentContext.read("$.length()");
        assertThat(cashCardCount).isEqualTo6(3);

        JSONArray ids = documentContext.read("$..id");
        assertThat(ids).containsExactlyInAnyOrder(99, 100, 101);

        JSONArray amounts = documentContext.read("$..amount");
        assertThat(amounts).containsExactlyInAnyOrder(123.45, 1.00, 150.00);
    }*/
    
    @Test
    void shouldReturnAllCashCardsWhenListIsRequested() throws Exception {
        // --- MOCK DOS 3 REGISTROS ---
        List<CashCard> cards = List.of(
                new CashCard(99L, 123.45, "sarah1"),
                new CashCard(100L, 1.00, "sarah1"),
                new CashCard(101L, 150.00, "sarah1")
        );

        Page<CashCard> page = new PageImpl<>(cards);

        when(cashCardRepository.findByOwner(eq("sarah1"), any(PageRequest.class)))
                .thenReturn(page);

        // --- CHAMADA COM PAGE + SIZE ---
        MvcResult result = mockMvc.perform(
                        get("/cashcards")
                                .principal(() -> "sarah1")
                )
                .andExpect(status().isOk())
                .andReturn();

        // --- VALIDA JSON ---
        DocumentContext json = JsonPath.parse(result.getResponse().getContentAsString());

        assertThat(json.read("$.length()", Integer.class)).isEqualTo(3);

        assertThat(json.read("$..id", List.class))
                .containsExactlyInAnyOrder(99, 100, 101);

        assertThat(json.read("$..amount", List.class))
                .containsExactlyInAnyOrder(123.45, 1.00, 150.00);

        //verify(cashCardRepository).findByOwner(eq("sarah1"), any(PageRequest.class));
    }

    @Test
    void shouldReturnAPageOfCashCards() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity("/cashcards?page=0&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray page = documentContext.read("$[*]");
        assertThat(page.size()).isEqualTo(1);
    }

    @Test
    void shouldReturnASortedPageOfCashCards() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity("/cashcards?page=0&size=1&sort=amount,desc", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray read = documentContext.read("$[*]");
        assertThat(read.size()).isEqualTo(1);

        Double amount = documentContext.read("$[0].amount");
        assertThat(amount).isEqualTo(150.00);
    }

    @Test
    void shouldReturnASortedPageOfCashCardsById() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity("/cashcards", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray page = documentContext.read("$[*]");
        assertThat(page.size()).isEqualTo(3);

        JSONArray amounts = documentContext.read("$..amount");
        assertThat(amounts).containsExactly(1.00, 123.45, 150.00);
    }
    
   
    @Test
    void shouldNotReturnACashCardWhenUsingBadCredentials() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("BAD-USER", "abc123")
                .getForEntity("/cashcards/99", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        response = restTemplate
                .withBasicAuth("sarah1", "BAD-PASSWORD")
                .getForEntity("/cashcards/99", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
    
    @Test
    void shouldRejectUsersWhoAreNotCardOwners() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("hank-owns-no-cards", "qrs456")
                .getForEntity("/cashcards/99", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
    
    @Test
    void shouldNotAllowAccessToCashCardsTheyDoNotOwn() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity("/cashcards/102", String.class); // kumar2's data
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private RequestPostProcessor user(String sarah1) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
