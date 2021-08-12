package com.ganzux.person.rest.controller;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

/**
 * @author Alvaro Alcedo Moreno - aalcedo
 * @version 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeControllerTest {

  @Autowired private MockMvc mockMvc;

  private static final String RESOLUTION_FILE_ENDPOINT = "/employees/all";

  private static final String SUPERVISORS = "/employees/supervisors";
  @Autowired
  private WebApplicationContext context;

  @Before
  public void setup() {
    mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
  }

  @Test
  @Order(1)
  @WithMockUser(username="spring", password = "secret")
  void test1() throws Exception {

    // save file
    String validFileInfor = "{" +
            "\"Pete\": \"Nick\"," +
            "\"Barbara\": \"Nick\"," +
            "\"Nick\": \"Sophie\"," +
            "\"Nick\": \"Sophie\"," +
            "\"Sophie\": \"Jonas\"" +
            "}";

    MockHttpServletResponse saveFile = postResolutionData(validFileInfor, RESOLUTION_FILE_ENDPOINT);
    Assertions.assertThat(saveFile.getStatus()).isEqualTo(201);

    MockHttpServletResponse row = getData(RESOLUTION_FILE_ENDPOINT + "/");
    Assertions.assertThat(row.getStatus()).isEqualTo(201);
    row.getContentAsString();

    MockHttpServletResponse supervisors = getData(SUPERVISORS + "/Barbara");
    Assertions.assertThat(supervisors.getStatus()).isEqualTo(200);
    System.out.println(supervisors.getContentAsString());

    supervisors = getData(SUPERVISORS + "/Pete");
    Assertions.assertThat(supervisors.getStatus()).isEqualTo(200);
    System.out.println(supervisors.getContentAsString());

    supervisors = getData(SUPERVISORS + "/Nick");
    Assertions.assertThat(supervisors.getStatus()).isEqualTo(200);
    System.out.println(supervisors.getContentAsString());

    supervisors = getData(SUPERVISORS + "/Sophie");
    Assertions.assertThat(supervisors.getStatus()).isEqualTo(200);
    System.out.println(supervisors.getContentAsString());

    supervisors = getData(SUPERVISORS + "/Jonas");
    Assertions.assertThat(supervisors.getStatus()).isEqualTo(200);
    System.out.println(supervisors.getContentAsString());
  }


  private MockHttpServletResponse postResolutionData(String reqBody, String endPoint)
      throws Exception {
    return mockMvc
        .perform(
            MockMvcRequestBuilders
                    .post(endPoint)
                    .content(reqBody)
                    .contentType(MediaType.APPLICATION_JSON)
//                    .header(HttpHeaders.AUTHORIZATION,
//                            "Basic " + Base64Utils.encodeToString("spring:secret".getBytes()))
        )
        .andReturn()
        .getResponse();
  }

  private MockHttpServletResponse getData(String endPoint) throws Exception {
    return mockMvc
            .perform(MockMvcRequestBuilders
                    .get(endPoint)
                    .contentType(MediaType.APPLICATION_JSON)
//                    .header(HttpHeaders.AUTHORIZATION,
//                            "Basic " + Base64Utils.encodeToString("spring:secret".getBytes()))
            )
            .andReturn()
            .getResponse();
  }

}
