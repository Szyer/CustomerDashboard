package com.assignment.customer.service;

import com.assignment.customer.model.CustomerModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Data
@RequiredArgsConstructor
public class CustomerServiceIMPL implements CustomerService {
    private final RestTemplate restTemplate;

    private static final String BASE_URL = "https://qa2.sunbasedata.com/sunbase/portal/api/";



    private String token;

    @Override
    public List<CustomerModel> getAllCustomers() {
        HttpHeaders headers = createHeadersWithAuthorization();
        String url = BASE_URL + "assignment.jsp?cmd=get_customer_list";

        ResponseEntity<CustomerModel[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CustomerModel[].class
        );

        if (response.getStatusCode()==HttpStatus.OK) {
            return Arrays.asList(response.getBody());
        } else {
            throw new RuntimeException("Failed to fetch customer data: " + response.getStatusCodeValue());
        }
    }


    @Override
    public ResponseEntity<String> updateCustomer(String uuid, CustomerModel customer) throws JsonProcessingException {
        HttpHeaders headers = createHeadersWithAuthorization();

        if (!isValidCustomer(customer)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid customer data");
        }

        String updateUrl = BASE_URL + "assignment.jsp?cmd=update&uuid=" + uuid;

        ResponseEntity<String> response = restTemplate.exchange(
                updateUrl,
                HttpMethod.POST,
                new HttpEntity<>(customer, headers),
                String.class
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.OK).body("Successfully Updated");
        } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("UUID not found");
        } else if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Not Updated");
        } else {
            throw new RuntimeException("Failed to update customer: " + response.getStatusCodeValue());
        }
    }



    @Override
    public ResponseEntity<String> createCustomer(CustomerModel customer) throws JsonProcessingException {
        HttpHeaders headers = createHeadersWithAuthorization();

        if (!isValidCustomer(customer)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid customer data");
        }

        String createUrl = BASE_URL + "assignment.jsp?cmd=create";

        ResponseEntity<String> response = restTemplate.exchange(
                createUrl,
                HttpMethod.POST,
                new HttpEntity<>(customer, headers),
                String.class
        );

        if (response.getStatusCode() == HttpStatus.CREATED) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Successfully Created");
        } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create customer. Please check the data.");
        } else {
            throw new RuntimeException("Failed to create customer: " + response.getStatusCodeValue());
        }
    }

    @Override
    public ResponseEntity<String> deleteCustomer(String uuid) {
        HttpHeaders headers = createHeadersWithAuthorization();

        String deleteUrl = BASE_URL + "assignment.jsp?cmd=delete&uuid=" + uuid;

        ResponseEntity<String> response = restTemplate.exchange(
                deleteUrl,
                HttpMethod.POST,
                new HttpEntity<>(headers),
                String.class
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted");
        } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("UUID not found");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Not deleted");
        }
    }

    @Override
    public ResponseEntity<String> loginUser(String loginId, String password) throws JsonProcessingException {
        String authUrl = BASE_URL + "/assignment_auth.jsp";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = Map.of("login_id", loginId, "password", password);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(authUrl, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            if (responseBody != null) {
                Map<String, Object> responseJson = new ObjectMapper().readValue(responseBody, Map.class);
                String accessToken = (String) responseJson.get("access_token");
                this.token = "Bearer " + accessToken;
                System.out.println(this.token);
            } else {
                throw new RuntimeException("Authentication response does not contain a valid token.");
            }
        } else {
            throw new RuntimeException("Authentication failed: " + response.getStatusCodeValue());
        }
        return response;
    }

    private HttpHeaders createHeadersWithAuthorization() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }

    private boolean isValidCustomer(CustomerModel customer) {
        // Check if first_name and last_name are not empty
        return isNotEmpty(customer.getFirst_name()) && isNotEmpty(customer.getLast_name());
    }

    private boolean isNotEmpty( @NotNull String value) {
        return value != null && !value.trim().isEmpty();
    }
}
