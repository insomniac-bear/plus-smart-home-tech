package ru.yandex.practicum.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "items")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    private UUID id;

    @Column(name = "fragile")
    @NotNull
    private boolean fragile;

    @Column(name = "width")
    @NotNull
    private double width;

    @Column(name = "height")
    @NotNull
    private double height;

    @Column(name = "depth")
    @NotNull
    private double depth;

    @Column(name = "weight")
    @NotNull
    private double weight;

    @Column(name = "quantity")
    @NotNull
    private long quantity;

    @Column(name = "reserved_quantity")
    @NotNull
    private long reservedQuantity;

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", fragile=" + fragile +
                ", width=" + width +
                ", height=" + height +
                ", depth=" + depth +
                ", weight=" + weight +
                ", quantity=" + quantity +
                ", reservedQuantity=" + reservedQuantity +
                '}';
    }
}
