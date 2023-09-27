package modules.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cuotas")
@Data
public class CuotaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private Integer numeroCuota;
    private Float valorCuota;
    private String estadoCuota;  // puede ser listo/pendiente

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaVencimiento;


    // para asociarlo a un pago
    @ManyToOne
    @JoinColumn(name = "pago_id", nullable = false)
    private GeneratePaymentsEntity generatePaymentsEntity;





}
