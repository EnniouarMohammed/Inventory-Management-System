package ma.xproce.inventorymanagementsystem.dao.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    public String image;

    @NotEmpty
    private String categoryName;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @NotNull
    private List<Product> products;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private UserIMS createdBy;
}