package ma.xproce.inventorymanagementsystem.dao.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty()
    @Size(max = 100)
    private String name;

    @NotEmpty()
    @Size(max = 50)
    private String code;

    @Min(value = 0)
    private double price;

    @Min(value = 0)
    private double cost;

    @Min(value = 0)
    private int quantity;

    @NotNull
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String image;

    @Size(max = 1000)
    @NotEmpty()
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull()
    private Category category;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private UserIMS createdBy;
}