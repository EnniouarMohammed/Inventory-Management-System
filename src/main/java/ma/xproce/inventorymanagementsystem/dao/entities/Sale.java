package ma.xproce.inventorymanagementsystem.dao.entities;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Entity
@Table(name = "sales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private LocalDate date;

    @Min(value = 0)
    private Integer quantity;

    @NotEmpty()
    private String saleStatus;

    @NotEmpty()
    private String paymentStatus;

    @Size(max = 1000)
    @NotEmpty()
    private String saleNote;

    @NotEmpty()
    private String reference;

    private boolean refunded;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @NotNull()
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @NotNull
    private Product product;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private UserIMS createdBy;

    public void generateReference() {
        String prefix = "#IMS#Sale#";
        Random random = new Random();
        int randomNumber = random.nextInt(999999);
        this.reference = prefix + String.format("%07d", randomNumber);
    }
}