package itmo.dto;

import itmo.entities.Passport;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder(builderMethodName = "clientBuilder")
public class ClientDto {
    /**
     * The name of the client.
     */
    private String name;

    /**
     * The birthday of the client.
     */
    private Date birthday;

    /**
     * The address of the client.
     */
    private String address;

    /**
     * The passport details of the client.
     */
    private Passport passport;

    /**
     * The bank associated with the client.
     */
    private String bank;

    /**
     * Returns a custom builder for ClientDto.
     * @return a new instance of CustomClientDtoBuilder
     */
    public static CustomClientDtoBuilder customBuilder() {
        return new CustomClientDtoBuilder();
    }

    public static class CustomClientDtoBuilder {
        private String name;
        private Date birthday;
        private String address;
        private Passport passport;
        private String bank;

        public CustomClientDtoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CustomClientDtoBuilder birthday(Date birthday) {
            this.birthday = birthday;
            return this;
        }

        public CustomClientDtoBuilder address(String address) {
            this.address = address;
            return this;
        }

        public CustomClientDtoBuilder passport(Passport passport) {
            this.passport = passport;
            return this;
        }

        public CustomClientDtoBuilder bank(String bank) {
            this.bank = bank;
            return this;
        }

        public ClientDto build() {
            ClientDto clientDto = ClientDto.clientBuilder()
                    .name(this.name)
                    .birthday(this.birthday)
                    .address(this.address)
                    .passport(this.passport)
                    .bank(this.bank)
                    .build();
            if (clientDto.name == null || clientDto.name.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be null or empty");
            }
            if (clientDto.birthday == null) {
                throw new IllegalArgumentException("Birthday cannot be null");
            }
            if (clientDto.bank == null) {
                throw new IllegalArgumentException("Bank id cannot be null");
            }
            return clientDto;
        }
    }
}
