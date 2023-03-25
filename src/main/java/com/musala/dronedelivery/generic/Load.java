package com.musala.dronedelivery.generic;

import com.musala.dronedelivery.entity.Drone;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "load_items")
public class Load<T extends BaseItem> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Drone drone;

    @ManyToMany(targetEntity = BaseItem.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<T> items;

    public Load(Drone drone, List<T> items) {
        this.drone = drone;
        this.items = items;
    }

    public List<String> getItemCodes() {
        return items.stream()
                .map(Item::getCode)
                .toList();
    }

}