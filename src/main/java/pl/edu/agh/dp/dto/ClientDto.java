package pl.edu.agh.dp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.agh.dp.entity.Client;

/**
 * DTO dla encji Client.
 * Demonstruje dziedziczenie JOINED - Client extends Person.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {

//    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String companyName;
    private String taxId;
    private String address;

    /**
     * Konwertuje encję Client na DTO.
     */
    public static ClientDto fromEntity(Client client) {
        if (client == null) {
            return null;
        }

        return ClientDto.builder()
//                .id(client.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .email(client.getEmail())
                .phone(client.getPhone())
                .companyName(client.getCompanyName())
                .taxId(client.getTaxId())
                .address(client.getAddress())
                .build();
    }

    /**
     * Konwertuje DTO na nową encję Client.
     */
    public Client toEntity() {
        Client client = new Client();
//        client.setId(this.id);
        client.setFirstName(this.firstName);
        client.setLastName(this.lastName);
        client.setEmail(this.email);
        client.setPhone(this.phone);
        client.setCompanyName(this.companyName);
        client.setTaxId(this.taxId);
        client.setAddress(this.address);
        return client;
    }
}
