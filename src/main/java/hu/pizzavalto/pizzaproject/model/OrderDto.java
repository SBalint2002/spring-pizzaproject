package hu.pizzavalto.pizzaproject.model;

import lombok.Data;

import java.util.Date;

/**
 * Rendelés DataTransferObjektum.
 */
@Data
public class OrderDto {
    /**
     * Rendelés DataTransferObjektum id-je.
     */
    private Long id;
    /**
     * Rendelés DataTransferObjektum felhasználó id-je.
     */
    private Long userId;
    /**
     * Rendelés DataTransferObjektum helyszíne.
     */
    private String location;
    /**
     * Rendelés DataTransferObjektum rendelésiDátuma.
     */
    private Date orderDate;
    /**
     * Rendelés DataTransferObjektum ára.
     */
    private int price;
    /**
     * Rendelés DataTransferObjektum telefonszáma.
     */
    private String phoneNumber;
    /**
     * Rendelés DataTransferObjektum státusza.
     */
    private boolean ready;

    /**
     * Rendelés DataTransferObjektum konstruktora.
     *
     * @param order Rendelés típusú adatot vár, aminek segítségével példányosítja a DataTransferObjektum-ot.
     */
    public OrderDto(Order order) {
        this.id = order.getId();
        this.userId = order.getUser().getId();
        this.location = order.getLocation();
        this.orderDate = order.getOrder_date();
        this.price = order.getPrice();
        this.phoneNumber = order.getPhone_number();
        this.ready = order.isReady();
    }
}