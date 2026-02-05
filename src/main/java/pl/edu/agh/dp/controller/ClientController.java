package pl.edu.agh.dp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.dp.dto.ApiResponse;
import pl.edu.agh.dp.dto.ClientDto;
import pl.edu.agh.dp.service.ClientService;

import java.util.List;


@RestController
@RequestMapping("/api/clients")
@Tag(name = "Clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }


    @GetMapping("/")
    public ApiResponse<List<ClientDto>> getAllClients() {
        try {
            List<ClientDto> clients = clientService.findAll();
            return ApiResponse.success(clients, "Found " + clients.size() + " clients");
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch clients: " + e.getMessage(), 500);
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<ClientDto> getClientById(@PathVariable Long id) {
        try {
            return clientService.findById(id)
                    .map(dto -> ApiResponse.success(dto, "Client found"))
                    .orElse(ApiResponse.notFound("Client", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch client: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/")
    public ApiResponse<ClientDto> createClient(ClientDto dto) {
        try {
            ClientDto created = clientService.create(dto);
            return ApiResponse.created(created);
        } catch (Exception e) {
            return ApiResponse.error("Failed to create client: " + e.getMessage(), 400);
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<ClientDto> updateClient(Long id, ClientDto dto) {
        try {
            return clientService.update(id, dto)
                    .map(updated -> ApiResponse.success(updated, "Client updated successfully"))
                    .orElse(ApiResponse.notFound("Client", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to update client: " + e.getMessage(), 400);
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteClient(@PathVariable Long id) {
        try {
            if (clientService.delete(id)) {
                return ApiResponse.success(null, "Client deleted successfully");
            }
            return ApiResponse.notFound("Client", id);
        } catch (Exception e) {
            return ApiResponse.error("Failed to delete client: " + e.getMessage(), 500);
        }
    }

    @GetMapping("/byCompanyName")
    public ApiResponse<List<ClientDto>> searchByCompanyName(String companyName) {
        try {
            List<ClientDto> clients = clientService.findByCompanyName(companyName);
            return ApiResponse.success(clients, "Found " + clients.size() + " clients matching '" + companyName + "'");
        } catch (Exception e) {
            return ApiResponse.error("Failed to search clients: " + e.getMessage(), 500);
        }
    }


//
//    // ==================== DEMO SCENARIOS ====================
//
//    /**
//     * Demonstruje operacje na encji z JOINED inheritance.
//     */
//    public void demonstrateInheritanceOperations() {
//        System.out.println("=== Client (JOINED Inheritance) Demo ===\n");
//        System.out.println("Client extends Person - uses JOINED inheritance strategy");
//        System.out.println("Data is split between 'person' and 'client' tables\n");
//
//        // CREATE
//        System.out.println("1. Creating client with both Person and Client fields...");
//        ClientDto newClient = ClientDto.builder()
//                // Person fields
//                .firstName("Anna")
//                .lastName("Nowak")
//                .email("anna.nowak@techcorp.pl")
//                .phone("+48 123 456 789")
//                // Client-specific fields
//                .companyName("TechCorp Sp. z o.o.")
//                .taxId("PL1234567890")
//                .address("ul. Technologiczna 15, 00-001 Warszawa")
//                .build();
//
//        ApiResponse<ClientDto> createResponse = createClient(newClient);
//        System.out.println("   Create result: " + createResponse.getMessage());
//        System.out.println("   Created client: " + createResponse.getData());
//
//        if (createResponse.isSuccess() && createResponse.getData() != null) {
//            Long clientId = createResponse.getData().getId();
//
//            // READ - Demonstruje JOIN między tabelami
//            System.out.println("\n2. Reading client (ORM joins person + client tables)...");
//            ApiResponse<ClientDto> readResponse = getClientById(clientId);
//            System.out.println("   Read result: " + readResponse.getMessage());
//            ClientDto client = readResponse.getData();
//            System.out.println("   Person data: " + client.getFirstName() + " " + client.getLastName());
//            System.out.println("   Client data: " + client.getCompanyName() + ", NIP: " + client.getTaxId());
//
//            // SEARCH
//            System.out.println("\n3. Searching by company name...");
//            ApiResponse<List<ClientDto>> searchResponse = searchByCompanyName("Tech");
//            System.out.println("   Search result: " + searchResponse.getMessage());
//
//            // UPDATE
//            System.out.println("\n4. Updating client (updates both tables)...");
//            client.setAddress("ul. Nowa 20, 00-002 Kraków");
//            client.setPhone("+48 987 654 321");
//            ApiResponse<ClientDto> updateResponse = updateClient(clientId, client);
//            System.out.println("   Update result: " + updateResponse.getMessage());
//            System.out.println("   New address: " + updateResponse.getData().getAddress());
//
//            // DELETE - Demonstruje kaskadowe usunięcie z obu tabel
//            System.out.println("\n5. Deleting client (deletes from both tables)...");
//            ApiResponse<Void> deleteResponse = deleteClient(clientId);
//            System.out.println("   Delete result: " + deleteResponse.getMessage());
//        }
//
//        System.out.println("\n=== Demo completed ===");
//    }
}
