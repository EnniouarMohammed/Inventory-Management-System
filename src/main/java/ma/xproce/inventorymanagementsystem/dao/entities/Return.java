package ma.xproce.inventorymanagementsystem.dao.entities;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.Random;

@Entity
@Table(name = "returns")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Return {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private LocalDate date;

    @Size(max = 1000)
    @NotEmpty()
    private String returnNote;

    @Size(max = 1000)
    @NotEmpty()
    private String returnStatus;

    @Size(max = 1000)
    @NotEmpty()
    private String returnReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private UserIMS createdBy;

    public void generateReturnReference() {
        String prefix = "#IMS#Return#";
        Random random = new Random();
        int randomNumber = random.nextInt(999999);
        this.returnReference = prefix + String.format("%07d", randomNumber);
    }
}

